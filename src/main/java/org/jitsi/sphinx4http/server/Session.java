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

package org.jitsi.sphinx4http.server;


import edu.cmu.sphinx.result.WordResult;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.concurrent.SynchronousQueue;

/**
 * Class representing a session for repeated audio files from the same voice.
 *
 * note:
 * Used to merge and store previous files to increase accuracy of transcriptions
 * but was deemed impractical because of bad run time performance. It is kept
 * for wrapping the AudioTranscriber class
 *
 * @author Nik Vaessen
 */
public class Session
{
    /**
     * The logger of this class
     */
    private static final Logger logger = LoggerFactory.getLogger(Session.class);

    /**
     * Class that formats the output of the speech-to-text to a JSONArray
     */
    private static JSONBuilder builder = new JSONBuilder();
    /**
     * class doing the speech-to-text on the given auduo file
     */
    private AudioTranscriber transcriber;

    /**
     * ID of the session
     */
    private String id;

    /**
     * Creates a session object with a unique ID
     * @param id the id of the new session
     */
    public Session(String id)
    {
        this.transcriber = new AudioTranscriber();
        this.id = id;
    }

    /**
     * transcribes the given audio file and returns it in a JSON format
     * where each word is a JSON object with values 'word, start, end,
     * filler'.
     *
     * @param audioFile the audio file to transcribe
     * @return JSON array with every uttered word in the audio file
     * @throws IOException when the audio file cannot be read
     */
    public JSONArray transcribe(File audioFile)
            throws IOException
    {
        logger.trace("transcribing audio file with id: {}", id);
        try(InputStream in = new FileInputStream(audioFile))
        {
            ArrayList<WordResult> results = transcriber.transcribe(in);
            return builder.buildSpeechToTextResult(results);
        }
    }


    /**
     * transcribe the given audio file and send each retrieved word back
     * immediately. The word will be a JSON object with values, word, start,
     * end, filler'.
     * @param audioFile the audio file to transcribe
     * @param out the outputstream to write each word results to immediately
     * @return JSON array with every uttered word in the audio file
     */
    public JSONArray chunkedTranscribe(File audioFile, PrintWriter out)
        throws IOException
    {
        logger.trace("started chunked transcribing of " +
                "audio file with id : {}", id);

        try(InputStream in = new FileInputStream(audioFile))
        {
            // create a thread to immediately get the word result out
            // of the synchronousQueue
            final SynchronousQueue<WordResult> results
                    = new SynchronousQueue<>();
            final ArrayList<WordResult> storedResults = new ArrayList<>();
            //make sure the printwriter does not close because it's needed
            //else where to finish the object
            final PrintWriter printWriter = new PrintWriter(out);
            Thread queueManager = new Thread(new Runnable()
            {
                @Override
                public void run()
                {
                    //listen for the first word outside of the loop
                    //to prevent a trailing "," at the end of the transcription
                    //json array
                    try
                    {
                        WordResult word = results.take();
                        logger.trace("retrieved word outside loop\"{}\"",
                                word.toString());
                        storedResults.add(word);
                        JSONObject toSend = builder.buildWordObject(word);
                        printWriter.write("," + toSend.toJSONString());
                        printWriter.flush();
                    }
                    catch (InterruptedException e)
                    {
                        Thread.currentThread().interrupt();
                    }

                    while(!Thread.currentThread().isInterrupted())
                    {
                        try
                        {
                            //blocks until result is retrieved
                            WordResult word = results.take();
                            logger.trace("retrieved word \"{}\"",
                                    word.toString());
                            storedResults.add(word);
                            JSONObject toSend = builder.buildWordObject(word);
                            printWriter.write("," + toSend.toJSONString());
                            printWriter.flush();
                        }
                        catch (InterruptedException e)
                        {
                            //make sure the thread ends
                            Thread.currentThread().interrupt();
                        }
                    }
                }
            });
            queueManager.start();
            transcriber.transcribeSynchronous(in, results);
            //stop the thread as the transcribing is done
            queueManager.interrupt();

            return builder.buildSpeechToTextResult(storedResults);
        }
    }

    /**
     * Get the ID of the session
     * @return the ID of the session
     */
    public String getId()
    {
        return this.id;
    }
}
