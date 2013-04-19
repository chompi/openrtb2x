package org.openrtb.dsp.core;

import java.util.ArrayList;



import java.util.Map;

import org.apache.avro.AvroRemoteException;
import org.openrtb.common.api.*;
import org.openrtb.dsp.intf.model.RTBAdvertiser;
import org.openrtb.dsp.intf.model.RTBRequestWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OpenRTBAPIDummyTest implements OpenRTBAPI 
{
	
	private final Logger logger = LoggerFactory.getLogger(OpenRTBAPIDummyTest.class);
	private long lastBidNum;
	private final String adId = "AD123456789";

	public OpenRTBAPIDummyTest() {
		lastBidNum = 1L;
	}
	

	@Override
	public BidResponse process(BidRequest request) throws AvroRemoteException {
		BidResponse response = null;
		
		if (validateRequest(request)) {
			RTBRequestWrapper wReq = (RTBRequestWrapper) request;
			response = new BidResponse();
			response.id = wReq.getRequest().id;
			response.bidid = "simple-bid-tracker";
			
			Map<String, String> seats = wReq.getUnblockedSeats(wReq.getSSPName());
			for (Impression i : wReq.getRequest().imp) {
				for (Map.Entry<String, String> s : seats.entrySet()) {
					RTBAdvertiser a = wReq.getAdvertiser(s.getValue());
					
					SeatBid seat_bid = new SeatBid();
					seat_bid.seat = s.getKey();
					seat_bid.bid = new ArrayList<Bid>();
					
					Bid b = new Bid();
					b.id = "SimpleBid#"+lastBidNum++;
					b.impid = i.id;					
					b.price = i.bidfloor + (float) 0.10; // always bid 10 cents more than the floor
					b.nurl = a.nurl;
					b.adid = adId; // serves up the same ad to all impressions
					seat_bid.bid.add(b);
					
					response.seatbid.add(seat_bid);
				}
			}
		}
		return response;
	}
	
	public boolean validateRequest(BidRequest request) {
		if (request == null) {
			logger.error("BidRequest object was null");
			return false;
		} else {
			boolean error = false;
			if (error = (request.id == null))
				logger.error("BidRequest must have valid Id");
			if (error = ((request.imp == null) || request.imp.isEmpty()))
				logger.error("BidRequest must have one or more impressions");
			if (error = ((request.site == null) && (request.app == null)))
				logger.error("BidRequest must have at least site or app object");
			if (error)
				return false;
		}
		return true;
	}
}
