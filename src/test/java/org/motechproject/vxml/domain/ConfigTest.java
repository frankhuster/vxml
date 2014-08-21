package org.motechproject.vxml.domain;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Config Unit Tests
 */
public class ConfigTest {

    private static final String TOKEN_KEY = "tok";
    private static final String TOKEN_VALUE = "68496w84ef682f6des8";
    private static final String OUTGOING_URI_TEMPLATE_FORMAT = "http://foo.com/bar?token=%s";
    private static final String OUTGOING_URI_TEMPLATE = String.format(OUTGOING_URI_TEMPLATE_FORMAT,
            String.format("[%s]", TOKEN_KEY));

    //todo: test 'ignored' fields

    @Test
    public void shouldSubstituteOutgoingCallUriParameters() {
        Map<String, String> outgoingCallUriParams = new HashMap<>();
        outgoingCallUriParams.put(TOKEN_KEY, TOKEN_VALUE);

        Config config = new Config("Config", "ignore1, ignore2", OUTGOING_URI_TEMPLATE, HttpMethod.GET, outgoingCallUriParams);
        String expectedUriTemplate = String.format(OUTGOING_URI_TEMPLATE_FORMAT, TOKEN_VALUE);
        assertEquals(expectedUriTemplate, config.outgoingCallUri());
    }
}
