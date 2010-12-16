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

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 * Verification of the message content is handled inline with each request.
 * Every request contains an identification object. The identifier's token is
 * constructed by computing the MD5 checksum of the JSON request message, less
 * the token attribute, concatenated with a shared secret. The receiver of the
 * request must remove the token from the request and generate an MD5 checksum
 * with the same shared secret to confirm the message contents.
 *
 * The shared secret may take any form (i.e. text string or key encryption) and
 * is communicated between the parties outside of this protocol.
 *
 * This object encapsulates the <code>identification</code> object found in the
 * requests and responses between a DSP and SSP.
 */
@JsonSerialize
@JsonPropertyOrder({"organization", "timestamp", "token"})
public class Identification {

    private String organization;
    private long timestamp;
    private String token;

    @JsonCreator
    public Identification(@JsonProperty("organization") String organization,
                          @JsonProperty("timestamp") long timestamp,
                          @JsonProperty("token") String token) {
        this.organization = organization;
        this.timestamp = timestamp;
        this.token = token;
    }

    /**
     * The identifier used by the receiver to identify the requesting
     * organization.
     */
    public String getOrganization() {
        return organization;
    }

    /**
     * The number of milliseconds since EPOC this request was made.
     */
    public long getTimestamp() {
        return timestamp;
    }

    /**
     * The associated MD5 token used to confirm the authenticity of and uniquely
     * identify the request.
     */
    public String getToken() {
        return token;
    }

}
