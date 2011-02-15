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

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.openrtb.common.model.Advertiser;
import org.openrtb.common.model.AdvertiserBlocklistResponse;
import org.openrtb.dsp.intf.model.SupplySidePlatform;
import org.openrtb.dsp.intf.service.AdvertiserService;
import org.openrtb.dsp.intf.service.IdentificationService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class AdvertiserBlocklistRequesterTest {

    private static ApplicationContext ctx;

    @BeforeClass
    public static void beforeTests() {
        ctx = new ClassPathXmlApplicationContext(new String[] {"norequest-core.xml",
                                                               "dsp-client.xml"});
    }


    private AdvertiserBlocklistRequester test;

    @Before
    public void setup() {
        test = (AdvertiserBlocklistRequester)ctx.getBean(AdvertiserBlocklistRequester.SPRING_NAME);
    }

    @After
    public void teardown() {
        ((AdvertiserBlocklistNeverRequest)ctx.getBean(AdvertiserBlocklistRequester.SPRING_NAME))
                                             .reset();
    }


    @Test
    public void requestAllBlocklists_noAdvertisers() throws Exception {
        AdvertiserService aService = mock(AdvertiserService.class);
        when(aService.getAdvertiserList()).thenReturn(Collections.<Advertiser>emptyList());
        AdvertiserBlocklistRequester test = new AdvertiserBlocklistRequester(aService, null);

        test.requestAllBlocklists();
        verify(aService).getAdvertiserList();
        verify(aService, never()).replaceBlocklists(null, null);
    }

    @Test
    public void requestAllBlocklists_noMatch() throws Exception {
        Advertiser advertiser = new Advertiser("a-cool-advertiser.com");
        AdvertiserService aService = mock(AdvertiserService.class);
        when(aService.getAdvertiserList()).thenReturn(Collections.<Advertiser>singletonList(advertiser));

        SupplySidePlatform ssp = new SupplySidePlatform("supply-side-platform-organization", "supply.platform.com", "our shared secret".getBytes());
        IdentificationService iService = mock(IdentificationService.class);
        when(iService.getOrganizationIdentifier()).thenReturn("organization-identifier");
        when(iService.getServiceEndpoints()).thenReturn(Collections.<SupplySidePlatform>singletonList(ssp));

        AdvertiserBlocklistRequester test = new AdvertiserBlocklistRequester(aService, iService) {
            @Override
            AdvertiserBlocklistResponse makeRequest(SupplySidePlatform ssp, String request) {
                return null;
            }
        };

        test.requestAllBlocklists();
        verify(aService).getAdvertiserList();
        verify(aService, never()).replaceBlocklists(null, null);
    }

    @Test @Ignore
    public void requestAllBlocklists_integration() throws Exception {
        ApplicationContext ictx = new ClassPathXmlApplicationContext(new String[] {"dsp-core.xml",
                                                                                   "dsp-client.xml"});
        test = (AdvertiserBlocklistRequester)ictx.getBean(AdvertiserBlocklistRequester.SPRING_NAME);
        test.requestAllBlocklists();
    }
}
