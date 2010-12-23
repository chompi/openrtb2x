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
package org.openrtb.common.json;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.openrtb.common.model.Advertiser;
import org.openrtb.common.model.Blocklist;

/**
 * Verified the {@link Advertiser}'s translation to/from JSON.
 */
public class AdvertiserTranslatorTest {

    public static final Advertiser ADVERTISER = new Advertiser("mycarcompany.com",
                                                               "My_Car_Company",
                                                               System.currentTimeMillis());

    public static final Blocklist BLOCK1 = new Blocklist("h3lLo", "Welcome_Publisher",
                                                         "h3lLo-S1T3", "Home_Page");
    public static final Blocklist BLOCK2 = new Blocklist("g00d8ye", "Adios_Publisher");

    static {
        ADVERTISER.addBlocklist(BLOCK1);
        ADVERTISER.addBlocklist(BLOCK2);
    }

    private static final String PRETTY_VALUE =
        "{\n" +
        "  \"landingPageTLD\" : \""+ADVERTISER.getLandingPage()+"\"," +
        "  \"name\" : \""+ADVERTISER.getName()+"\"," +
        "  \"sinceThisTimestamp\" : "+ADVERTISER.getTimestamp()+"," +
        "  \"blocklist\" : [{\n" +
        "    \"publisherId\" : \""+BLOCK1.getPublisherId()+"\",\n" +
        "    \"publisherName\" : \""+BLOCK1.getPublisherName()+"\",\n" +
        "    \"siteId\" : \""+BLOCK1.getSiteId()+"\",\n" +
        "    \"siteName\" : \""+BLOCK1.getSiteName()+"\"\n" +
        "  }, {\n" +
        "    \"publisherId\" : \""+BLOCK2.getPublisherId()+"\",\n" +
        "    \"publisherName\" : \""+BLOCK2.getPublisherName()+"\"\n" +
        "  }]" +
        "}";

    private static final String EXPECTED_VALUE = PRETTY_VALUE.replaceAll("[ \n]", "");

    private AdvertiserTranslator test = new AdvertiserTranslator();

    public static void validateObject(Advertiser expectedObject, Advertiser actualObject) {
        assertEquals("unable to deserialize the landing page url",
                     expectedObject.getLandingPage(), actualObject.getLandingPage());
        assertEquals("unable to deserialize the name value",
                     expectedObject.getName(), actualObject.getName());
        assertEquals("unable to deserialize the timestamp value",
                     expectedObject.getTimestamp(), actualObject.getTimestamp());

        Map<String, Blocklist> expectedList = convertListToMap(expectedObject.getBlocklist());
        for(Blocklist blocklist : actualObject.getBlocklist()) {
            Blocklist expectedBlocklist = expectedList.get(blocklist.getPublisherId()+blocklist.getSiteId());
            assertNotNull("unexpected blocklist value in returned advertiser", expectedBlocklist);
            BlocklistTranslatorTest.validateObject(expectedBlocklist, blocklist);
        }
    }

    @Test
    public void serializeObject() throws IOException {
        assertEquals(EXPECTED_VALUE, test.toJSON(ADVERTISER));
    }

    @Test
    public void deserializeObject() throws IOException {
        validateObject(ADVERTISER, test.fromJSON(EXPECTED_VALUE));
    }

    @Test
    public void serializeEmptyObject() throws IOException {
        assertEquals("{}", test.toJSON(new Advertiser()));
    }

    @Test
    public void deserializeEmptyObject() throws IOException {
        validateObject(new Advertiser(), test.fromJSON("{}"));
    }

    private static Map<String, Blocklist> convertListToMap(List<Blocklist> list) {
        Map<String, Blocklist> retval = new HashMap<String, Blocklist>();

        for(Blocklist blocklist : list) {
            retval.put(blocklist.getPublisherId()+blocklist.getSiteId(), blocklist);
        }
        return retval;
    }

    private static class AdvertiserTranslator extends AbstractJsonTranslator<Advertiser> {
        public AdvertiserTranslator() {
            super(AdvertiserTranslator.class);
        }
    }
}
