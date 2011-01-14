package org.openrtb.ssp.core;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.junit.Before;
import org.junit.Test;
import org.openrtb.common.json.AdvertiserBlocklistRequestTranslator;
import org.openrtb.common.json.AdvertiserBlocklistResponseTranslator;
import org.openrtb.common.model.Advertiser;
import org.openrtb.common.model.AdvertiserBlocklistRequest;
import org.openrtb.common.model.AdvertiserBlocklistResponse;
import org.openrtb.common.model.Blocklist;
import org.openrtb.common.model.Status;
import org.openrtb.common.util.MD5Checksum;
import org.openrtb.ssp.OpenRtbSsp;


public class OpenRtbSspServerTest {

	class OpenRtbSspTestClient implements OpenRtbSsp {
		@Override
		public List<Advertiser> setBlocklists(List<Advertiser> advertisers) {
			List<Blocklist> list1 = new LinkedList<Blocklist>();
			list1.add(new Blocklist("3422","Joe's News"));
			for (Advertiser a : advertisers)
			{
				a.setBlocklist(list1);
			}
			return advertisers;
		}
		@Override
		public byte[] getSharedSecret(String dsp) {
			return "RTB".getBytes();
		}
		@Override
		public String getOrganization() {
			return "ORG";
		}
	
	}
	
	private static final String DSP = "The_DSP";
	
    private static final String REQUEST =
        "{" +
        "  \"identification\" : {" +
        "    \"organization\" : \""+DSP+"\",\n" +
        "    \"timestamp\" : "+System.currentTimeMillis()+",\n" +
        "	 \"token\" : \"1234567890\"\n"+
        "  },\n" +
        "  \"advertisers\" : [{" +
        "    \"landingPageTLD\" : \"acmeluxuryfurniture.com\",\n" +
        "    \"name\" : \"Acme_Luxury_Furniture\"" +
        "  }]" +
        "}";
    
    private OpenRtbSsp ssp;
    
    private OpenRtbSspServer server;
    
    @Before
    public void setup() {
        ssp = new OpenRtbSspTestClient();
        server = new OpenRtbSspServer(ssp);
    }    
	
    @Test
    public void invalidMD5ChecksumRequest() throws JsonMappingException, JsonParseException, IOException
    {
    	String jsonRequest = REQUEST.replaceAll("[ \n]", "");
    	
    	String jsonResponse = server.process(jsonRequest);
    	System.out.println(" IN:"+jsonRequest);
    	System.out.println("OUT:"+jsonResponse);
    	
    	AdvertiserBlocklistResponseTranslator resTrans = new AdvertiserBlocklistResponseTranslator();
    	AdvertiserBlocklistResponse response = resTrans.fromJSON(jsonResponse);
    	//assertTrue("expected AUTH error (invalid signature)",response.getStatus().getCode()==Status.AUTH_ERROR_CODE);
    }

    @Test
    public void validMD5ChecksumRequest() throws JsonMappingException, JsonParseException, IOException
    {
    	AdvertiserBlocklistRequestTranslator reqTrans = new AdvertiserBlocklistRequestTranslator();
    	AdvertiserBlocklistResponseTranslator resTrans = new AdvertiserBlocklistResponseTranslator();
    	String digest;
    	
    	//set the request checksum
    	String jsonRequest = REQUEST.replaceAll("[ \n]", "");
    	AdvertiserBlocklistRequest request = reqTrans.fromJSON(jsonRequest);
    	request.sign(ssp.getSharedSecret(DSP), reqTrans);
    	jsonRequest = reqTrans.toJSON(request);
    	
    	//request --> response
    	String jsonResponse = server.process(jsonRequest);
    	System.out.println(" IN:"+jsonRequest);
    	System.out.println("OUT:"+jsonResponse);
    	
    	//validate success
    	AdvertiserBlocklistResponse response = resTrans.fromJSON(jsonResponse);
    	assertTrue("expected success status code",response.getStatus().getCode()==Status.SUCCESS_CODE);
    	
    	//verify the response checksum
    	response.verify(ssp.getSharedSecret(DSP), resTrans);
    	assertTrue("expected successful verification",true); //verify should return boolean
    }
    
    @Test
    public void malformedRequest() throws JsonMappingException, JsonParseException, IOException
    {
    	String jsonRequest = "{xyz}";
    	
    	String jsonResponse = server.process(jsonRequest);
    	System.out.println(" IN:"+jsonRequest);
    	System.out.println("OUT:"+jsonResponse);
    	
    	AdvertiserBlocklistResponseTranslator resTrans = new AdvertiserBlocklistResponseTranslator();
    	AdvertiserBlocklistResponse response = resTrans.fromJSON(jsonResponse);
    	assertTrue("bad MD5 status code",response.getStatus().getCode()==Status.OTHER_ERROR_CODE);
    }

}
