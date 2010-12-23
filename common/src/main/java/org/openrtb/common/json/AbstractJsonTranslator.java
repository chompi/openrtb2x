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

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.ParameterizedType;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.MappingJsonFactory;
import org.codehaus.jackson.map.ObjectMapper;

/**
 * This generic class is responsible for converting JSON formatted inputs into
 * instances of <code>T</code>. All transformation configuration data is
 * expected to be set on <code>T</code> in the form of annotations.
 *
 * For examples on how to use this class, please refer to the unit tests for
 * specific examples.
 *
 * @param <T>
 *            The class to serialize and deserialize to and from JSON.
 */
public abstract class AbstractJsonTranslator<T> {

    private Class<?> clazz;
    private boolean usePrettyPrinter;


    public AbstractJsonTranslator(Class<? extends AbstractJsonTranslator<?>> subclass) {
        ParameterizedType pType = (ParameterizedType) subclass.getGenericSuperclass();
        clazz = (Class<?>)pType.getActualTypeArguments()[0];
    }

    /**
     * Attempts to convert the <code>json</code> {@link String} into an instance
     * of the parameterized type <code>T</code>.
     *
     * If the parser is unsuccessful, this method will throw one of
     * {@link JsonMappingException}, {@link JsonParseException}, or an
     * {@link IOException} as specified in {@link #fromJSON(Reader)}.
     *
     * @param json
     *            The JSON formatted {@link String} to construct an object
     *            instance from.
     * @return An instance of parameterized type <code>T</code>.
     * @throws JsonMappingException
     *             Refer to {@link #fromJSON(Reader)} for more information.
     * @throws JsonParseException
     *             Refer to {@link #fromJSON(Reader)} for more information.
     * @throws IOException
     *             Refer to {@link #fromJSON(Reader)} for more information.
     */
    public T fromJSON(String json)
            throws JsonMappingException, JsonParseException, IOException {
        return fromJSON(new StringReader(json));
    }

    /**
     * Attempts to convert the <code>json</code> {@link String} into an instance
     * of the parameterized type <code>T</code>.
     *
     * If the parser is unsuccessful, this method will throw one of
     * {@link JsonMappingException}, {@link JsonParseException}, or an
     * {@link IOException} as specified in {@link #fromJSON(Reader)}.
     *
     * @param reader
     * @return An instance of parameterized type <code>T</code>.
     * @throws JsonMappingException
     *             Refer to {@link #fromJSON(Reader)} for more information.
     * @throws JsonParseException
     *             Refer to {@link #fromJSON(Reader)} for more information.
     * @throws IOException
     *             Refer to {@link #fromJSON(Reader)} for more information.
     */
    @SuppressWarnings("unchecked")
    public T fromJSON(Reader reader)
            throws JsonMappingException, JsonParseException, IOException {
        return (T)new ObjectMapper().readValue(reader, clazz);
    }

    public String toJSON(T value) throws IOException {
        Writer writer = new StringWriter();
        toJSON(writer, value);
        return writer.toString();
    }

    public void toJSON(Writer writer, T value) throws IOException {
        MappingJsonFactory factory = new MappingJsonFactory();
        JsonGenerator generator = factory.createJsonGenerator(writer);

        if (usePrettyPrinter) {
            generator.useDefaultPrettyPrinter();
        }
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(generator, value);
    }

    public void usePrettyPrinter() {
        usePrettyPrinter = true;
    }
    public void disablePrettyPrint() {
        usePrettyPrinter = false;
    }

    /**
     * Returns the {@link Class} that this converter object is responsible for
     * translating. Used for testing.
     *
     * This is primarily used for testing purposes.
     */
    Class<?> getTranslatedType() {
        return clazz;
    }

}
