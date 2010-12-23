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

import java.io.IOException;

import org.junit.Test;
import org.openrtb.common.model.Status;

/**
 * Verified the {@link Status}'s translation to/from JSON.
 */
public class StatusTranslatorTest {

    private static final Status STATUS =
        new Status("5449a6a03be7022b74215c34e32d4f82", 0, "success");

    private static final String PRETTY_VALUE =
        "{" +
        "  \"requestToken\" : \""+STATUS.getRequestToken()+"\",\n" +
        "  \"statusCode\" : "+STATUS.getCode()+",\n" +
        "  \"statusMessage\" : \""+STATUS.getMessage()+"\"\n" +
        "}";

    private static final String EXPECTED_VALUE = PRETTY_VALUE.replaceAll("[ \n]", "");

    private StatusTranslator test = new StatusTranslator();

    public static void validateObject(Status expectedObject, Status actualObject) {
        assertEquals("unable to deserialize the request token",
                     expectedObject.getRequestToken(), actualObject.getRequestToken());
        assertEquals("unable to deserialize the code value",
                     expectedObject.getCode(), actualObject.getCode());
        assertEquals("unable to deserialize the message value",
                     expectedObject.getMessage(), actualObject.getMessage());
    }

    @Test
    public void serializeObject() throws IOException {
        assertEquals(EXPECTED_VALUE, test.toJSON(STATUS));
    }

    @Test
    public void deserializeObject() throws IOException {
        validateObject(STATUS, test.fromJSON(PRETTY_VALUE));
    }

    @Test
    public void serializeEmptyObject() throws IOException {
        assertEquals("{}", test.toJSON(new TestObject()));
    }

    @Test
    public void deserializeEmptyObject() throws IOException {
        validateObject(new TestObject(), test.fromJSON("{}"));
    }

    private static class StatusTranslator extends AbstractJsonTranslator<Status> {
        public StatusTranslator() {
            super(StatusTranslator.class);
        }
    }

    private static class TestObject extends Status { }
}
