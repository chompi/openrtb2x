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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.junit.Test;
import org.openrtb.common.json.AbstractJsonTranslator;
/**
 * Validates the functionality of signing and verifying the signature of an
 * assoicated object.
 */
public class SignableTest {

    private static final Identification IDENT = new Identification("organization identifier", 1295019653468L);
    private static final String SOME_VALUE = "this is some value";

    // HEX'd BYTES : 6120737570657220646f6f706572207365637265742068657265
    private static final byte[] SECRET = "a super dooper secret here".getBytes();

    // computed md5 hash of all values
    private static final String MD5_VALIDATION = "560bdad6841e323991d9c156fd309cdc";

    private static final SignableTranslator translator = new SignableTranslator();

    @Test(expected = IllegalStateException.class)
    public void testSign_noIdentification() throws Exception {
        new SignableObject(null, SOME_VALUE).sign(SECRET, translator);
    }

    @Test
    public void testSign_badToken() throws Exception {
        Identification identification = new Identification(IDENT.getOrganization(), IDENT.getTimestamp());
        identification.setToken("somerandomtoken");
        SignableObject test = new SignableObject(identification, SOME_VALUE);
        test.sign(SECRET, translator);

        assertEquals("object signing doesn't match expected value",
                     MD5_VALIDATION, test.getIdentification().getToken());
    }

    @Test
    public void testSign_works() throws Exception {
        SignableObject test = new SignableObject(IDENT, SOME_VALUE);
        test.sign(SECRET, translator);

        assertEquals("object signing doesn't match expected value",
                     MD5_VALIDATION, test.getIdentification().getToken());
    }

    @Test
    public void testVerify_noToken() throws Exception {
        Identification identification = new Identification(IDENT.getOrganization(), IDENT.getTimestamp());
        SignableObject test = new SignableObject(identification, SOME_VALUE);

        assertFalse("message should have failed validation",
                    test.verify(SECRET, translator));
        assertNull("message should still have no token",
                   test.clearToken());

    }

    @Test
    public void testVerify_valid() throws Exception {
        SignableObject test = new SignableObject(IDENT, SOME_VALUE);
        test.setToken(MD5_VALIDATION);

        assertTrue("message should have passed validation",
                   test.verify(SECRET, translator));
        assertEquals("message should still have token value",
                     MD5_VALIDATION, test.clearToken());
    }

    @Test
    public void testVerify_invalid() throws Exception {
        SignableObject test = new SignableObject(IDENT, SOME_VALUE+" too");
        test.setToken(MD5_VALIDATION);

        assertFalse("message should NOT have passed validation",
                    test.verify(SECRET, translator));
        assertEquals("message should still have token value",
                     MD5_VALIDATION, test.clearToken());
    }

    private static class SignableTranslator extends AbstractJsonTranslator<SignableObject>{
        public SignableTranslator() { super(SignableTranslator.class); }
    }

    @JsonSerialize(include=Inclusion.NON_DEFAULT)
    @JsonPropertyOrder({"identification", "someValue"})
    private static class SignableObject extends Signable {

        @JsonProperty
        private Identification identification;
        @JsonProperty
        private String someValue;

        private SignableObject() {}

        public SignableObject(Identification identification, String someValue) {
            this.identification = identification;
            this.someValue = someValue;
        }

        @Override
        public Identification getIdentification() {
            return identification;
        }

    }
}
