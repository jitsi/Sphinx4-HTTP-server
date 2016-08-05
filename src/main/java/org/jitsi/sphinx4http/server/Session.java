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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Class representing a session for repeated audio files from the same voice.
 *
 * note:
 * Used to merge and store previous files to increase accuracy of transcriptions
 * but was deemed impractical because of bad run time performance.
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
     * transcribed the given audio file and returns it in a JSON format
     * where each word is a JSON object with a timestamp and an identifier words
     * which end a sentence
     * @param audioFile the audio file to transcribe
     * @return JSON array with every uttered word in the audio file
     * @throws IOException when the audio file cannot be read
     */
    public JSONArray transcribe(File audioFile)
            throws IOException
    {
        logger.trace("transcribing audio file with id: {}", id);
        try(InputStream stream = new FileInputStream(audioFile))
        {
            ArrayList<WordResult> results = transcriber.transcribe(stream);
            return builder.buildSpeechToTextResult(results);
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
