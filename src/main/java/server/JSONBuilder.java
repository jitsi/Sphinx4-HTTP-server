package server;

import edu.cmu.sphinx.linguist.dictionary.Word;
import edu.cmu.sphinx.result.WordResult;
import util.json.JSONArray;
import util.json.JSONObject;
import util.json.JSONPair;

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

    public JSONArray buildSpeechToTextResult(ArrayList<WordResult> results)
    {
        JSONArray toReturn = new JSONArray();
        for(WordResult result : results)
        {
            JSONObject word = new JSONObject();
            //add word
            word.addPair(new JSONPair(JSON_WORD, result.getWord().toString()));
            //add start timestamp
            word.addPair(new JSONPair(JSON_TIMESTAMP_START,
                    result.getTimeFrame().getStart()));
            //add end timestamp
            word.addPair(new JSONPair(JSON_TIMESTAMP_END,
                    result.getTimeFrame().getEnd()));
            //add if word is filler
            word.addPair(new JSONPair(JSON_FILL_WORD,
                    result.getWord().isFiller()));

            toReturn.addValue(word);
        }

        return toReturn;
    }
}
