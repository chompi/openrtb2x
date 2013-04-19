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

package org.openrtb.dsp.intf.model;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;


@JsonSerialize(include=Inclusion.NON_DEFAULT)
@JsonPropertyOrder({"orgname", "rtbUrl", "rtbCtype"})
public class RTBExchange  {
    @JsonProperty("orgname")
	private String orgName;
    @JsonProperty("rtbUrl")
	private String rtbServiceUrl;
    @JsonProperty("rtbCtype")
	private String rtbContentType;
	
    public RTBExchange() {
    }
    
	public RTBExchange(String orgName, String rtbServiceUrl, String rtbContentType) {
		this.orgName = orgName;
		this.rtbServiceUrl = rtbServiceUrl;
		this.rtbContentType = rtbContentType;
	}
	
	public RTBExchange(RTBExchange copy) {
		this.orgName = copy.getOrgName();
		this.rtbContentType = copy.getRtbContentType();
		this.rtbServiceUrl = copy.getRtbServiceUrl();
	}

    @JsonProperty("orgname")
	public String getOrgName() {
		return orgName;
	}
	
	protected void setOrgName(String orgname) {
		this.orgName = orgname;
	}

    @JsonProperty("rtbUrl")
	public String getRtbServiceUrl() {
		return rtbServiceUrl;
	}
	
    protected void setRtbServiceUrl(String rtbUrl) {
		this.rtbServiceUrl = rtbUrl;
	}

    @JsonProperty("rtbCtype")
	public String getRtbContentType() {
		return rtbContentType;
	}
	
    protected void setRtbContentType(String rtbCtype) {
		this.rtbContentType = rtbCtype;
	}	
}
