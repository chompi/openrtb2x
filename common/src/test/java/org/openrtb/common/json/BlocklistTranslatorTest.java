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
import org.openrtb.common.model.Blocklist;

/**
 * Verified the {@link Blocklist}'s translation to/from JSON.
 */
public class BlocklistTranslatorTest {

    private static final Blocklist BLOCKLIST =
        new Blocklist("h3lLo", "Welcome_Publisher", "h3lLo-S1T3", "Home_Page");

    private static final String PRETTY_VALUE =
        "{" +
        "  \"publisherId\" : \""+BLOCKLIST.getPublisherId()+"\",\n" +
        "  \"publisherName\" : \""+BLOCKLIST.getPublisherName()+"\",\n" +
        "  \"siteId\" : \""+BLOCKLIST.getSiteId()+"\",\n" +
        "  \"siteName\" : \""+BLOCKLIST.getSiteName()+"\"\n" +
        "}";

    private static final String EXPECTED_VALUE = PRETTY_VALUE.replaceAll("[ \n]", "");

    private BlocklistTranslator test = new BlocklistTranslator();

    @Test
    public void serializeObject() throws IOException {
        assertEquals(EXPECTED_VALUE, test.toJSON(BLOCKLIST));
    }

    @Test
    public void deserializeObject() throws IOException {
        validateObject(BLOCKLIST, test.fromJSON(PRETTY_VALUE));
    }

    @Test
    public void serializeEmptyObject() throws IOException {
        assertEquals("{}", test.toJSON(new Blocklist()));
    }

    @Test
    public void deserializeEmptyObject() throws IOException {
        validateObject(new Blocklist(), test.fromJSON("{}"));
    }

    private void validateObject(Blocklist expectedObject, Blocklist actualObject) {
        assertEquals("unable to deserialize the publisher id value",
                     expectedObject.getPublisherId(), actualObject.getPublisherId());
        assertEquals("unable to deserialize the publisher name value",
                     expectedObject.getPublisherName(), actualObject.getPublisherName());
        assertEquals("unable to deserialize the site id value",
                     expectedObject.getSiteId(), actualObject.getSiteId());
        assertEquals("unable to deserialize the site name value",
                     expectedObject.getSiteName(), actualObject.getSiteName());
    }

    private static class BlocklistTranslator extends AbstractJsonTranslator<Blocklist> {
        public BlocklistTranslator() {
            super(BlocklistTranslator.class);
        }
    }

}
