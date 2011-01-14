package org.openrtb.ssp;

import java.util.List;

import org.openrtb.common.model.Advertiser;

public interface OpenRtbSsp {
	List<Advertiser> setBlocklists(List<Advertiser> advertisers);
	byte[] getSharedSecret(String dsp);
	String getOrganization();
}
