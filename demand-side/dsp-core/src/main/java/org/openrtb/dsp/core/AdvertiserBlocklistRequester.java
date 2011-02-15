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

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
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
import org.openrtb.dsp.intf.model.SupplySidePlatform;
import org.openrtb.dsp.intf.service.AdvertiserService;
import org.openrtb.dsp.intf.service.IdentificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * There are multiple ways to request {@link Blocklist}s from SSPs. This class
 * currently supports the following mechanisms of requesting {@link Advertiser}
 * {@link Blocklist}:
 * 
 * <ul>
 * <li>Complete Blocklist Refresh<br/>
 * For all SSPs configured to provide blocklists to the DSP, a list of
 * Advertisers will be sent to the SSP based upon the DSP's implementation of
 * the {@link AdvertiserService#getAdvertiserList()} implementation. For more
 * information, please refer to the {@link #requestAllBlocklists()} method.</li>
 * </ul>
 * 
 * @since 1.0
 */
public class AdvertiserBlocklistRequester {

    public static final String SPRING_NAME = "dsp.core.AdvertiserBlocklistRequester";

    private static final Logger logger = LoggerFactory.getLogger(AdvertiserBlocklistRequester.class);

    private static final AdvertiserBlocklistRequestTranslator REQUEST_TRANSFORM;
    private static final AdvertiserBlocklistResponseTranslator RESPONSE_TRANSFORM;
    static {
        REQUEST_TRANSFORM = new AdvertiserBlocklistRequestTranslator();
        RESPONSE_TRANSFORM = new AdvertiserBlocklistResponseTranslator();
    }

    private AdvertiserService advertiserService;
    private IdentificationService identificationService;

    public AdvertiserBlocklistRequester(AdvertiserService advertiserService,
                                        IdentificationService identificationService) {
        this.advertiserService = advertiserService;
        this.identificationService = identificationService;
    }

    /**
     * Perform a complete refresh for all {@link Advertiser} {@link Blocklist}
     * for the available {@link SupplySidePlatform}s. This action is intended to
     * delete any/all data that was previously retrieved for the requested
     * {@link Advertiser}s.
     * 
     * @throws BlocklistException
     *             if anything erroneous happens in this method, a
     *             BlocklistException is thrown indicating what the functional
     *             root cause of the issue was and the associated original
     *             exception.
     */
    public void requestAllBlocklists() throws BlocklistException {
        Collection<Advertiser> advertisers = advertiserService.getAdvertiserList();
        if (advertisers == null || advertisers.isEmpty()) {
            logger.info("Unable to sync blocklists with supply-side platforms; no advertisers returned from AdvertiserService#getAdvertiserList().");
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
                logger.error("Unable to sign json request due to exception", e);
                throw new BlocklistException("Unable to sign json request due to exception", e);
            }

            try {
                response = makeRequest(ssp, REQUEST_TRANSFORM.toJSON(request));
                if (response != null) {
                    if (response.verify(ssp.getSharedSecret(), RESPONSE_TRANSFORM)) {
                        advertiserService.replaceBlocklists(ssp, response.getAdvertisers());
                    } else {
                        logger.error("Verification of response from ["+ssp.getOrganization()+"] failed");
                    }
                }
            } catch (IOException e) {
                logger.error("Unable to verify json response due to exception", e);
                // TODO: we need to handle/log these things...
            }
        }
    }

    /**
     * @param ssp
     * @param request
     * @return
     */
    AdvertiserBlocklistResponse makeRequest(SupplySidePlatform ssp, String request) {

        if (logger.isDebugEnabled()) {
            logger.debug("Organization Name ["+ssp.getOrganization()+"]");
            logger.debug("Organization Endpoint ["+ssp.getBatchServiceUrl()+"]");
            logger.debug("Organization Secret ["+new String(ssp.getSharedSecret())+"]");
            logger.debug("Organization Request: " + request);
        }

        HttpClient client = new HttpClient();
        PostMethod post = new PostMethod(ssp.getBatchServiceUrl());
        try {
            post.setRequestEntity(new StringRequestEntity(request, "application/json", null));
        } catch (UnsupportedEncodingException e) {
            logger.error("Unable to set the request's encoding type: application/json", e);
            // TODO: return something that doesn't break
        }

        AdvertiserBlocklistResponse response = null;
        try {
            int statusCode = client.executeMethod(post);
            if (statusCode != HttpStatus.SC_OK) {
                logger.error("Request for blocklists failed w/ code ["+statusCode+"] " +
                             "for supply-side platform ["+ssp.getOrganization()+"] " +
                             "w/ url ["+ssp.getBatchServiceUrl()+"]");
                return null;
            }
            response = RESPONSE_TRANSFORM.fromJSON(new InputStreamReader(post.getResponseBodyAsStream()));
            if (logger.isDebugEnabled()) {
                System.out.println();
                File f = new File("/Users/cpate/output.txt");
                FileWriter write = new FileWriter(f);
                write.write(RESPONSE_TRANSFORM.toJSON(response));
                write.flush();
                write.close();
                logger.debug("Organization Response: " + RESPONSE_TRANSFORM.toJSON(response));
            }
        } catch (HttpException e) {
            // TODO: Handle the exceptions...
            e.printStackTrace();
        } catch (IOException e) {
            // TODO: Handle the exceptions...
            e.printStackTrace();
        } finally {
            post.releaseConnection();
            if (response == null) {
                logger.error("An error occurred while processing response from supply-side platform ["+ssp.getOrganization()+"]");
            }
        }

        return response;
    }
}
