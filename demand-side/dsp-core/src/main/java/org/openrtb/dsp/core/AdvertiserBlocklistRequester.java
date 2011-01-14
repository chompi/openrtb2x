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
package org.openrtb.dsp.core;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.openrtb.common.json.AdvertiserBlocklistRequestTranslator;
import org.openrtb.common.json.AdvertiserBlocklistResponseTranslator;
import org.openrtb.common.model.Advertiser;
import org.openrtb.common.model.AdvertiserBlocklistRequest;
import org.openrtb.common.model.AdvertiserBlocklistResponse;
import org.openrtb.common.model.Blocklist;
import org.openrtb.common.model.Identification;
import org.openrtb.common.util.MD5Checksum;
import org.openrtb.dsp.intf.model.Exchange;
import org.openrtb.dsp.intf.service.AdvertiserService;
import org.openrtb.dsp.intf.service.IdentificationService;

/**
 * There are multiple ways to request {@link Blocklist}s from SSPs. This class
 * currently supports the following mechanisms of requesting {@link Advertiser}
 * {@link Blocklist}:
 * 
 * <ul>
 * <li>For all SSPs<br/>
 * For all SSP configured to integrate with DSP, {@link Blocklist}s will be
 * requested for all {@link Advertiser}s in the DSP, regardless of whether or
 * not they are targeting the exchange. For these situations, please refer to
 * {@link #requestAllBlocklists()}.
 * </li>
 * <li>For targeted SSPs<br/>
 * For any number of reasons, it may be more viable to only request
 * {@link Blocklist}s for {@link Advertiser}s that target a specific SSP. For
 * these situations, use the {@link #requestTargetedBlocklists()}.
 * </li>
 * </ul>
 * 
 * @since 1.0
 */
public class AdvertiserBlocklistRequester {

    private static final AdvertiserBlocklistRequestTranslator REQUEST_TRANSFORM;
    private static final AdvertiserBlocklistResponseTranslator RESPONSE_TRANSFORM;
    static {
        REQUEST_TRANSFORM = new AdvertiserBlocklistRequestTranslator();
        RESPONSE_TRANSFORM = new AdvertiserBlocklistResponseTranslator();
    }

    AdvertiserService advertiserService;
    IdentificationService identificationService;

    /**
     * For all of the SSPs configured to use the DSP, request <b>complete</b>
     * {@link Blocklist}s for each {@link Advertiser}.
     */
    public void requestAllBlocklists() {
        List<Advertiser> advertisers = advertiserService.getAllAdvertisers();
        if (advertisers.size() == 0) {
            // TODO: we need to add logging...
            return;
        }

        String organization = identificationService.getOrganizationIdentifier();
        Identification dsp = new Identification(organization);
        AdvertiserBlocklistRequest request = new AdvertiserBlocklistRequest(dsp, advertisers);

        for(Exchange ssp : identificationService.getExchanges()) {
            AdvertiserBlocklistResponse response = null;
            try {
                MD5Checksum.signRequest(ssp.getSharedSecret(), request, REQUEST_TRANSFORM);
                response = makeRequest(ssp, REQUEST_TRANSFORM.toJSON(request));
            } catch (IOException e) {
                // TODO: we need to handle/log these things...
                e.printStackTrace();
                throw new RuntimeException(e);
            }
            
            advertiserService.replaceAdvertiserBlocklists(response.getAdvertisers());
        }
    }
    
    /**
     * 
     */
    public void requestTargetedBlocklists() {
        
    }

    /**
     * @param ssp
     * @param request
     * @return
     */
    AdvertiserBlocklistResponse makeRequest(Exchange ssp, String request) {
        HttpClient client = new HttpClient();
        
        PostMethod post = new PostMethod(ssp.getBatchServiceUrl());
        try {
            post.setRequestEntity(new StringRequestEntity(request, null, null));
        } catch (UnsupportedEncodingException e) {
            // TODO: Handle exceptions correctly...
            e.printStackTrace();
        }
        
        try {
            int statusCode = client.executeMethod(post);
            if (statusCode != HttpStatus.SC_OK) {
                return null;
            }
        } catch (HttpException e) {
            // TODO: Handle the exceptions...
            e.printStackTrace();
        } catch (IOException e) {
            // TODO: Handle the exceptions...
            e.printStackTrace();
        } finally {
            post.releaseConnection();
        }
        
        try {
            return RESPONSE_TRANSFORM.fromJSON(new InputStreamReader(post.getResponseBodyAsStream()));
        } catch (IOException e) {
            // TODO: Handle the exception...
            e.printStackTrace();
        }
        return null;
    }
}
