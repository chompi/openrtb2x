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
package org.openrtb.dsp.intf.service;

import java.util.Collections;
import java.util.List;

import org.openrtb.common.model.Advertiser;
import org.openrtb.common.model.Blocklist;

/**
 * This service is responsible for retrieving and storing data necessary to
 * support the synchronization of advertiser data between the DSP and SSP.
 *
 * In order to request {@link Blocklist}s from the SSP, a list of
 * {@link Advertiser}s is required. The only required field is the
 * {@link Advertiser#getLandingPage()}. For more information about populating
 * that object, please refer to the {@link Advertiser} javadoc.
 *
 * Responses from the SSP need to be persisted. Complete replacements of data
 * will make a call to the {@link #replaceAdvertiserBlocklists(List)}. If the
 * blocklist values being returned are an incremental update to the advertiser,
 * then {@link #updateAdvertiserBlocklists(List)} will be called.
 *
 * @since 1.0
 */
public interface AdvertiserService {

    public static final String SPRING_NAME = "dsp.intf.AdvertiserService";

    /**
     * Return the list of advertisers you would like to request
     * {@link Blocklist} for. If the desire-ment is to not request any
     * blocklists, then return {@link Collections#emptyList()}.
     *
     * @return return a non-<tt>null</tt> value list of {@link Advertiser}s to
     *         request blocklists for. If the demand-side platform wishes to not
     *         synchronize any advertisers, an empty list will suffice.
     */
    public List<Advertiser> getAdvertiserList();

    /**
     * {@link Advertiser}s supplied in this call will have their entire
     * {@link Blocklist} entry replaced in the demand-side store.
     *
     * This method is necessary as the {@link #updateAdvertiserBlocklists(List)}
     * does not support deletions.
     *
     * Implementers should be aware that {@link Advertiser}s can have no
     * {@link Blocklist}s being returned from the supply-side platform. Please
     * see those javadocs for more informaton.
     *
     * @param advertisers
     *            a non-<tt>null</tt> list of advertisers whose blocklist
     *            contents need to be replaced in the demand-side platform's
     *            persistant store.
     */
    public void replaceAdvertiserBlocklists(List<Advertiser> advertisers);

    /**
     * For the {@link Advertiser}s passed in this request, the {@link Blocklist}
     * supplied should be merged with the values currently stored in the
     * demand-side platform.
     *
     * This method does not provide a blocklist removal mechanism. If you
     * require such a mechanism, Advertisers should be requested without
     * timestamps ({@link Advertiser#getTimestamp()}).
     *
     * Implementers should be aware that {@link Advertiser}s can have no
     * {@link Blocklist}s being returned from the supply-side platform. Please
     * see those javadocs for more informaton.
     *
     * @param advertisers
     *            a non-<tt>null</tt> list of advertisers to update in the
     *            demand-side platform's persistant store.
     */
    public void updateAdvertiserBlocklists(List<Advertiser> advertisers);

}
