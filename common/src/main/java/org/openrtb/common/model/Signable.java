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

import java.io.IOException;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openrtb.common.json.AbstractJsonTranslator;

/**
 * This interface is used to aide in the creation of identifiable/verifiable
 * tokens for the various requests and response between the DSP and SSP.
 *
 * @since 1.0
 */
public abstract class Signable {

    private static final Log log = LogFactory.getLog(Signable.class);

    abstract Identification getIdentification();


    /**
     * This method performs the following actions based upon the specification:
     * <ol>
     * <li>remove the token for the request/response,</li>
     * <li>along with the shared secret, creates and MD5 hash, and then</li>
     * <li>sets the hash as the 'token' on the supplied {@link Signable}.</li>
     * </ol>
     *
     * If there is an issue converting the {@link Signable} to JSON via the
     * supplied <tt>translator</tt>, then an
     *
     * @param sharedSecret
     *            a byte array representing the shared secret between the
     *            sender and receiver.
     * @param translator
     *            a specific {@link AbstractJsonTranslator} associated with the
     *            supplied {@link Signable} <tt>request</tt>. If the wrong
     *            <tt>translator</tt> is supplied for the supplied
     *            <tt>request</tt>, well, then any number of things can go
     *            wrong.  Good luck.
     * @throws IOException
     *             should the {@link Signable} be unable to be converted to
     *             JSON, an <tt>IOException</tt> will be thrown.
     */
    public void sign(byte[] sharedSecret, AbstractJsonTranslator translator)
            throws IOException {
        clearToken();
        StringBuilder signableStr =
            new StringBuilder("{").append(translator.toJSON(this))
                                  .append(",\"sharedSecret\":")
                                  .append(Hex.encodeHex(sharedSecret))
                                  .append("}");
        String token = DigestUtils.md5Hex(signableStr.toString());
        setToken(token);
    }

    /**
     * This method validates the messages contents are intact based upon the
     * supplied <tt>sharedSecret</tt>. The method will remove the respective
     * object's <tt>token</tt> value for verification, but will replace the
     * value back before exiting the method.
     *
     * @param sharedSecret
     *            a byte array representing the shared secret between the sender
     *            and receiver.
     * @param translator
     *            a specific {@link AbstractJsonTranslator} associated with the
     *            supplied {@link Signable} <tt>request</tt>. If the wrong
     *            <tt>translator</tt> is supplied for the supplied
     *            <tt>request</tt>, well, then any number of things can go
     *            wrong. Good luck.
     * @return <tt>true</tt> if the verification completed successfully,
     *         <tt>false</tt> otherwise.
     * @throws IOException
     *             should the {@link Signable} be unable to be converted to
     *             JSON, an <tt>IOException</tt> will be thrown.
     */
    public boolean verify(byte[] sharedSecret, AbstractJsonTranslator translator)
            throws IOException {
        String token = clearToken();
        try {
            if (token == null) {
                log.warn("this signable has not been signed yet; nothing to verify");
                return false;
            }

            StringBuilder signableStr =
                new StringBuilder("{").append(translator.toJSON(this))
                                      .append(",\"sharedSecret\":")
                                      .append(Hex.encodeHex(sharedSecret))
                                      .append("}");
            String verification = DigestUtils.md5Hex(signableStr.toString());
            if (!token.equals(verification)) {
                log.error("signable verification ["+verification+"] failed against delivered value ["+token+"]");
                return false;
            }
        } finally {
            setToken(token);
        }
        return true;
    }

    /**
     * Clear the token from the associated object, if one exists, and return the
     * value prior to clearing it out of the object. <tt>null</tt> is a valid
     * token, however, the internal state of the object implementing this
     * interface may not be complete enough to identify if a token is present.
     *
     * If this case occurs, throw an appropriate {@link IllegalStateException}
     * indicating why one can not be identified.
     *
     * @return the token value used to uniquely identify/validate the object, if
     *         one exists, including <tt>null</tt>.
     * @throws IllegalStateException
     *             if you are unable to identify a token for the signable
     *             object, for whatever reason, no {@link Identification} object
     *             exists prior to calling this method; indicative of a logic
     *             error elsewhere in your application.
     */
    public String clearToken() {
        validateIdentification();

        String token = getIdentification().getToken();
        getIdentification().setToken(null);
        return token;
    }

    /**
     * For the associated object, set its internal token to the supplied
     * <tt>token</tt> value. If you are unable to do so, throw an
     * {@link IllegalStateException} to the caller to indicate that it is
     * currently not possible to do so.
     *
     * The <tt>token</tt> value supplied should be non-<tt>null</tt>. If a
     * <tt>null</tt> is supplied, then an {@link IllegalArgumentException} will
     * be thrown. If the caller desires to clear out the token value for the
     * associated object, please see {@link #clearToken()}.
     *
     * @param token
     *            non-<tt>null</tt> value to set on the assoicate object as a
     *            token.
     * @throws IllegalStateException
     *             if you are unable to set the supplied token on hte associated
     *             object.
     * @throws IllegalArgumentException
     *             if the supplied token value is <tt>null</tt>.
     */
    public void setToken(String token) {
        validateIdentification();
        getIdentification().setToken(token);
    }

    /**
     * Make sure the member variable <tt>identification</tt> is non-
     * <tt>null</tt>.
     *
     * @throws IllegalStateException
     *             if the member variable <tt>identification</tt> fails
     *             validation.
     */
    protected void validateIdentification() {
        try { validateIdentification(getIdentification()); }
        catch (IllegalArgumentException e) { throw new IllegalStateException(e.getMessage(), e); }
    }

    /**
     * Make sure the <tt>identification</tt> supplied to the method is non-
     * <tt>null</tt>.
     *
     * @param identification
     *            {@link Identification} object to be validated.
     * @throws IllegalArguementException
     *             if the identification object supplied is <tt>null</tt>.
     */
    protected void validateIdentification(Identification identification) {
        if (identification == null) {
            throw new IllegalArgumentException("Identification is required for AdvertiserBlocklistRequest and must be non-null");
        }
    }

}
