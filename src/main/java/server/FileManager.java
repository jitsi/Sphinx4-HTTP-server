package server;

import util.TimeStrings;

import java.io.File;

/**
 * Created by workingnik on 14/07/16.
 */
public class FileManager
{
    private static final String MAIN_DIRECTORY = "data/";
    private static final String INCOMING = MAIN_DIRECTORY + "incoming/";
    private static final String CONVERTED = MAIN_DIRECTORY + "converted/";
    private static final String TRANSCRIBED = MAIN_DIRECTORY + "transcribed/";


    private static FileManager fileManager = null;

    private File mainDir;
    private File incomingDir;
    private File convertedDir;
    private File transcribedDir;

    private static int tag;

    public static FileManager getInstance()
    {
        if(fileManager == null)
        {
            fileManager = new FileManager();
            tag = 0;
        }
        return fileManager;
    }

    private FileManager()
    {

        checkDirectoryExistence();
    }

    private void checkDirectoryExistence()
    {
        //check if the directory "data/" exists
        mainDir = new File(MAIN_DIRECTORY);
        mainDir.mkdir();
        //check if the directory "data/incoming" exists
        incomingDir = new File(INCOMING);
        incomingDir.mkdir();
        //check if the directory "data/converted" exists
        convertedDir = new File(CONVERTED);
        convertedDir.mkdir();
        //check if the directory "data/transcribed" exists
        transcribedDir = new File(TRANSCRIBED);
        transcribedDir.mkdir();
    }

    public File getNewIncomingFile(String contentType)
    {
        String fileExtension = "." + contentType.split("/")[1];
        return new File(INCOMING + getTag() + fileExtension);
    }

    /**
     *
     * @return
     */
    public String getNewConvertedFilePath(String fileExtension)
    {
        return CONVERTED + getTag() + fileExtension;
    }
//
//    public File getNewTranscribedFile()
//    {
//
//    }
//
//    public File getNewDisposedFile()
//    {
//
//    }

    private static String getTag()
    {
        tag++;
        if(tag < 0)
        {
            tag = 0;
        }
        return tag + "_" + TimeStrings.getNowString();
    }
}
