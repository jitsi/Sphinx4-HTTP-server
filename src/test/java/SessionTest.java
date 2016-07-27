import com.sun.xml.internal.bind.v2.model.core.ID;
import com.sun.xml.internal.ws.api.message.ExceptionHasMessage;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.client.api.Response;
import org.eclipse.jetty.client.api.Result;
import org.eclipse.jetty.http.HttpMethod;
import server.HttpServer;
import server.Session;
import util.SessionManager;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;


/**
 * Tests if sessions create unique ID's
 */
public class SessionTest
{

    private static String uri = "http://localhost:8081/recognize";

    public static void main(String[] args) throws Exception
    {
        SessionManager manager = new SessionManager();
        ArrayList<String> sessionIDs = new ArrayList<>();
        for(int i = 0; i < 100; i++)
        {
            String newID = manager.createNewSession().getId();
            if(sessionIDs.contains(newID))
            {
                throw new Exception("SessionTest has failed");
            }
            sessionIDs.add(newID);
        }
    }
}
