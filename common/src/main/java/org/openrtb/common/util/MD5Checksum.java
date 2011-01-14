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
package org.openrtb.common.util;

import java.io.IOException;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.openrtb.common.json.AbstractJsonTranslator;
import org.openrtb.common.model.Signable;

public class MD5Checksum {

    /**
     * This method performs the following actions based upon the specification:
     * <ol>
     * <li>remove the token for the request,</li>
     * <li>along with the shared secret, creates and MD5 hash, and then</li>
     * <li>sets the hash as the 'token' on the supplied {@link Signable}.</li>
     * </ol>
     * 
     * If there is an issue converting the {@link Signable} to JSON via the
     * supplied <tt>translator</tt>, then an
     * 
     * @param sharedSecret
     *            a byte array representing the shared secret between the
     *            requestor and requestee.
     * @param request
     *            the request to be sent to a listening service corresponding to
     *            the <tt>sharedSecret</tt> supplied.
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
    public static void signRequest(byte[] sharedSecret, Signable request,
                                   AbstractJsonTranslator translator) throws IOException {
        request.clearToken();
        StringBuilder signableStr = 
            new StringBuilder("{").append(translator.toJSON(request))
                                  .append(",sharedSecret:")
                                  .append(Hex.encodeHex(sharedSecret))
                                  .append("}");
        String token = DigestUtils.md5Hex(signableStr.toString());
        request.setToken(token);
    }
    
	public static byte[] createChecksum(String input) {
	    return DigestUtils.md5(input);
   }	
	
	// convert a byte array to a HEX string
	public static String getMD5Checksum(String input) {
	    return DigestUtils.md5Hex(input);
   }

}
