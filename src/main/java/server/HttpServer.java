package server;

import org.eclipse.jetty.server.Server;

/**
 * Main method starting the HTTP server
 * Accepts HTTP POST requests on port 8081
 */
public class HttpServer
{
    public static void main(String[] args) throws Exception
    {
        int port = 8081;
        if (args.length >= 1)
        {
            try
            {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException e)
            {
                System.out.println(args[0] + " is not a valid port. Using " +
                        port + " instead");
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
            e.printStackTrace();
            System.out.flush();
            System.out.println("Something went wrong while starting the " +
                    "server. Is the port " + port + " already in use?");
            System.exit(1);
        }
    }
}
