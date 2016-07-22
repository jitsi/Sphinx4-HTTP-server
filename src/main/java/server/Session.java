package server;

import util.FileManager;

import java.io.File;
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

    public Session(String id)
    {
        this.files = new ArrayList<>();
        this.transcriber = new AudioTranscriber();
        this.fileManager = FileManager.getInstance();
        this.id = id;
        this.transcript = new Transcript();
    }

    public void addAudioFile()
    {

    }

    public Transcript getMostRecentTranscript()
    {

    }

    public Transcript getWholeTranscript()
    {

    }

    public String getId()
    {
        return this.id;
    }
}
