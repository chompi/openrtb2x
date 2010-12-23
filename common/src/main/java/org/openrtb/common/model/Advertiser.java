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

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

/**
 * Represents the contents of an Advertiser object as is necessary for all
 * requests and responses.
 *
 * The {@link #landingPage} represents the unique identifier of an individual
 * advertiser for all communication between a DSP and SSP.
 *
 * @since 1.0
 */
@JsonSerialize(include=Inclusion.NON_DEFAULT)
@JsonPropertyOrder({"landingPageTLD", "name", "sinceThisTimestamp", "blocklist"})
public class Advertiser {

    @JsonProperty("landingPageTLD")
    private String landingPage;

    @JsonProperty("name")
    private String name;

    @JsonProperty("sinceThisTimestamp")
    private Long timestamp;

    @JsonProperty("blocklist")
    private List<Blocklist> blocklist;

    public Advertiser() {
        this(null, null, null, null);
    }
    public Advertiser(String landingPage) {
        this(landingPage, null);
    }
    public Advertiser(String landingPage, String name) {
        this(landingPage, name, null);
    }
    public Advertiser(String landingPage, String name, Long timestamp) {
        this(landingPage, name, timestamp, null);
    }
    public Advertiser(String landingPage, String name, Long timestamp, List<Blocklist> blocklist) {
        setLandingPage(landingPage);
        setName(name);
        setTimestamp(timestamp);
        setBlocklist(blocklist);
    }

    /**
     * The advertiser's landing page url as a top-level domain (TLD). Advertiser
     * landing pages are expected to be a generic landing page for the name
     * optional sinceThisTimestamp optional
     *
     * Example: "carbrand.com". Should be in lower case letters. This field is
     * used as a unique ID for the advertiser between the DSP and SSP.
     */
    @JsonProperty("landingPageTLD")
    public String getLandingPage() {
        return landingPage;
    }

    public void setLandingPage(String landingPage) {
        this.landingPage = landingPage;
    }

    /**
     * The advertiser's name.
     *
     * This field is used for informational purposes only and is not expected to
     * be unique.
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * If specified, the API will only return changes since this POSIX
     * timestamp.
     *
     * By default, the API will return all results if this value is set to
     * <code>null</code>.
     */
    @JsonProperty("sinceThisTimestamp")
    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long newTime) {
        this.timestamp = newTime;
    }

    /**
     * One or more {@link Blocklist} definitions that this
     * <code>Advertiser</code> is blocked from serving advertisements on.
     *
     * If this field is empty (<code>getBlocklist().empty()</code> or
     * <code>null</code>, there is no <tt>blocklist</tt> specified for this
     * advertiser.
     *
     * This method should never return <code>null</code>.
     */
    public List<Blocklist> getBlocklist() {
        return blocklist;
    }

    public void setBlocklist(List<Blocklist> blocklist) {
        initializeBlocklist();
        if (blocklist == null) {
            this.blocklist.clear();
        } else {
            this.blocklist.addAll(blocklist);
        }
    }

    public void addBlocklist(Blocklist blocklist) {
        if (blocklist == null) {
            throw new IllegalArgumentException("Blocklist passed to Advertiser#addBlocklist() must be non-null");
        }

        initializeBlocklist();
        this.blocklist.add(blocklist);
    }

    private void initializeBlocklist() {
        if (blocklist == null) {
            blocklist = new ArrayList<Blocklist>();
        }
    }
}
