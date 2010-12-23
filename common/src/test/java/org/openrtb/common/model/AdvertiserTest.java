package org.openrtb.common.model;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Validation tests for {@link Advertiser}'s method behavior.
 */
public class AdvertiserTest {

    private Advertiser test;

    @Before
    public void setup() {
        test = new Advertiser();
    }

    /**
     * Make sure object is capable of stripping off protocol, subdomains, etc.
     * to leave only the top-level domain.
     */
    @Test @Ignore
    public void formatLandingPage() {
        String rawUrl = "https://Sports.Publisher.COM/NFL";
        String expectedUrl = "publisher.com";

        test = new Advertiser(rawUrl);
        assertEquals("url is not properly formatted by advertiser object",
                     expectedUrl, test.getLandingPage());
    }

    /**
     * Confirmation that all blocklist methods work correctly with null object
     * values.
     */
    @Test
    public void blocklistReturnsNonNull() {
        assertNotNull("blocklist should not be null on object creation",
                      test.getBlocklist());
    }

    @Test
    public void cannotSetBlocklistToNull() {
        test.setBlocklist(null);
        assertNotNull("blocklist should not be null when explicitly set as such",
                      test.getBlocklist());
    }

    @Test(expected = IllegalArgumentException.class)
    public void invalidBlocklistAddedAdvertiser() {
        test.addBlocklist(null);
    }

}
