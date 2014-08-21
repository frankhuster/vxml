package org.motechproject.vxml;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * Helper used to generate the current time as a string in a specially formatted (sortable) way
 */
public class TimestampHelper {
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.SSSS");
    public static String currentTime() {
        return dateTimeFormatter.print(DateTime.now());
    }
}
