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

/**
 * This interface is used to aide in the creation of identifiable/verifiable
 * tokens for the various requests and response between the DSP and SSP.
 * 
 * @since 1.0
 */
public interface Signable {

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
    String clearToken();

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
    void setToken(String token);
}
