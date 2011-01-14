package org.openrtb.dsp.core;

import org.openrtb.common.model.AdvertiserBlocklistResponse;
import org.openrtb.dsp.intf.model.Exchange;
import org.openrtb.dsp.intf.service.AdvertiserService;
import org.openrtb.dsp.intf.service.IdentificationService;

/**
 * This class does the same thing as its parent, only it never sends a request.
 * Instead, we stub out the method and return the response as set by the
 * corresponding setter method #.
 */
public class AdvertiserBlocklistNeverRequest extends
                                            AdvertiserBlocklistRequester {

    private AdvertiserBlocklistResponse response;

    public AdvertiserBlocklistNeverRequest(AdvertiserService advertiserService,
                                           IdentificationService identificationService) {
        super(advertiserService, identificationService);
    }

    /**
     * Reset the requester's state back to a clean state for the next test.
     */
    public void reset() {
        response = null;
    }

    public void setResponse(AdvertiserBlocklistResponse response) {
        this.response = response;
    }

    @Override
    AdvertiserBlocklistResponse makeRequest(Exchange ssp, String request) {
        return response;
    }

}
