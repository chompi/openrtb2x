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

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.junit.Before;
import org.junit.Test;

/**
 * Verifies the {@link AbstractJsonTranslator} behaves as expected.
 */
public class AbstractJsonTranslatorTest {

    private JsonTestTranslator test= new JsonTestTranslator();

    @Before
    public void before() {
        test = new JsonTestTranslator();
    }

    @Test
    public void identifiesCorrectClass() {
        assertEquals("unexpected type returned from translator",
                     ParentType.class, test.getTranslatedType());
    }

    @Test
    public void verifyPrettyPrinter() throws IOException {
        SubType sub = new SubType("subtype-value");
        ParentType parent = new ParentType(sub, 1234L, "parent-value");

        String prettyValue =
        "{\n"+
        "  \"first\" : {\n"+
        "    \"myValue\" : \"subtype-value\"\n"+
        "  },\n"+
        "  \"second\" : 1234,\n"+
        "  \"third\" : \"parent-value\"\n"+
        "}";

        String defaultValue = prettyValue.replaceAll("[ \n]", "");
        assertEquals("default return value is different than expected",
                     defaultValue, test.toJSON(parent));

        test.usePrettyPrinter();
        assertEquals("pretty printer didn't return the expected results",
                     prettyValue, test.toJSON(parent));

        test.disablePrettyPrint();
        assertEquals("should display default json text again",
                     defaultValue, test.toJSON(parent));
    }

    /*
     * The following classes are used to verify the behavior/functionality of
     * the JsonTranslator independent of the other model implementations.
     */
    private static class JsonTestTranslator extends AbstractJsonTranslator<ParentType> {
        JsonTestTranslator() {super(JsonTestTranslator.class);}
    }

    @JsonSerialize
    @JsonPropertyOrder({"first", "second", "third"})
    private static class ParentType {
        private SubType first;
        private long second;
        private String third;

        @JsonCreator
        public ParentType(@JsonProperty("first") SubType first,
                   @JsonProperty("second") long second,
                   @JsonProperty("third") String third) {
            this.first = first;
            this.second = second;
            this.third = third;
        }

        public SubType getFirst() {
            return first;
        }
        public long getSecond() {
            return second;
        }
        public String getThird() {
            return third;
        }
    }

    @JsonSerialize
    private static class SubType {
        private String myValue;

        @JsonCreator
        public SubType(@JsonProperty("myValue") String myValue) {
            this.myValue = myValue;
        }

        @JsonProperty("myValue")
        public String getValue() {
            return myValue;
        }
    }

}
