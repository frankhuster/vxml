package org.motechproject.vxml.service.it;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Specify which integration test classes to run
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({CallDetailRecordServiceIT.class, ConfigDataServiceIT.class, OutboundCallServiceIT.class})
public class IntegrationTests {
}
