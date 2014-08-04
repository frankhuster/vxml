package org.motechproject.vxml.it;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Specify which integration test classes to run
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({StatusControllerIT.class, ConfigDataServiceIT.class, OutboundCallServiceIT.class,
        CallDetailRecordServiceIT.class})
public class IntegrationTests {
}
