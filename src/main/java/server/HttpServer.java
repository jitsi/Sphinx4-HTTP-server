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
        int port = 8081;
        if(args.length >= 1)
        {
            port = Integer.parseInt(args[0]);
        }
        Server server = new Server(port);
        server.setHandler(new RequestHandler());
        server.start();
        server.join();
    }
}
