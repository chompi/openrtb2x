package org.openrtb.ssp;

import java.util.List;

import org.openrtb.common.model.Advertiser;

public interface OpenRtbSsp {
	void setBlocklists(List<Advertiser> advertisers);
	String getSharedSecret();
	String getOrganization();
}
