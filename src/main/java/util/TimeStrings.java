package util;

import java.time.*;
import java.time.format.*;

/**
 * This class holds methods which can translate a time/date into string format
 */
public class TimeStrings
{
    /**
     * Formats a date into a string with a pattern
     * "day-month___hour-minutes-second-millisecond"
     */
    private static DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("dd-MM___HH-mm-ss-SSSS")
                    .withZone(ZoneId.systemDefault());

    /**
     * Get the current time as a string in the format dd-MM___HH-mm-ss-SSSS
     * @return the string representing the "current" time at the moment
     * of calling the method
     */
    public static String getNowString()
    {
        return formatter.format(Instant.now());
    }
}
