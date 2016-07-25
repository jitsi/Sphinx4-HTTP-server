package server;

import edu.cmu.sphinx.result.WordResult;
import exceptions.OperationFailedException;
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

    public Session(String id)
    {
        this.files = new ArrayList<>();
        this.transcriber = new AudioTranscriber();
        this.fileManager = FileManager.getInstance();
        this.id = id;
        this.transcript = new Transcript();

        //

    }

    private synchronized boolean transcribeAudioFile(File audioFile)
    {
        try
        {
            //merge new file with old file to get better results
            File toTranscribe;
            if(lastFile == null)
            {
                //"merge" to create a new file which can be deleted
                //at the end of this method
                toTranscribe = AudioFileManipulator.
                        mergeWAVFiles(audioFile);
            }
            else
            {
                toTranscribe = AudioFileManipulator.
                        mergeWAVFiles(lastFile, audioFile);
            }

            ArrayList<WordResult> text = transcriber.transcribe(toTranscribe);
            transcript.addWordList(text);

            //delete merged file and save the new one
            lastFile = audioFile;
            fileManager.disposeFiles(toTranscribe);

            return true;
        }
        catch (IOException | OperationFailedException e)
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
