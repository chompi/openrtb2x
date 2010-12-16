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
package org.openrtb.common.json;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.openrtb.common.model.Identification;

/**
 * Verifies the {@link Identification} object's translation to/from JSON.
 */
public class IdentificationJsonConverterTest {

    private static final Identification IDENT =
        new Identification("The_DSP", System.currentTimeMillis(),
                           "4a49b3cab1374931bbb0a5af11eeef43");

    private static final String PRETTY_VALUE =
        "{" +
        "  \"organization\" : \""+IDENT.getOrganization()+"\"," +
        "  \"timestamp\" : "+IDENT.getTimestamp()+"," +
        "  \"token\" : \""+IDENT.getToken()+"\"" +
        "}";
    private static final String EXPECTED_VALUE = PRETTY_VALUE.replaceAll("[ \n]", "");

    AbstractJsonTranslator<Identification> test = new IdentificationJsonTranslator();

    @Test
    public void serializeObject() throws Exception {
        assertEquals(EXPECTED_VALUE, test.toJSON(IDENT));
    }

    @Test
    public void deserializeObject() throws Exception {
        Identification value = test.fromJSON(EXPECTED_VALUE);

        assertEquals("unable to deserialize the organization value",
                     IDENT.getOrganization(), value.getOrganization());
        assertEquals("unable to deserialize the timestamp value",
                     IDENT.getTimestamp(), IDENT.getTimestamp());
        assertEquals("unable to deserialize the token value",
                     IDENT.getToken(), IDENT.getToken());
    }

    /**
     * This class is used internally for the test to verify the serialization
     * and de-serialization of the {@link Identification} object to and from
     * JSON.
     *
     * @see AbstractJsonTranslator
     */
    private static class IdentificationJsonTranslator extends AbstractJsonTranslator<Identification> {
        public IdentificationJsonTranslator() {
            super(IdentificationJsonTranslator.class);
        }
    }
}
