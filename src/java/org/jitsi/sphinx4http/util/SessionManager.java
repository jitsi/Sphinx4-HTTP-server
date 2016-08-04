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

import org.jitsi.sphinx4http.server.Session;
import org.jitsi.sphinx4http.util.SessionIdentifierGenerator;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


/**
 * Manages a HashMap containing all Sessions handled
 * by the server
 */
public class SessionManager
{
    /**
     * The length of the generated ID's associated with the sessions
     */
    private final static int ID_LENGTH = 32;

    /**
     * Generated the ID's for new sessions
     */
    private SessionIdentifierGenerator generator;

    /**
     * HashMap mapping the ID of the session the the session
     */
    private Map<String, Session> sessions;

    /**
     * Constructor for a SessionManager
     */
    public SessionManager()
    {
        generator = new SessionIdentifierGenerator(ID_LENGTH);
        sessions = Collections.synchronizedMap(new HashMap<String, Session>());
    }

    /**
     * Creates a new Session with a unique ID
     * @return a new uniquely identified session
     */
    public Session createNewSession()
    {
        String id;
        do
        {
            id = generator.nextID();
        }
        while(sessions.containsKey(id));

        sessions.put(id, new Session(id));
        return sessions.get(id);
    }

    /**
     * Gets a session corresponding to the given ID. If the session doesn't
     * exist, null will be returned
     * @param key the ID for the session
     * @return the session associated with the ID or null if the session doesn't
     * exist
     */
    public Session getSession(String key)
    {
        return sessions.get(key);
    }

    /**
     * Checks whether the given key maps to a session
     * @param key the ID of a possible session
     * @return whether the ID has an associated Session
     */
    public boolean hasID(String key)
    {
        return sessions.containsKey(key);
    }

}
