package server;

import edu.cmu.sphinx.linguist.dictionary.Word;
import edu.cmu.sphinx.result.WordResult;
import exceptions.OperationFailedException;
import util.FileManager;
import util.json.JSONObject;
import util.json.JSONPair;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 */
public class Session
{
    private ArrayList<File> files;

    private AudioTranscriber transcriber;

    private FileManager fileManager;

    private String id;

    private Transcript transcript;

    private File lastFile;

    public Session(String id)
    {
        this.files = new ArrayList<>();
        this.transcriber = new AudioTranscriber();
        this.fileManager = FileManager.getInstance();
        this.id = id;
        this.transcript = new Transcript();
    }

    public JSONObject transcribe(File audioFile)
            throws IOException, OperationFailedException
    {
        System.out.println("transcribe in Session got called");
        ArrayList<WordResult> results = internalTranscribe(audioFile);
        JSONObject toReturn = new JSONObject();
        for(WordResult result : results)
        {
            Word word = result.getWord();
            if(!word.isFiller())
            {
                toReturn.addPair(new JSONPair(
                        result.getTimeFrame().toString(),
                        word.toString()));
            }
        }

        return toReturn;
    }

    private ArrayList<WordResult> internalTranscribe(File audioFile)
            throws IOException, OperationFailedException
    {
        //merge new file with old file to get better results
        File toTranscribe;
        if(lastFile == null)
        {
            //"merge" to create a new file which can be deleted
            //at the end of this method
            System.out.println("merging one file");
            toTranscribe = AudioFileManipulator.
                    mergeWAVFiles(audioFile);
            System.out.println("done merging");
        }
        else
        {
            System.out.println("merging two files");
            toTranscribe = AudioFileManipulator.
                    mergeWAVFiles(lastFile, audioFile);
            System.out.println("done merging");
        }

        System.out.println("Starting transcription in session");
        ArrayList<WordResult> text = transcriber.transcribe(toTranscribe);
        transcript.addWordList(text);

        //delete merged file and save the new one
        lastFile = audioFile;
        fileManager.disposeFiles(toTranscribe);

        return text;
    }

    public Transcript getMostRecentTranscript()
    {
        return null;
    }

    public Transcript getWholeTranscript()
    {
        return null;
    }

    public String getId()
    {
        return this.id;
    }
}
