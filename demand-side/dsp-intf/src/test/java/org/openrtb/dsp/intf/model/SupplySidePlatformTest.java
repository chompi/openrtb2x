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

import org.junit.Test;

/**
 * Validating the supply-side platform object is correctly constructed; We
 * require and enforce an organization identifier, a service url, and a shared
 * secret are supplied.
 */
public class SupplySidePlatformTest {

    private static final String ORGANIZATION = "an organization identifier";
    private static final String SERVICE_URL = "http://blah.blah.com/foo/bar";
    private static final byte[] SECRET = "can't guess me".getBytes();

    @Test(expected = IllegalArgumentException.class)
    public void createSsp_noOrganization() {
        new SupplySidePlatform(null, SERVICE_URL, SECRET);
    }
    @Test(expected = IllegalArgumentException.class)
    public void createSsp_noService() {
        new SupplySidePlatform(ORGANIZATION, null, SECRET);
    }
    @Test(expected = IllegalArgumentException.class)
    public void createSsp_noSecret() {
        new SupplySidePlatform(ORGANIZATION, SERVICE_URL, null);
    }

}
