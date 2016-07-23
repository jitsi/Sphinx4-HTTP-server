package server;

import edu.cmu.sphinx.result.WordResult;
import util.FileManager;

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

    public Session(String id, File initialAudioFile)
    {
        this.files = new ArrayList<>();
        this.transcriber = new AudioTranscriber();
        this.fileManager = FileManager.getInstance();
        this.id = id;
        this.transcript = new Transcript();

        //

    }

    private synchronized boolean (File audioFile)
    {
        try
        {
            //merge new file with old file to get better results
            File toTranscribe = AudioFileManipulator.
                        mergeWAVFiles(lastFile, audioFile);

            ArrayList<WordResult> text = transcriber.transcribe(toTranscribe);
            transcript.addWordList(text);

            //delete merged file and save the new one
            lastFile = audioFile;
            toTranscribe.delete();

            return true;
        }
        catch (IOException e)
        {
            return false;
        }
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
