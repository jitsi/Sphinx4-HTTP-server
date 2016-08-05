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

package org.jitsi.sphinx4http.server;

import org.eclipse.jetty.server.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main method starting the HTTP server
 * Accepts HTTP POST requests on port 8081
 */
public class HttpServer
{
    /**
     * The logger of this class
     */
    private static final Logger logger =
            LoggerFactory.getLogger(HttpServer.class);

    /**
     * Main method starting the server
     * @param args command line argument specifying the port
     */
    public static void main(String[] args)
    {
        int port = 8081;
        if (args.length >= 1)
        {
            try
            {
                port = Integer.parseInt(args[0]);
            }
            catch (NumberFormatException e)
            {
                logger.info("{} is not a valid port. Exiting.", args[0]);
                System.exit(1);
            }
        }
        try
        {
            Server server = new Server(port);
            server.setHandler(new RequestHandler());
            server.start();
            server.join();
        }
        catch (Exception e)
        {
            logger.info("Something went wrong while starting the " +
                    "server. Is the port {} already in use? stacktrace:\n {}",
                    port, e.getMessage());
            System.exit(2);
        }
    }
}
