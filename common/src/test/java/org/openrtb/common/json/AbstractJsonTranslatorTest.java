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
