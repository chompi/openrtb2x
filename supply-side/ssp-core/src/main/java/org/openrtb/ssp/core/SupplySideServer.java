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
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFT:) WARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package org.openrtb.ssp.core;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.openrtb.common.json.AdvertiserBlocklistRequestTranslator;
import org.openrtb.common.json.AdvertiserBlocklistResponseTranslator;
import org.openrtb.common.model.Advertiser;
import org.openrtb.common.model.AdvertiserBlocklistRequest;
import org.openrtb.common.model.AdvertiserBlocklistResponse;
import org.openrtb.common.model.Identification;
import org.openrtb.common.model.Status;
import org.openrtb.ssp.SupplySideService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The stateless processing of batch Open RTB JSON requests resulting in JSON responses.
 * Besides translation of JSON to internal model objects it verifies the requests and
 * signs the responses. Its dependency on an SSP implementor is defined by the 
 * {@link SupplySideService} interface. 
 *
 * @since 1.0.1
 */
public class SupplySideServer {

    private static final Logger log = LoggerFactory.getLogger(SupplySideServer.class);
	private SupplySideService ssp;

	private AdvertiserBlocklistRequestTranslator reqTrans =
           new AdvertiserBlocklistRequestTranslator();
	private AdvertiserBlocklistResponseTranslator resTrans =
           new AdvertiserBlocklistResponseTranslator();

	public SupplySideServer(SupplySideService ssp)
	{
		this.ssp = ssp;
	}
	
	/**
	 * Processes Open RTB JSON requests. Returns JSON-formatted responses.
	 * @param jsonRequest
	 */
	public String process(String jsonRequest) {
		AdvertiserBlocklistRequest request = null;
		AdvertiserBlocklistResponse response = new AdvertiserBlocklistResponse();
		Status status = new Status("n/a");
		String requestToken = null;
		String jsonResponse = null;
		Identification identification = new Identification(ssp.getOrganization(),System.currentTimeMillis());
		String dsp = null;

		//process request
		try {
			//translate and verify request
			request = reqTrans.fromJSON(jsonRequest);
			dsp = request.getIdentification().getOrganization();
			if (dsp==null || !request.verify(ssp.getSharedSecret(dsp),reqTrans)) throw new IllegalArgumentException("Invalid MD5 checksum");
			requestToken = request.getIdentification().getToken(); 
			status.setRequestToken(requestToken);

			//obtain block lists
			Collection<Advertiser> advertisers = request.getAdvertisers();
			advertisers = ssp.setBlocklists(advertisers);
			response.setAdvertisers(advertisers);

			//set success code
			status.setResponseCode(Status.SUCCESS_CODE, Status.SUCCESS_MESSAGE);

		} catch (IllegalArgumentException e) {
			status.setResponseCode(Status.AUTH_ERROR_CODE, e.getMessage());
		} catch (JsonMappingException e) {
			//e.printStackTrace();
			status.setResponseCode(Status.OTHER_ERROR_CODE, e.getMessage());
		} catch (JsonParseException e) {
			//e.printStackTrace();
			status.setResponseCode(Status.OTHER_ERROR_CODE, e.getMessage());
		} catch (IOException e) {
			//e.printStackTrace();
			status.setResponseCode(Status.OTHER_ERROR_CODE, e.getMessage());
		}
		//set status
		response.setStatus(status);
		//set response identification
		response.setIdentification(identification);
		//translate response and add a MD5 token
		try {
			if (dsp!=null)
				response.sign(ssp.getSharedSecret(dsp), resTrans);
			jsonResponse = resTrans.toJSON(response);
		} catch (Exception e) {
			//what to do in this case? ... HTTP error?
			log.error("Response signing/translation failed",e);
			jsonResponse = null;
		}
		return jsonResponse;
	}

}
