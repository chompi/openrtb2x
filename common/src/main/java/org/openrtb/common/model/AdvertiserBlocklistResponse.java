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

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

@JsonSerialize(include = Inclusion.NON_DEFAULT)
@JsonPropertyOrder({"identification", "status", "advertisers"})
public class AdvertiserBlocklistResponse extends Signable{

    @JsonProperty
    private Identification identification;

    @JsonProperty
    private Status status;

    @JsonProperty
    private List<Advertiser> advertisers;

    public AdvertiserBlocklistResponse() {
        setAdvertisers(null);
    }

    public AdvertiserBlocklistResponse(Identification identification, Status status) {
        setIdentification(identification);
        setStatus(status);
    }

    @Override
    public Identification getIdentification() {
        return identification;
    }
    public void setIdentification(Identification identification) {
        this.identification = identification;
    }

    public Status getStatus() {
        return status;
    }
    public void setStatus(Status status) {
        this.status = status;
    }

    /**
     * There has to be at least one advertiser in the
     * {@link AdvertiserBlocklistRequest} for that to be valid; as a result, the
     * response will have at least one {@link Advertiser} in the object to be
     * valid.
     *
     * @return
     */
    public List<Advertiser> getAdvertisers() {
        return advertisers;
    }
    public void setAdvertisers(Collection<Advertiser> advertisers) {
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
            throw new IllegalArgumentException("Advetiser passed to AdvertiserBlocklistResponse#addAdvetiser() must be non-null");
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
