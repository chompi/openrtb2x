package org.openrtb.ssp.core;

import java.io.IOException;
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
import org.openrtb.common.util.MD5Checksum;
import org.openrtb.ssp.OpenRtbSsp;

public class OpenRtbSspServer {
	
	private OpenRtbSsp ssp;
	
	private AdvertiserBlocklistRequestTranslator reqTrans =
           new AdvertiserBlocklistRequestTranslator();
	private AdvertiserBlocklistResponseTranslator resTrans =
           new AdvertiserBlocklistResponseTranslator();
	   
	public OpenRtbSspServer(OpenRtbSsp ssp)
	{
		this.ssp = ssp;
	}
	
	String process(String jsonRequest) {
		AdvertiserBlocklistRequest request = null;
		AdvertiserBlocklistResponse response = new AdvertiserBlocklistResponse();
		Status status = new Status("n/a");
		String requestToken = null;
		String jsonResponse = null;
		Identification identification = new Identification(ssp.getOrganization(),System.currentTimeMillis(),"");
		
		//process request
		try {
			//translate request
			request = reqTrans.fromJSON(jsonRequest);
			requestToken = request.getIdentification().getToken();
			status.setRequestToken(requestToken);
			
			//validate request by checking the MD5 checksum
			request.getIdentification().setToken("");
			String calcToken = MD5Checksum.getMD5Checksum(reqTrans.toJSON(request)+ssp.getSharedSecret());
			if (!calcToken.equals(requestToken)) throw new IllegalArgumentException("Invalid MD5 checksum");
			
			//obtain block lists
			List<Advertiser> advertisers = request.getAdvertisers();
			ssp.setBlocklists(advertisers);
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
			//calculate a checksum with the token value of ""
			response.getIdentification().setToken("");
			jsonResponse = resTrans.toJSON(response);
			String token = MD5Checksum.getMD5Checksum(jsonResponse+ssp.getSharedSecret());
			response.getIdentification().setToken(token);
			jsonResponse = resTrans.toJSON(response);
		} catch (Exception e) {
			//what to do in this case? ... HTTP error?
			e.printStackTrace();
			jsonResponse = null;
		}		
		return jsonResponse;
	}

}
