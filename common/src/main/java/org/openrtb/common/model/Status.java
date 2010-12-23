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

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

/**
 * All request responses include the {@link Identification} object in addition
 * to the status object. The status object contains the necessary error messages
 * and status for its associated request.
 *
 * @since 1.0
 */
@JsonSerialize(include = Inclusion.NON_NULL)
@JsonPropertyOrder({"requestToken", "statusCode", "statusMessage"})
public class Status {

    public static final int SUCCESS_CODE = 0;
    public static final String SUCCESS_MESSAGE = "success";

    @JsonProperty
    private String requestToken;

    @JsonProperty("statusCode")
    private Integer code;

    @JsonProperty("statusMessage")
    private String message;

    protected Status() { }

    public Status(String requestToken) {
        this(requestToken, SUCCESS_CODE, SUCCESS_MESSAGE);
    }

    public Status(String requestToken, Integer code, String message) {
        setRequestToken(requestToken);
        setResponseCode(code, message);
    }

    /**
     * The identifier associated with the original request.
     */
    public String getRequestToken() {
        return requestToken;
    }
    public void setRequestToken(String requestToken) {
        if (requestToken == null) {
            throw new IllegalArgumentException("requestToken passed to Status#setRequestToken() must be non-null");
        }
        this.requestToken = requestToken;
    }


    public void setResponseCode(int code, String message) {
        if (code == SUCCESS_CODE && message != SUCCESS_MESSAGE) {
            if (message != null) {
                throw new IllegalArgumentException("code of 0 (zero) requires the message be set to 'success' (Status#SUCCESS_MESSAGE)");
            }
            message = SUCCESS_MESSAGE;
        }
        if (message == null) {
            throw new IllegalArgumentException("message passed to Status#setResponseCode() must be non-null");
        }

        this.code = code;
        this.message = message;
    }
    /**
     * An integral status code associated with the request.
     * <ul>
     * <li><code>0</code>: indicates no error.</li>
     * <li><code>1</code>: indicates authentication error.</li>
     * <li><code>2</code>: indicates a duplicate transaction.</li>
     * <li><code>3</code>: indicates other error (refer to {@link #message}).</li>
     * </ul>
     *
     * Individual requests may introduce their own, unique, specific error codes
     * outside of these values. Request specific values are greater than or
     * equal to 100.
     */
    @JsonProperty("statusCode")
    public Integer getCode() {
        return code;
    }

    /**
     * For status codes of <code>0</code>, "<tt>success</tt>" is returned;
     * otherwise and error message is present describing the issue with
     * validation (for example, "<tt>incorrect password</tt>", or
     * "<tt>user not found</tt>")
     */
    @JsonProperty("statusMessage")
    public String getMessage() {
        return message;
    }

}
