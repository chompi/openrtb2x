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
 * This is the response made from the SSP to the DSP to retrieve the list of
 * Publishers with associated Sites (if any) that the advertiser is blocked on.
 * 
 * @since 1.0
 */
@JsonSerialize(include = Inclusion.NON_DEFAULT)
@JsonPropertyOrder( { "identification", "status", "advertiser" })
public class AdvertiserBlocklistResponse {

	@JsonProperty("identification")
	private Identification identification;

	@JsonProperty("status")
	private Status status;

	@JsonProperty("advertiser")
	private List<AdvertiserBlockList> advertisers;

	/**
	 * Needed for JSON serialization/deserialization.
	 */
	protected AdvertiserBlocklistResponse() {
		this(null, null, null);
	}

	public AdvertiserBlocklistResponse(Identification identification,
			Status status, List<AdvertiserBlockList> advertisers) {
		setIdentification(identification);
		setStatus(status);
		setAdvertisers(advertisers);
	}

	/**
	 * {@link Identification} of who is making this request.
	 * 
	 * This attribute is required.
	 */
	public Identification getIdentification() {
		return identification;
	}

	public void setIdentification(Identification identification) {
		this.identification = identification;
	}

	/**
	 * {@link Status} status of this response.
	 * 
	 * This attribute is required.
	 */
	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	/**
	 * The list of associated {@link AdvertiserBlockList} objects to retrieve
	 * {@link AdvertiserBlockList}s for.
	 * 
	 * There must be at least <b>one</b> {@link AdvertiserBlockList} in this
	 * list for the request to be valid.
	 */
	public List<AdvertiserBlockList> getAdvertisers() {
		return advertisers;
	}

	public void setAdvertisers(List<AdvertiserBlockList> advertisers) {
		initializeAdvertisers();
		if (advertisers == null) {
			this.advertisers.clear();
		} else {
			this.advertisers.addAll(advertisers);
		}
	}

	/**
	 * @param advertiserBlockList
	 *            non-null {@link AdvertiserBlockList} to add to the request.
	 * @throws IllegalArgumentException
	 *             should <code>advertiserBlockList</code> be <code>null</code>.
	 */
	public void addAdvertiserBlockList(AdvertiserBlockList advertiserBlockList) {
		if (advertiserBlockList == null) {
			throw new IllegalArgumentException(
					"AdvertiserBlockList passed to AdvertiserBlocklistRequest#addAdvertiserBlockList() must be non-null");
		}

		initializeAdvertisers();
		advertisers.add(advertiserBlockList);
	}

	private void initializeAdvertisers() {
		if (advertisers == null) {
			advertisers = new LinkedList<AdvertiserBlockList>();
		}
	}

}
