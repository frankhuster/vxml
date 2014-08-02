package org.motechproject.vxml.service.impl;

import org.motechproject.admin.service.StatusMessageService;
import org.motechproject.vxml.service.MotechStatusMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Helper class - Uses StatusMessageService to send system Alerts
 */
@Service
public class MotechStatusMessageImpl implements MotechStatusMessage {
    @Autowired
    private StatusMessageService statusMessageService;

    public void alert(String message) {
        statusMessageService.warn(message, "vxml");
    }
}
