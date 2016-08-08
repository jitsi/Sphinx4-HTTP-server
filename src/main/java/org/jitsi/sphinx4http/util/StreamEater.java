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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * This class is used to consume from streams so that the Process API does not
 * block when the buffer from the Error- and OutputStream gets full.
 *
 * @author Nik Vaessen
 */
public class StreamEater
{
    /**
     * Logger of this class
     */
    private static final Logger logger =
            LoggerFactory.getLogger(StreamEater.class);

    /**
     * The stream being read from
     */
    private InputStream stream;
    /**
     * Arbitrary name of the stream used to clarify in the console
     * from which stream the output is
     */
    private String name;

    /**
     * Whether to log the content of the stream. Defaults to false
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
     * @param print whether the content of the stream will be logged
     */
    public StreamEater(InputStream stream, String streamName, boolean print)
    {
        this.stream = stream;
        this.name = streamName;
        this.print = print;
        run();
    }

    /**
     * Consumes from the given stream. Will not print the content
     * as long as print will be false.
     */
    private void run()
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try(InputStreamReader streamReader =
                            new InputStreamReader(stream);
                    BufferedReader br = new BufferedReader(streamReader))
                {
                    String line = null;
                    while ((line = br.readLine()) != null)
                    {
                        if (print)
                        {
                            logger.debug(name + ":" + line);
                        }
                    }
                    br.close();
                    streamReader.close();
                    stream.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}

