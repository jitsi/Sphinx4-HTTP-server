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

import org.jitsi.sphinx4http.util.SessionManager;
import org.junit.Assert;

import java.util.ArrayList;

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
