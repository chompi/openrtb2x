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

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.GregorianCalendar;

import org.junit.Before;
import org.junit.Test;

/**
 * Validation tests for {@link Identification}'s method behavior.
 */
public class IdentificationTest {

    public static final String ORGANIZATION = "Organization_Name";
    private Identification test;

    @Before
    public void setup() {
        test = new Identification(ORGANIZATION);
    }

    /**
     * Verify <code>null</code> and empty organization identification values.
     */
    @Test(expected = IllegalArgumentException.class)
    public void cannotInitializeNullOrganization() {
        new Identification(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void cannotInitializeEmptyOrganization() {
        new Identification("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void cannotSetNullOrganization() {
        test.setOrganization(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void cannotSetEmptyOrganization() {
        test.setOrganization("");
    }

    /**
     * The identification object should default the timestamp to the current
     * time when none is specified.
     */
    @Test
    public void validateTimestampValue() {
        GregorianCalendar calendar = new GregorianCalendar();
        assertTrue("difference between default timestamp and now is greater than 1 seconds",
                   (1L*1000) > (calendar.getTime().getTime() - test.getTimestamp()));
    }

    /**
     * While a token is required for the request to be valid, the initial value
     * of the {@link Identification#getToken()} should be null.
     */
    @Test
    public void startWithNullToken() {
        assertNull("token value should be null on newly created identificaiton object",
                   test.getToken());
    }

}
