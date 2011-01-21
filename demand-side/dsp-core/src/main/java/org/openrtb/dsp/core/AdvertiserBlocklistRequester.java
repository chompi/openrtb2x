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
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openrtb.common.json.AdvertiserBlocklistRequestTranslator;
import org.openrtb.common.json.AdvertiserBlocklistResponseTranslator;
import org.openrtb.common.model.Advertiser;
import org.openrtb.common.model.AdvertiserBlocklistRequest;
import org.openrtb.common.model.AdvertiserBlocklistResponse;
import org.openrtb.common.model.Blocklist;
import org.openrtb.common.model.Identification;
import org.openrtb.dsp.intf.model.SupplySidePlatform;
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

    public static final String SPRING_NAME = "dsp.core.AdvertiserBlocklistRequester";

    private static final AdvertiserBlocklistRequestTranslator REQUEST_TRANSFORM;
    private static final AdvertiserBlocklistResponseTranslator RESPONSE_TRANSFORM;
    static {
        REQUEST_TRANSFORM = new AdvertiserBlocklistRequestTranslator();
        RESPONSE_TRANSFORM = new AdvertiserBlocklistResponseTranslator();
    }
    private static final Log log = LogFactory.getLog(AdvertiserBlocklistRequester.class);

    private AdvertiserService advertiserService;
    private IdentificationService identificationService;

    public AdvertiserBlocklistRequester(AdvertiserService advertiserService,
                                        IdentificationService identificationService) {
        this.advertiserService = advertiserService;
        this.identificationService = identificationService;
    }

    /**
     * Perform a complete refresh for all {@link Advertiser} {@link Blocklist}
     * for the available {@link SupplySidePlatform}s. This action is intended to delete
     * any/all data that was previously retrieved for the requested
     * {@link Advertiser}s.
     */
    public void requestAllBlocklists() {
        List<Advertiser> advertisers = advertiserService.getAdvertiserList();
        if (advertisers.size() == 0) {
            log.info("Unable to sync blocklists with supply-side platforms; no advertisers returned from AdvertiserService#getAdvertiserList().");
            return;
        }

        String organization = identificationService.getOrganizationIdentifier();
        Identification dsp = new Identification(organization);
        AdvertiserBlocklistRequest request = new AdvertiserBlocklistRequest(dsp, advertisers);

        for(SupplySidePlatform ssp : identificationService.getServiceEndpoints()) {
            AdvertiserBlocklistResponse response = null;
            try {
                request.sign(ssp.getSharedSecret(), REQUEST_TRANSFORM);
            } catch (IOException e) {
                log.error("unable to sign json request due to exception", e);
                // TODO: need to pass message back to caller...
            }

            try {
                response = makeRequest(ssp, REQUEST_TRANSFORM.toJSON(request));
                if (response != null) {
                    response.verify(ssp.getSharedSecret(), RESPONSE_TRANSFORM);
                    advertiserService.replaceAdvertiserBlocklists(response.getAdvertisers());
                }
            } catch (IOException e) {
                // TODO: we need to handle/log these things...
                e.printStackTrace();
                throw new RuntimeException(e);
            }
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
    AdvertiserBlocklistResponse makeRequest(SupplySidePlatform ssp, String request) {
        HttpClient client = new HttpClient();

        PostMethod post = new PostMethod(ssp.getBatchServiceUrl());
        try {
            post.setRequestEntity(new StringRequestEntity(request, null, null));
        } catch (UnsupportedEncodingException e) {
            // TODO: Handle exceptions correctly...
            e.printStackTrace();
        }

        AdvertiserBlocklistResponse response = null;
        try {
            int statusCode = client.executeMethod(post);
            if (statusCode != HttpStatus.SC_OK) {
                log.error("blocklist request failed w/ code ["+statusCode+"] " +
                          "for supply-side platform ["+ssp.getOrganization()+"] " +
                          "w/ url ["+ssp.getBatchServiceUrl()+"]");
                return null;
            }
            response = RESPONSE_TRANSFORM.fromJSON(new InputStreamReader(post.getResponseBodyAsStream()));
        } catch (HttpException e) {
            // TODO: Handle the exceptions...
            e.printStackTrace();
        } catch (IOException e) {
            // TODO: Handle the exceptions...
            e.printStackTrace();
        } finally {
            post.releaseConnection();
            if (response == null) {
                log.error("an error occurred while processing response from supply-side platform ["+ssp.getOrganization()+"]");
            }
        }

        return response;
    }
}
