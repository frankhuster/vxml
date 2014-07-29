package org.motechproject.vxml.alert;

/**
 * Helper class - Uses StatusMessageService to send system Alerts
 */
public interface MotechStatusMessage {

    void alert(String message);
}
