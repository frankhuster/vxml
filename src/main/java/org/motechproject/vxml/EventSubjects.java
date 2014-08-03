package org.motechproject.vxml;

import org.motechproject.vxml.domain.CallStatus;

/**
 * Event subjects, mirrors CallStatus
 */
public final class EventSubjects {

    private EventSubjects() { }
    public static final String CALL_INITIATED = "vxml_call_initiated";
    public static final String CALL_IN_PROGRESS = "vxml_call_in_progress";
    public static final String CALL_ANSWERED = "vxml_call_answered";
    public static final String BUSY = "vxml_busy";
    public static final String CALL_FAILED = "vxml_call_failed";
    public static final String NO_ANSWER = "vxml_no_answer";
    public static final String UNKNOWN = "vxml_unknown";

    public static String subjectFromCallStatus(CallStatus callStatus) {
        switch (callStatus) {
            case UNKNOWN:
                return UNKNOWN;

            case NO_ANSWER:
                return NO_ANSWER;

            case FAILED:
                return CALL_FAILED;

            case BUSY:
                return BUSY;

            case ANSWERED:
                return CALL_ANSWERED;

            case IN_PROGRESS:
                return CALL_IN_PROGRESS;

            case INITIATED:
                return CALL_INITIATED;
        }

        throw new IllegalStateException(String.format("Unable to match CallStatus {} to an event Subject", callStatus));
    }
}
