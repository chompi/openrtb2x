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
package org.openrtb.common.model;

import java.util.LinkedList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

/**
 * This is the request made from the DSP to an SSP to retrieve the list of
 * Publishers with associated Sites (if any) that the advertiser is blocked on.
 *
 * The expected return value for an <code>AdvertiserBlocklistRequest</code> is
 * an {@link AdvertiserBlocklistResponse}.
 *
 * @since 1.0
 */
@JsonSerialize(include=Inclusion.NON_DEFAULT)
@JsonPropertyOrder({"identification", "advertisers"})
public class AdvertiserBlocklistRequest {

    @JsonProperty private Identification identification;
    @JsonProperty private List<Advertiser> advertisers;

    /**
     * Needed for JSON serialization/deserialization.
     */
    protected AdvertiserBlocklistRequest() {
        this((Identification)null);
    }

    /**
     * Creates a minimal <code>AdvertiserBlocklistRequest</code>.
     *
     * @param organization
     *            The unique name of the organization making the request. This
     *            value represents the unique key between the SSP and the
     *            requestor for identifying who sent the request.
     * @throws IllegalArgumentException
     *             this exception is thrown if <code>organization</code> is
     *             <code>null</code>.
     */
    public AdvertiserBlocklistRequest(String organization) {
        setIdentification(new Identification(organization));
    }

    public AdvertiserBlocklistRequest(Identification identification) {
        this(identification, null);
    }

    public AdvertiserBlocklistRequest(Identification identification, List<Advertiser> advertisers) {
        setIdentification(identification);
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
     * The list of associated {@link Advertiser} objects to retrieve
     * {@link Blocklist}s for.
     *
     * There must be at least <b>one</b> {@link Advertiser} in this list for the
     * request to be valid.
     */
    public List<Advertiser> getAdvertisers() {
        return advertisers;
    }

    public void setAdvertisers(List<Advertiser> advertisers) {
        initializeAdvertisers();
        if (advertisers == null) {
            this.advertisers.clear();
        } else {
            this.advertisers.addAll(advertisers);
        }
    }

    /**
     * @param advertiser
     *            non-null {@link Advertiser} to add to the request.
     * @throws IllegalArgumentException
     *             should <code>advertiser</code> be <code>null</code>.
     */
    public void addAdvertiser(Advertiser advertiser) {
        if (advertiser == null) {
            throw new IllegalArgumentException("Advetiser passed to AdvertiserBlocklistRequest#addAdvetiser() must be non-null");
        }

        initializeAdvertisers();
        advertisers.add(advertiser);
    }

    private void initializeAdvertisers() {
        if (advertisers == null) {
            advertisers = new LinkedList<Advertiser>();
        }
    }

}
