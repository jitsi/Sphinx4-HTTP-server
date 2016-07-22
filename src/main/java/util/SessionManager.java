package util;

import server.Session;
import util.SessionIdentifierGenerator;

import java.util.ArrayList;
import java.util.HashMap;

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
    private HashMap<String, Session> sessions;

    /**
     * Constructor for a SessionManager
     */
    public SessionManager()
    {
        generator = new SessionIdentifierGenerator(ID_LENGTH);
        sessions = new HashMap<>();
    }

    /**
     * Creates a new Session with a unique ID
     * @return a new uniquely identified session
     */
    public Session createNewSession()
    {
        String newKey;
        do
        {
            newKey = generator.nextID();
        }
        while(!sessions.containsKey(newKey));
        sessions.put(newKey, new Session());
        return sessions.get(newKey);
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
