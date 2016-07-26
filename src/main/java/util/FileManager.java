package util;

import exceptions.InvalidDirectoryException;
import exceptions.NotInDirectoryException;

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
    private static final String MAIN_DIR = "data/";

    /**
     * Name of the directory where initially retrieved files are stored in
     * before they are converted to the right format
     */
    public static final String INCOMING_DIR = MAIN_DIR + "incoming/";

    /**
     * Name of the directory where converted files are stored
     */
    public static final String CONVERTED_DIR = MAIN_DIR + "converted/";

    /**
     * Name of the directory where the files are moved to once the speech-
     * recognition is done with the audio files and no longer needs them.
     */
    private static final String DISPOSED_DIR = MAIN_DIR + "disposed/";

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

    // FIXME: 25/07/16 actually get the directory
    public String getDirectory(File file)
        throws NotInDirectoryException
    {
        String path = file.getAbsolutePath();
        if(path.contains(MAIN_DIR))
        {
            if(path.contains(INCOMING_DIR))
            {
                return INCOMING_DIR;
            }
            else if(path.contains(CONVERTED_DIR))
            {
                return CONVERTED_DIR;
            }
            else if(path.contains(DISPOSED_DIR))
            {
                return DISPOSED_DIR;
            }
            else
            {
                return MAIN_DIR;
            }
        }
        else
        {
            throw new NotInDirectoryException();
        }
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

    private File getDirectory(String dir)
        throws InvalidDirectoryException
    {
        switch (dir)
        {
            case MAIN_DIR:
                return mainDir;
            case INCOMING_DIR:
                return incomingDir;
            case CONVERTED_DIR:
                return convertedDir;
            case DISPOSED_DIR:
                return disposedDir;
            default:
                throw new InvalidDirectoryException(dir + " is not directory" +
                        " managed by the FileManager");
        }
    }

    public File getNewFile(String directory)
        throws InvalidDirectoryException
    {
        return getNewFile(directory, "");
    }

    public File getNewFile(String directory, String name)
            throws InvalidDirectoryException
    {
        File dir = getDirectory(directory);
        return new File(dir.getAbsolutePath() + File.separator + getTag() + name);
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
                try
                {
                    boolean success = file.renameTo(
                            new File(getNewFile(FileManager.DISPOSED_DIR,
                                    file.getName()).getAbsolutePath()));
                    if(!success)
                    {
                        file.delete();
                    }
                }
                catch (InvalidDirectoryException e)
                {
                    e.printStackTrace();
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
        mainDir = new File(MAIN_DIR);
        mainDir.mkdir();
        //check if the directory "data/incoming" exists
        incomingDir = new File(INCOMING_DIR);
        incomingDir.mkdir();
        //check if the directory "data/converted" exists
        convertedDir = new File(CONVERTED_DIR);
        convertedDir.mkdir();
        //check if the directory "data/transcribed" exists
        disposedDir = new File(DISPOSED_DIR);
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
