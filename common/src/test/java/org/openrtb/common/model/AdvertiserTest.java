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

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Validation tests for {@link Advertiser}'s method behavior.
 */
public class AdvertiserTest {

    private Advertiser test;

    @Before
    public void setup() {
        test = new Advertiser();
    }

    /**
     * Make sure object is capable of stripping off protocol, subdomains, etc.
     * to leave only the top-level domain.
     */
    @Test @Ignore
    public void formatLandingPage() {
        String rawUrl = "https://Sports.Publisher.COM/NFL";
        String expectedUrl = "publisher.com";

        test = new Advertiser(rawUrl);
        assertEquals("url is not properly formatted by advertiser object",
                     expectedUrl, test.getLandingPage());
    }

    /**
     * Confirmation that all blocklist methods work correctly with null object
     * values.
     */
    @Test
    public void blocklistReturnsNonNull() {
        assertNotNull("blocklist should not be null on object creation",
                      test.getBlocklist());
    }

    @Test
    public void cannotSetBlocklistToNull() {
        test.setBlocklist(null);
        assertNotNull("blocklist should not be null when explicitly set as such",
                      test.getBlocklist());
    }

    @Test(expected = IllegalArgumentException.class)
    public void invalidBlocklistAddedAdvertiser() {
        test.addBlocklist(null);
    }

}
