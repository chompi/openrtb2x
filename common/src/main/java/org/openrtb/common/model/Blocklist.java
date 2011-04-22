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

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

/**
 * This object represents information that a particular {@link Advertiser}
 * instance is blocked on.
 *
 * The {@link #publisherId} and {@link #siteId} values are unique to the
 * specific supply-side platform that provides them and should be considered the
 * key value when communication exists between the DSPs and SSPs regarding
 * publishers.
 *
 * @since 1.0
 */
@JsonSerialize(include=Inclusion.NON_NULL)
@JsonIgnoreProperties({"publisherId", "siteId"})
@JsonPropertyOrder({"publisherID", "publisherName", "siteID", "siteName"})
public class Blocklist {

    @JsonProperty("publisherID")
    private String publisherId;
    @JsonProperty
    private String publisherName;
    @JsonProperty("siteID")
    private String siteId;
    @JsonProperty
    private String siteName;

    public Blocklist() {}

    public Blocklist(String publisherId, String publisherName) {
        this(publisherId, publisherName, null, null);
    }

    public Blocklist(String publisherId, String publisherName, String siteId, String siteName) {
        setPublisherId(publisherId);
        setPublisherName(publisherName);
        setSiteId(siteId);
        setSiteName(siteName);
    }

    /**
     * SSP's unique identifier for the publisher.
     *
     * There are no restrictions imposed on the content of this value beyond the
     * specified datatype (i.e. the value may be alphanumeric).
     */
    public String getPublisherId() {
        return publisherId;
    }

    public void setPublisherId(String publisherId) {
        this.publisherId = publisherId;
    }

    /**
     * Human readable name of the publisher.
     *
     * This field is used for informational purposes only and is not expected to
     * be unique.
     */
    public String getPublisherName() {
        return publisherName;
    }

    public void setPublisherName(String publisherName) {
        this.publisherName = publisherName;
    }

    /**
     * If blocking is at the site level, then specify the SSP's unique site
     * identifier.
     *
     * There are no restrictions imposed on the content of this value beyond the
     * specified datatype (i.e. the value may be alphanumeric).
     *
     * If this field is blank or <code>null</code>, then all sites for the
     * corresponding {@link #publisherId} are blocked.
     */
    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    /**
     * Human readable name of the site ID, if site level blocking is being used.
     *
     * This field is used for informational purposes only and is not expected to
     * be unique.
     */
    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

}
