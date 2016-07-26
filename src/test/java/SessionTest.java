import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.client.api.Response;
import org.eclipse.jetty.client.api.Result;
import org.eclipse.jetty.http.HttpMethod;
import server.HttpServer;
import util.SessionManager;

import java.io.File;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;


/**
 *
 */
public class SessionTest
{

    private static String uri = "http://localhost:8081/recognize";

    public static void main(String[] args) throws Exception
    {
//        File file = TestFiles.TEST_FILE;
//        File[] chunks = TestFiles.TEST_FILE_CHUNKS;
//
//        new Thread(new Runnable()
//        {
//            @Override
//            public void run()
//            {
//                try
//                {
//                    HttpServer.main(new String[] {"hi"});
//                }
//                catch (Exception e)
//                {
//                    e.printStackTrace();
//                }
//            }
//        }).start();
//
//        try
//        {
//            Thread.sleep(1000);
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//        }

        HttpClient httpClient = new HttpClient();
        //httpClient.setFollowRedirects(true);
        httpClient.start();

        System.out.println("sending request");
        httpClient.newRequest("http://www.google.com")
                .timeout(5, TimeUnit.SECONDS)
                .send(new Response.CompleteListener()
                {
                    @Override
                    public void onComplete(Result result)
                    {
                        System.out.println("result: " + result);
                    }
                });
        System.out.println("done sending");

//        ContentResponse response = httpClient.newRequest(uri)
//                .method(HttpMethod.POST)
//                .file(Paths.get(chunks[0].getAbsolutePath()), "audio/wav")
//                .timeout(5, TimeUnit.SECONDS)
//                .send();
    }
}
