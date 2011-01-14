package org.openrtb.ssp.client;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.openrtb.common.model.Advertiser;
import org.openrtb.common.model.Blocklist;
import org.openrtb.ssp.OpenRtbSsp;

public class OpenRtbSspClient implements OpenRtbSsp {

	private Map<String,List<Blocklist>> blocklistDB = new HashMap<String,List<Blocklist>>();
	private String secret = "RTB";
	private String org = "The SSP";
	
	public OpenRtbSspClient() {
		List<Blocklist> list1 = new LinkedList<Blocklist>();
		list1.add(new Blocklist("3422","Joe's News"));
		list1.add(new Blocklist("2342","Big Portal","1","Finance section"));
		list1.add(new Blocklist("23423","Smith Blog","223","Technology Section"));
		list1.add(new Blocklist("423","Smith Blog","23","Cars Section"));
		list1.add(new Blocklist("34223","Jones Blog"));
		blocklistDB.put("acmeluxuryfurniture.com", list1);
		
		List<Blocklist> list2 = new LinkedList<Blocklist>();
		list2.add(new Blocklist("34223","Joe's Blog"));
		blocklistDB.put("luxurycarbrand.com", list2);
	}
	
	@Override
	public List<Advertiser> setBlocklists(List<Advertiser> advertisers) {
		
		for (Advertiser a : advertisers)
		{
			String url = a.getLandingPage();
			a.setBlocklist(blocklistDB.get(url));
		}
		return advertisers;
	}

	@Override
	public byte[] getSharedSecret(String dsp) {
		return secret.getBytes();
	}

	@Override
	public String getOrganization() {
		return org;
	}

}
