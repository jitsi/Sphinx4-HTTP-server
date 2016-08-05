/*
 * Sphinx4 HTTP server
 *
 * Copyright @ 2016 Atlassian Pty Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jitsi.sphinx4http.util;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

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
