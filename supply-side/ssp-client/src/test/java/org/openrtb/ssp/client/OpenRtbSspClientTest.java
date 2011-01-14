package org.openrtb.ssp.client;

import static org.junit.Assert.assertTrue;

import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.openrtb.common.model.Advertiser;
import org.openrtb.common.model.Blocklist;
import org.openrtb.ssp.OpenRtbSsp;

public class OpenRtbSspClientTest {
	
	private OpenRtbSsp ssp;

    @Before
    public void setup() {
        ssp = new OpenRtbSspClient();
    }

    @Test
    public void blocklistGetsSet()
    {
    	List<Blocklist> bls = null;
     	Advertiser a  = new Advertiser("acmeluxuryfurniture.com","ACME Luxury Furniture");
    	bls = a.getBlocklist();
    	assertTrue("Blocklist is initialy empty",bls.size()==0);
    	
       	List<Advertiser> advertisers = new LinkedList<Advertiser>();
       	advertisers.add(a);
    	ssp.setBlocklists(advertisers);
    	assertTrue("Blocklist has been set",bls.size()!=0);
    	//System.out.println("SIZE="+bls.size());
    }
}
