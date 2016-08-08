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

package org.jitsi.sphinx4http.util;

import org.jitsi.sphinx4http.exceptions.InvalidDirectoryException;
import org.jitsi.sphinx4http.exceptions.NotInDirectoryException;
import org.jitsi.sphinx4http.server.ServerConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Manages a directory and creation of unique file names so IOExceptions
 * while reading and writing the audio files for speech recognition are
 * less likely to occur.
 *
 * @author Nik Vaessen
 */
public class FileManager
{
    /**
     * Logger of this class
     */
    private static final Logger logger =
            LoggerFactory.getLogger(FileManager.class);

    /**
     * Name of the main directory
     * Defaults to "data/" when not specified in config file
     */
    private static final String MAIN_DIR = ServerConfiguration.
            getInstance().getDataFolderPath();

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
    private static FileManager fileManager = new FileManager();

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
    private static AtomicInteger tag;

    /**
     * Sets the tag to 0 and creates the directory where the files
     * will be stored, as well as deleting all files in them
     * and making a thread which deletes all files from the disposed
     * directory every 5 minutes
     */
    private FileManager()
    {
        tag = new AtomicInteger(0);
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
        return fileManager;
    }

    /**
     * Finds out in which directory, managed by this class, a file is located
     * @param file the file to get the directory from
     * @return A string of the name of the directory, equal to one of the
     * constant strings from this class
     * @throws NotInDirectoryException when the file is not in any of the
     * directories manged by this class
     */
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
        return tag.getAndIncrement() + "_" + TimeStrings.getNowString();
    }

    /**
     * Internal method to get the File object associated with the constant
     * string representing a directory managed by the class
     * @param dir One of the constant strings representing a directory
     *            managed by this class
     * @return the File object of the directory
     * @throws InvalidDirectoryException when the input is not one of the
     * constant directory strings of this class
     */
    private File getDirectory(String dir)
        throws InvalidDirectoryException
    {
        if(dir.equals(MAIN_DIR))
        {
            return mainDir;
        }
        else if(dir.equals(INCOMING_DIR))
        {
            return incomingDir;
        }
        else if(dir.equals(CONVERTED_DIR))
        {
            return convertedDir;
        }
        else if(dir.equals(DISPOSED_DIR))
        {
            return disposedDir;
        }
        else
        {
            throw new InvalidDirectoryException(dir + " is not directory" +
                    " managed by the FileManager");
        }
    }

    /**
     * Get a new potential File object with a unique name in one of the
     * specified directories
     * @param directory the name of the directory in which the file should
     *                  be located
     * @return a File representing a potential new file in the specified
     * directory which is safe to write to
     * @throws InvalidDirectoryException when the specified directory is not
     * one of the directories managed by this class
     */
    public File getNewFile(String directory)
        throws InvalidDirectoryException
    {
        return getNewFile(directory, "");
    }

    /**
     * Get a new potential File object with a unique name in one of the
     * specified directories
     * @param directory the name of the directory in which the file should be
     *                  located
     * @param name part of the name of the new file trailing the unique
     *             tag
     * @return A file representing a potential new file in the specified
     * directory which is safe to write to
     * @throws InvalidDirectoryException when the specified directory is not
     * one of the directories managed by this class
     */
    public File getNewFile(String directory, String name)
            throws InvalidDirectoryException
    {
        File dir = getDirectory(directory);
        return new File(dir.getAbsolutePath() + File.separator +
                getTag() + name);
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
                    }
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                    if(disposedDir.isDirectory())//to prevent null pointer
                    {
                        for(File file : disposedDir.listFiles())
                        {
                            if(!file.delete())
                            {
                                logger.warn("could not delete {} from " +
                                        "the the disposed directory",
                                        file.getPath());
                            }
                        }
                    }
            }
            }
        }).start();
    }
}
