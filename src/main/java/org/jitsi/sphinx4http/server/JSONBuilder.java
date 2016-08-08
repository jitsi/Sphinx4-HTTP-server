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
 *
 * @author Nik Vaessen
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
    @SuppressWarnings("unchecked") //for JSONArray.add()
    public JSONArray buildSpeechToTextResult(ArrayList<WordResult> results)
    {
        JSONArray toReturn = new JSONArray();
        for(WordResult result : results)
        {
            toReturn.add(this.buildWordObject(
                    result.getWord().toString(),
                    result.getTimeFrame().getStart(),
                    result.getTimeFrame().getEnd(),
                    result.getWord().isFiller()
            ));
        }
        return toReturn;
    }

    /**
     * Create a JSONObject with a word, start, end and filler value based
     * on a WordResult
     * @param result the WordResult whose values will be held in the JSONObject
     * @return a JSONObject holding the word, start, end and filler
     * values of the given WordResult
     */
    public JSONObject buildWordObject(WordResult result)
    {
        return buildWordObject(
                result.getWord().toString(),
                result.getTimeFrame().getStart(),
                result.getTimeFrame().getEnd(),
                result.getWord().isFiller());
    }

    /**
     * Create a JSONObject with a word, start, end and filler value based
     * on a WordResult
     * @param word the word value of the JSONObject
     * @param start  the start value of the JSONObject
     * @param end the end value of the JSONObject
     * @param filler the filler value of the JSONObject
     * @return a JSONObject holding the word, start, end and filler
     * values of the given WordResult
     */
    @SuppressWarnings("unchecked") //for JSONObject.put()
    public JSONObject buildWordObject(String word, long start,
                                      long end, boolean filler)
    {
        JSONObject jsonWord = new JSONObject();
        jsonWord.put(JSON_WORD, word);
        jsonWord.put(JSON_TIMESTAMP_START, start);
        jsonWord.put(JSON_TIMESTAMP_END, end);
        jsonWord.put(JSON_FILL_WORD, filler);
        return jsonWord;
    }
}
