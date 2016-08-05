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

import com.sun.xml.internal.bind.v2.model.core.ID;
import com.sun.xml.internal.ws.api.message.ExceptionHasMessage;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.client.api.Response;
import org.eclipse.jetty.client.api.Result;
import org.eclipse.jetty.http.HttpMethod;
import org.jitsi.sphinx4http.server.HttpServer;
import org.jitsi.sphinx4http.server.Session;
import org.jitsi.sphinx4http.util.SessionManager;
import org.junit.Assert;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;


/**
 * Tests if sessions create unique ID's
 */
public class SessionTest
{
    @org.junit.Test
    public void testSessionCreation()
    {
        SessionManager manager = new SessionManager();
        ArrayList<String> sessionIDs = new ArrayList<>();
        for(int i = 0; i < 100; i++)
        {
            String newID = manager.createNewSession().getId();
            Assert.assertFalse("Session ID was already created",
                    sessionIDs.contains(newID));
            sessionIDs.add(newID);
        }
    }
}
