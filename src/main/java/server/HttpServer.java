package server;

import org.eclipse.jetty.server.Server;
import util.TimeStrings;

import java.io.File;

/**
 * Created by workingnik on 13/07/16.
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
