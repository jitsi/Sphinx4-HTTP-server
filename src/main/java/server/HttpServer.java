package server;

import org.eclipse.jetty.server.Server;
import util.TimeStrings;

import java.io.File;

/**
 * Main method starting the HTTP server
 * Accepts HTTP POST requests on port 8081
 */
public class HttpServer
{
    public static void main(String[] args) throws Exception
    {
        Server server = new Server(8081);
        server.setHandler(new RequestHandler());
        server.start();
        server.join();
    }
}
