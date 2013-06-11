/*
 * Copyright (c) 2010, The OpenRTB Project
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   1. Redistributions of source code must retain the above copyright notice,
 *      this list of conditions and the following disclaimer.
 *
 *   2. Redistributions in binary form must reproduce the above copyright
 *      notice, this list of conditions and the following disclaimer in the
 *      documentation and/or other materials provided with the distribution.
 *
 *   3. Neither the name of the OpenRTB nor the names of its contributors
 *      may be used to endorse or promote products derived from this
 *      software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package org.openrtb.dsp.core;

import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;

import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.io.Decoder;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.io.Encoder;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.protobuf.ProtobufDatumReader;
import org.apache.avro.protobuf.ProtobufDatumWriter;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificDatumWriter;
import org.apache.avro.thrift.ThriftDatumReader;
import org.apache.avro.thrift.ThriftDatumWriter;
import org.codehaus.jackson.map.ObjectMapper;
import org.openrtb.common.api.BidRequest;
import org.openrtb.common.api.BidResponse;
import org.openrtb.common.api.OpenRTBAPI;
import org.openrtb.dsp.intf.model.DSPException;
import org.openrtb.dsp.intf.model.DemandSideDAO;
import org.openrtb.dsp.intf.model.RTBRequestWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

public class DemandSideServer {
	public DemandSideServer() {
		super();
	}

	private static final Logger logger = LoggerFactory
			.getLogger(DemandSideServer.class);
	// the singleton DAO object to share config variables.
	private DemandSideDAO dspDAO;

	// An instance of the open RTB API spec implementation
	// processes a separate instance of BidRequest object received from SSP
	// and returns a new instance of BidResponse object or null
	private OpenRTBAPI bidder = null;

	// TODO: private BlocklistAPI blocklistRequester = null;

	public DemandSideServer(OpenRTBAPI bidder, DemandSideDAO dao) {
		this.bidder = bidder;
		this.dspDAO = dao;
	}

	public void init(String dbLocation) throws DSPException {
		dspDAO.loadData(dbLocation);
	}

	// DemandSideDAO requires ConcurrentMap implementation for storing Exchanges
	// hence, get() access to this is threadsafe
	public boolean authorizeRemoteService(String sspOrgName) {
		if (dspDAO.getExchanges().get(sspOrgName) != null) {
			return true;
		}
		return false;
	}

	// Though access to ConcurrentMap.get() is thread-safe, accessing underlying
	// object's methods
	// is not guaranteed to be thread safe, hence synchronized access to this
	// method
	public synchronized boolean verifyContentType(String sspName,
			String contentType) throws DSPException {
		if (authorizeRemoteService(sspName)) {
			if (dspDAO.getExchanges().get(sspName).getRtbContentType()
					.equals(contentType)) {
				return true;
			}
		}
		return false;
	}

	// we dont use avro / ipc - just use basic avro.io classes to translate
	// request-buffers into a BidRequest object, and invoke the bidder's
	// 'process' method
	public byte[] respond(String sspName, InputStream inStream,
			String requestContentType) throws DSPException {
		try {			
			// create a new BidRequest object by decoding the input stream
			BidRequest bidRequest = readRequest(inStream, requestContentType);
			// wrap this request object with additional info from the DAO
			RTBRequestWrapper wReq = new RTBRequestWrapper(bidRequest);
			long reqTimeout = dspDAO.getDefaultTimeout("request_timeout");
			long offerTimeout = dspDAO.getDefaultTimeout("offer_timeout");

			// copy the context into the newly created wrapped request,
			// the bidder always reads from this local copy of the context
			wReq.setContext(dspDAO.getExchanges().get(sspName),
					dspDAO.getAdvertisers(), reqTimeout, offerTimeout);

			// process the request in the bidder implementation instance
			BidResponse bidResponse = bidder.process(wReq);			
			// encode the resulting BidResponse object in the expected encoding
			// format
			
			return writeResponse(bidResponse, requestContentType);
		} catch (Exception e) {
			throw new DSPException(e);
		}
	}

	protected static DecoderFactory DECODER_FACTORY = DecoderFactory.get();
	protected static EncoderFactory ENCODER_FACTORY = EncoderFactory.get();
	protected static final String JSON_CONTENT_TYPE = "application/json";
	protected static final String THRIFT_CONTENT_TYPE = "application/x-thrift";
	protected static final String PROTOBUF_CONTENT_TYPE = "application/x-protobuf";
	protected static final String AVRO_BINARY_CONTENT_TYPE = "avro/binary";

	protected Decoder getBidRequestDecoder(InputStream is, String contentType)
			throws IOException {
		Decoder decoder = null;		
		return decoder = DECODER_FACTORY.binaryDecoder(is, null);		
	}

	protected Encoder getBidResponseEncoder(OutputStream out, String contentType)
			throws IOException {
		Encoder encoder = null;		
		return	encoder = ENCODER_FACTORY.binaryEncoder(out, null);	
	}

	protected DatumReader<BidRequest> getDatumReader(String contentType) {
		DatumReader<BidRequest> reader = null;
		if (contentType.equals(AVRO_BINARY_CONTENT_TYPE)){
			reader = new SpecificDatumReader<BidRequest>(BidRequest.SCHEMA$);
		} else if (contentType.equals(PROTOBUF_CONTENT_TYPE)) {
			reader = new ProtobufDatumReader<BidRequest>(BidRequest.class);
		} else if (contentType.equals(THRIFT_CONTENT_TYPE)) {
			reader = new ThriftDatumReader<BidRequest>(BidRequest.SCHEMA$);
		}
		return reader;
	}

	protected DatumWriter<BidResponse> getDatumWriter(String contentType) {
		DatumWriter<BidResponse> writer = null;
		if (contentType.equals(AVRO_BINARY_CONTENT_TYPE)) {
			writer = new SpecificDatumWriter<BidResponse>(BidResponse.SCHEMA$);
		} else if (contentType.equals(PROTOBUF_CONTENT_TYPE)) {
			writer = new ProtobufDatumWriter<BidResponse>(BidResponse.class);
		} else if (contentType.equals(THRIFT_CONTENT_TYPE)) {
			writer = new ThriftDatumWriter<BidResponse>(BidResponse.class);
		}
		return writer;
	}

	public BidRequest readRequest(InputStream is, String contentType)
			throws DSPException {
		BidRequest bidRequest = null;
		if(contentType.equals(JSON_CONTENT_TYPE))			
		{
			try{			
				bidRequest = (BidRequest)new ObjectMapper().readValue(is,BidRequest.class);			
			}catch(Exception ex)
			{
				logger.error("Json Mapping Exception : " + ex.getMessage());
				throw new DSPException(ex);
			}
		}
		else {	
			try {
				Decoder in = getBidRequestDecoder(is, contentType);
				DatumReader<BidRequest> reader = getDatumReader(contentType);
				// Should only be 1 request reader.read(arg0, arg1)
				bidRequest = reader.read(null, in);
				logger.info("Read Request: " + bidRequest);
			} catch (EOFException eof) {
				logger.error("End of file: " + eof.getMessage());
				throw new DSPException(eof);
			} catch (Exception ex) {
				logger.error("Error in processing request " + ex.getMessage());
				throw new DSPException(ex);
			}
		}
		return bidRequest;
	}
	protected byte[] writeResponse(BidResponse bidResponse, String contentType)
			throws DSPException {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		if(contentType.equals(JSON_CONTENT_TYPE))
		{		
	        try {	        		        				
				Gson gson = new Gson();
				String jsonResponse = gson.toJson(bidResponse);				
				os.write(jsonResponse.getBytes());						       
		        return os.toByteArray();
	        } catch (Exception ex) {
				logger.error("Error in writing Json response : " + ex.getMessage());	
				throw new DSPException(ex);
			}			
		}	
		else{
			try {			
				Encoder out = getBidResponseEncoder(os, contentType);
				DatumWriter<BidResponse> writer = getDatumWriter(contentType);
				writer.write(bidResponse, out);
				out.flush();		
			 // for all binary output, the data needs to be serialized
				ByteBuffer serialized = ByteBuffer
						.allocate(os.toByteArray().length);
				serialized.put(os.toByteArray());
				return serialized.array();
			} catch (Exception ex) {
				logger.error("Error in writing response buffer: " + ex.getMessage());
				throw new DSPException(ex);
			}
		}
	}
	
	
	

}
