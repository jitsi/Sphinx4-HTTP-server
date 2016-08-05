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

import java.util.ArrayList;

/**
 * Constructs the output of the speech-to-text in JSON format
 */
public class JSONBuilder
{
    /**
     * Identifier for the word
     */
    private static final String JSON_WORD = "word";

    /**
     * Identifier for the time when the word started to get spoken
     */
    private static final String JSON_TIMESTAMP_START = "start";

    /**
     * Identifier for the time when the word stopped being spoken
     */
    private static final String JSON_TIMESTAMP_END = "end";

    /**
     * identifier that the word is a fill word, e.g a sigh
     */
    private static final String JSON_FILL_WORD = "filler";

    /**
     * Builds the array of words from the Sphinx4 into a JSON array
     * @param results the ArrayList of WordResults from a Speech-to-text
     *                transcription
     * @return a JSONArray holding a JSONObject for each word, with additional
     * information
     *
     */
    public JSONArray buildSpeechToTextResult(ArrayList<WordResult> results)
    {
        JSONArray toReturn = new JSONArray();
        for(WordResult result : results)
        {
            JSONObject word = new JSONObject();
            //add word
            word.put(JSON_WORD, result.getWord().toString());
            //add start timestamp
            word.put(JSON_TIMESTAMP_START, result.getTimeFrame().getStart());
            //add end timestamp
            word.put(JSON_TIMESTAMP_END, result.getTimeFrame().getEnd());
            //add if word is filler
            word.put(JSON_FILL_WORD, result.getWord().isFiller());

            toReturn.add(word);
        }

        return toReturn;
    }
}
