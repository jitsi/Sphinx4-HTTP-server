package server;

import edu.cmu.sphinx.linguist.dictionary.Word;
import edu.cmu.sphinx.result.WordResult;
import exceptions.OperationFailedException;
import util.FileManager;
import util.json.JSONArray;
import util.json.JSONObject;
import util.json.JSONPair;

import java.io.File;
import java.io.IOException;
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
     * @throws IOException
     */
    public JSONArray transcribe(File audioFile)
            throws IOException
    {
        System.out.println("transcribe in Session got called");
        ArrayList<WordResult> results = transcriber.transcribe(audioFile);
        JSONArray toReturn = new JSONArray();
        for(WordResult result : results)
        {
            Word word = result.getWord();
            JSONObject wordObject = new JSONObject();
            if(!word.isFiller())
            {
                wordObject.addPair(new JSONPair(
                        result.getTimeFrame().toString(),
                        word.toString()));
                if(word.isSentenceEndWord())
                {
                    wordObject.addPair(new JSONPair("sentenceEnd", true));
                }
            }
            toReturn.addValue(wordObject);
        }

        return toReturn;
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
