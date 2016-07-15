package util;

import java.io.*;

/**
 * This class is used to consume from streams so that the Process API does not
 * block when the buffer from the Error- and OutputStream gets full.
 */
public class StreamEater extends Thread
{
    /**
     * The stream being read from
     */
    private InputStream stream;
    /**
     * Arbitrary name of the stream used to clarify in the console
     * from which stream the output it
     */
    private String name;

    /**
     * Whether to print the content of the stream. Defaults to false
     */
    private boolean print;

    /**
     * Creates a new thread consuming from the specified stream. Will not
     * print the content of the streams
     * @param stream the stream being consumed
     * @param streamName arbitrary name for the stream
     */
    public StreamEater(InputStream stream, String streamName)
    {
        this(stream, streamName, false);
    }

    /**
     * Creates a new thread consuming for the specified stream
     * @param stream the stream being consumed
     * @param streamName arbitrary name for the stream
     * @param print whether the content of the stream will be printed to
     *              standard out
     */
    public StreamEater(InputStream stream, String streamName,  boolean print)
    {
        this.stream = stream;
        this.name = streamName;
        this.print = print;
    }

    /**
     * Consumes from the given stream. Will not print the content
     * as long as print will be false.
     */
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
