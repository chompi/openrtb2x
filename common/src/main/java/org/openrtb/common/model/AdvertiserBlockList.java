/*
 * Copyright (c) 2010, The OpenRTB Project All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * 
 * 3. Neither the name of the OpenRTB nor the names of its contributors may be
 * used to endorse or promote products derived from this software without
 * specific prior written permission.
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
package org.openrtb.common.model;

import java.util.LinkedList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

/**
 * The response to an advertiser block list response from SSP to DSP.
 * 
 * @since 1.0
 * 
 */
@JsonSerialize(include = Inclusion.NON_NULL)
@JsonPropertyOrder( { "landingPageTLD", "blockList" })
public class AdvertiserBlockList {

	@JsonProperty("landingPageTLD")
	private String landingPage;

	@JsonProperty("blocklList")
	private List<Blocklist> blockLists;

	public AdvertiserBlockList() {
		this(null, null);
	}

	public AdvertiserBlockList(String landingPage) {
		this(landingPage, null);
	}

	public AdvertiserBlockList(String landingPage, List<Blocklist> blockList) {
		this.landingPage = landingPage;
		setBlockList(blockList);
	}

	public String getLandingPage() {
		return landingPage;
	}

	public void setLandingPage(String landingPage) {
		this.landingPage = landingPage;
	}

	public List<Blocklist> getBlockList() {
		return blockLists;
	}

	public void setBlockList(List<Blocklist> blockList) {
		initializeBlockList();
		if (blockList == null) {
			this.blockLists.clear();
		} else {
			this.blockLists.addAll(blockList);
		}
	}

	/**
	 * @param blockList
	 *            non-null {@link Blocklist} to add to the request.
	 * @throws IllegalArgumentException
	 *             should <code>blockList</code> be <code>null</code>.
	 */
	public void addBlockList(Blocklist blockList) {
		if (blockList == null) {
			throw new IllegalArgumentException(
					"Blocklist passed to AdvertiserBlockList#addBlockList() must be non-null");
		}

		initializeBlockList();
		blockLists.add(blockList);
	}

	private void initializeBlockList() {
		if (blockLists == null) {
			blockLists = new LinkedList<Blocklist>();
		}
	}

}
