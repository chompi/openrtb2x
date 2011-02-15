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
package org.openrtb.ssp.client;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.openrtb.common.model.Advertiser;
import org.openrtb.common.model.Blocklist;
import org.openrtb.ssp.SupplySideService;

/**
 * A sample reference implementation in order to demonstrate
 * the role of SSP implementor.   
 *
 * @since 1.0.1
 */
public class SupplySideServiceRefImpl implements SupplySideService {

	private Map<String,List<Blocklist>> blocklistDB = new HashMap<String,List<Blocklist>>();
	private String secret = "RTB";
	private String org = "The SSP";
	
	public SupplySideServiceRefImpl() {
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
	public Collection<Advertiser> setBlocklists(Collection<Advertiser> advertisers) {
		
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
