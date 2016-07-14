package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by workingnik on 14/07/16.
 */
public class StreamEater extends Thread
{
    private InputStream stream;
    private String name;
    private boolean print;

    public StreamEater(InputStream stream, String streamName)
    {
        this(stream, streamName, false);
    }

    public StreamEater(InputStream stream, String streamName,  boolean print)
    {
        this.stream = stream;
        this.name = streamName;
        this.print = print;
    }

    public void run()
    {
        try
        {
            InputStreamReader streamReader = new InputStreamReader(stream);
            BufferedReader br = new BufferedReader(streamReader);

            String line = null;
            while( (line = br.readLine()) != null)
            {
                if(print)
                {
                    System.out.println(name + ":" + line);
                }
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
