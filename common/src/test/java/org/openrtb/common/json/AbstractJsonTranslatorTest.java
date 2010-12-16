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
import org.junit.Ignore;
import org.junit.Test;

/**
 * Verifies the {@link AbstractJsonTranslator} behaves as expected.
 */
public class AbstractJsonTranslatorTest {

    private static final SubType SUBTYPE = new SubType("subtype-value");
    private static final ParentType PARENT = new ParentType(SUBTYPE, 1234L, "parent-value");

    private static final String PRETTY_VALUE =
        "{\n"+
        "  \"object\" : {\n"+
        "    \"value\" : \"subtype-value\"\n"+
        "  },\n"+
        "  \"long\" : 1234,\n"+
        "  \"string\" : \"parent-value\"\n"+
        "}";

    private static final String DEFAULT_VALUE = PRETTY_VALUE.replaceAll("[ \n]", "");


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
    public void serializeObject() throws Exception {
        assertEquals("JSON representation of object model is different than expected",
                     DEFAULT_VALUE, test.toJSON(PARENT));
    }

    @Test
    public void deserializeObject() throws Exception {
        ParentType value = test.fromJSON(DEFAULT_VALUE);

        assertEquals("parent's subtype value did not deserialize correctly",
                     PARENT.getFirst().getValue(), value.getFirst().getValue());
        assertEquals("parent's long value is not expected value",
                     PARENT.getSecond(), value.getSecond());
        assertEquals("parent's string value is not expected value",
                     PARENT.getThird(), value.getThird());
    }

    @Test
    public void verifyPrettyPrinter() throws IOException {
        test.usePrettyPrinter();
        assertEquals("pretty printer didn't return the expected results",
                     PRETTY_VALUE, test.toJSON(PARENT));

        test.disablePrettyPrint();
        assertEquals("should display default json text again",
                     DEFAULT_VALUE, test.toJSON(PARENT));
    }

    @Test @Ignore
    public void dontPrintNullValues() throws Exception {
        long value = 0L;
        ParentType parent = new ParentType(null, value, null);

        String expectedValue = "{\"long\":"+value+"}";
        assertEquals("expected value should not contain null values",
                     expectedValue, test.toJSON(parent));
    }

    /*
     * The following classes are used to verify the behavior/functionality of
     * the JsonTranslator independent of the other model implementations.
     */
    private static class JsonTestTranslator extends AbstractJsonTranslator<ParentType> {
        JsonTestTranslator() {super(JsonTestTranslator.class);}
    }

    /**
     * This class demonstrates creating an immutable object.
     */
    @JsonSerialize
    @JsonPropertyOrder({"first", "second", "third"})
    private static class ParentType {
        private SubType objectValue;
        private long longValue;
        private String stringValue;

        @JsonCreator
        public ParentType(@JsonProperty("object") SubType first,
                          @JsonProperty("long") long second,
                          @JsonProperty("string") String third) {
            this.objectValue = first;
            this.longValue = second;
            this.stringValue = third;
        }

        @JsonProperty("object")
        public SubType getFirst() {
            return objectValue;
        }
        @JsonProperty("long")
        public long getSecond() {
            return longValue;
        }
        @JsonProperty("string")
        public String getThird() {
            return stringValue;
        }
    }

    /**
     * This class demonstrates creating a mutable object without the
     * {@link JsonCreator} annotation.
     */
    @JsonSerialize
    private static class SubType {
        @JsonProperty("value")
        private String myValue;

        /**
         * The default constructor is needed when the JsonCreator annotation is
         * not used.
         */
        public SubType() { }

        public SubType(String myValue) {
            this.myValue = myValue;
        }

        public String getValue() {
            return myValue;
        }
        public void setValue(String value) {
            myValue = value;
        }
    }

}
