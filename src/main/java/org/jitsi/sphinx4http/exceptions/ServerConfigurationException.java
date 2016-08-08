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

package org.jitsi.sphinx4http.exceptions;

/**
 * Thrown when something goes wrong in reading the sphinx4http.properties
 * file in the ServerConfiguration class
 *
 * @author Nik Vaessen
 */
public class ServerConfigurationException extends Exception
{
    public ServerConfigurationException()
    {
        super();
    }

    public ServerConfigurationException(String s)
    {
        super(s);
    }
}
