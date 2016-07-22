package util;

import java.io.Externalizable;
import java.io.File;

/**
 * Manages a directory and creation of unique file names so IOExceptions
 * while reading and writing the audio files for speech recognition are
 * less likely to occur.
 */
public class FileManager
{
    /**
     * Name of the main directory
     */
    private static final String MAIN_DIRECTORY = "data/";

    /**
     * Name of the directory where initially retrieved files are stored in
     * before they are converted to the right format
     */
    private static final String INCOMING = MAIN_DIRECTORY + "incoming/";

    /**
     * Name of the directory where converted files are stored
     */
    private static final String CONVERTED = MAIN_DIRECTORY + "converted/";

    /**
     * Name of the directory where the files are moved to once the speech-
     * recognition is done with the audio files and no longer needs them.
     */
    private static final String DISPOSED = MAIN_DIRECTORY + "disposed/";

    /**
     * Singleton object of this FileManager class
     */
    private static FileManager fileManager = null;

    /**
     * The main directory
     */
    private File mainDir;

    /**
     * The incoming file's directory
     */
    private File incomingDir;

    /**
     * The converted file's directory
     */
    private File convertedDir;

    /**
     * The disposed file's directory
     */
    private File disposedDir;

    /**
     * tag that gets added the every file name. The tag gets incremented every
     * time a new file is created so that all file names will be unique
     */
    private static int tag;

    /**
     * Sets the tag to 0 and creates the directory where the files
     * will be stored, as well as deleting all files in them
     * and making a thread which deletes all files from the disposed
     * directory every 5 minutes
     */
    private FileManager()
    {
        tag = 0;
        checkDirectoryExistence();
        cleanDirectories();
        createDisposedDirectoryCleaner();
    }

    /**
     * Gets the singleton object of this FileManager class
     * @return the singleton object representing this FileManager class
     */
    public static FileManager getInstance()
    {
        if(fileManager == null)
        {
            fileManager = new FileManager();
        }
        return fileManager;
    }

    /**
     * A tag with a unique integer and the rough time and date of the methods
     * execution
     * @return the unique tag
     */
    private static String getTag()
    {
        tag++;
        if(tag < 0)
        {
            tag = 0;
        }
        return tag + "_" + TimeStrings.getNowString();
    }

    /**
     * Gets a new empty file in the incoming directory which a new audio file
     * can be written to
     * @param contentType the content type of the new file such that the
     *                    right file extension can be used. Expected to be
     *                    in the form "audio/xxxx"
     * @return a new empty file for writing the retrieved audio file to
     */
    public File getNewIncomingFile(String contentType)
    {
        String fileExtension = "." + contentType.split("/")[1];
        return new File(INCOMING + getTag() + fileExtension);
    }

    /**
     * Gets a path for a new file in the converted file's directory
     * @param fileExtension the file extension of the new file
     * @return a path to the converted file's directory including a unique
     * file name
     */
    public String getNewConvertedFilePath(String fileExtension)
    {
        return CONVERTED + getTag() + fileExtension;
    }

    /**
     * Gets a path for a new file in the disposed file's directory
     * @param fileName the name of the file
     * @return the path to the disposed file's directory including the name
     * of the file being placed there
     */
    public String getNewDisposedFilePath(String fileName)
    {
        return DISPOSED + fileName;
    }

    /**
     * create a new empty file in the main directory
     * @param fileExtension the file extension of the new file
     * @return a new empty file
     */
    public File getNewFile(String fileExtension)
    {
        return new File(MAIN_DIRECTORY + getTag() + fileExtension);
    }

    /**
     * create a new empty file in the main directory
     * @return a new empty file
     */
    public File getNewFile()
    {
        return getNewFile("");
    }

    /**
     * Puts all the file given as arguments in the disposed file's directory.
     * If this fails, the files are instead deleted
     * @param files all files needed to be moved the disposed directory
     */
    public void disposeFiles(File... files)
    {
        for(File file : files)
        {
            if(file.exists())
            {
                boolean success = file.renameTo(
                        new File(getNewDisposedFilePath(file.getName())));
                if(!success)
                {
                    file.delete();
                }
            }
        }
    }

    /**
     * If the directories do not yet exist in the file system,
     * they will be created.
     */
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
        disposedDir = new File(DISPOSED);
        disposedDir.mkdir();
    }

    /**
     * Removes all files in all subdirectories
     */
    private void cleanDirectories()
    {
        //delete every file in the incoming directory
        if(incomingDir.isDirectory()) // to prevent null pointers
        {
            for(File file : incomingDir.listFiles())
            {
                file.delete();
            }
        }
        else
        {
            new Exception("could not delete files from incoming directory")
                    .printStackTrace();
        }
        //delete every file in the converted directory
        if(convertedDir.isDirectory()) // to prevent null pointers
        {
            for(File file : convertedDir.listFiles())
            {
                file.delete();
            }
        }
        else
        {
            new Exception("could not delete files from converted directory")
                    .printStackTrace();
        }
        //delete every file in the disposed directory
        if(disposedDir.isDirectory()) // to prevent null pointers
        {
            for(File file : disposedDir.listFiles())
            {
                file.delete();
            }
        }
        else
        {
            new Exception("could not delete files from disposed directory")
                    .printStackTrace();
        }
    }

    /**
     * Creates a thread which removes every file from the
     * disposed directory every 5 minutes
     */
    private void createDisposedDirectoryCleaner()
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                while(true)
                {
                    try
                    {
                        Thread.sleep(60 * 5 * 1000);
                        if(disposedDir.isDirectory())//to prevent null pointer
                        {
                            for(File file : disposedDir.listFiles())
                            {
                                file.delete();
                            }
                        }
                        else
                        {
                            throw new Exception("could not delete files" +
                                    " from disposed directory");
                        }
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
}
