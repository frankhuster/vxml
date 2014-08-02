package org.motechproject.vxml.service;

/**
 * Helper class - Uses StatusMessageService to send system Alerts
 */
public interface MotechStatusMessage {

    void alert(String message);
}
