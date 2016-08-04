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
package org.jitsi.sphinx4http.server;

import org.jitsi.sphinx4http.exceptions.InvalidDirectoryException;
import org.jitsi.sphinx4http.exceptions.NotInDirectoryException;
import org.jitsi.sphinx4http.exceptions.OperationFailedException;
import org.jitsi.sphinx4http.util.FileManager;
import org.jitsi.sphinx4http.util.StreamEater;
import java.io.*;

/**
 * Class holds methods for the tasks below related to audio files. The tasks
 * are completed by the external program "ffmpeg", which needs to be installed
 * on the device. more info on ffmpeg:
 *      https://ffmpeg.org/download.html#get-sources
 * The ffpmeg version needs to be version >= 1.1
 *
 * Tasks:
 *  - Convert a given audio file to the correct format required by Sphinx4
 *  - Merge multiple audio files
 */
public class AudioFileManipulator
{
    /**
     * The path to the ffmpeg executable, version >= 1.1
     */
    private final static String PROGRAM = "/usr/bin/ffmpeg";

    /**
     * Command to to convert an audio file to
     * .wav format with mono sound and a KHz of 16000
     * "-i %s" -  the file to convert, where %s needs to be formatted in
     * "-acodec pcm_s16le" - convert to raw pcm for .wav file
     * "-ac 1" - convert to one audio chanel (mono sound)
     * "-ar 16000" - convert to 16000 KHz
     * "%s" the file to convert to, where %s needs to be formatted in
     */
    private final static String CONVERT_COMMAND =
        PROGRAM + " -i %s -acodec pcm_s16le -ac 1 -ar 16000 %s";

    /**
     * Command to merge 2 or more .wav file.
     * Requires a .txt file with the paths of all files with the structure:
     *          #mylist.txt
     *          file 'path/to/file'\n
     *          file 'second/file'\n
     * The first %s contains the path to the .txt file
     * The second %s contains the path to the output file
     */
    private final static String MERGE_COMMAND =
            PROGRAM + " -f concat -i %s -c copy %s";

    /**
     * whether the output of the commands get logged
     */
    public static boolean OUTPUT = false;

    /**
     * The file manager
     */
    private static FileManager fileManager = FileManager.getInstance();

    /**
     * Converts the given file to .wav format with a sampling rate of
     * 16 kHz and mono audio
     * @param toConvert the file to convert
     * @param newFilePath the path where the converted file should be
     * @throws OperationFailedException when converting fails
     */
    public static File convertToWAV(File toConvert, String newFilePath)
        throws OperationFailedException
    {
        //build the correct command string and return the converted file
        String formattedCommand = String.format(CONVERT_COMMAND,
                toConvert.getAbsolutePath(), newFilePath);
        try
        {
            return runCommand(formattedCommand, newFilePath);
        }
        catch (IOException | InterruptedException e)
        {
            throw new OperationFailedException(e.getMessage());
        }
    }

    /**
     * Merges the given files. The program relies on an .txt file containing
     * the files names to merge. All files need to be located in the
     * same folder. The new merged file will be placed in the this folder.
     * @param toMerge all the files to merge
     * @return one audio file containing the merged input files
     * @throws OperationFailedException when merging fails
     */
    public static File mergeWAVFiles(File... toMerge)
        throws OperationFailedException
    {
        //check for invalid inputs
        if(toMerge.length == 0)
        {
            throw new OperationFailedException("cannot merge zero files");
        }

        //make sure all files to merge are in the same directory
        String directory;
        try
        {
            directory = fileManager.getDirectory(toMerge[0]);
            for (int i = 1; i < toMerge.length; i++)
            {
                if (!fileManager.getDirectory(toMerge[i]).equals(directory))
                {
                    throw new NotInDirectoryException();
                }
            }
        }
        catch (NotInDirectoryException e)
        {
            throw new OperationFailedException("all files to merge need" +
                            " to be in the same folder");
        }

        //create the .txt file
        File text;
        try
        {
            text = FileManager.getInstance().getNewFile(directory, ".txt");
        }
        catch (InvalidDirectoryException e)
        {
            throw new OperationFailedException("couldn't make text file");
        }

        try(PrintWriter printWriter =
                    new PrintWriter(new FileOutputStream(text)))
        {
            for(File file : toMerge)
            {
                printWriter.printf("file '%s'\n", file.getName());
            }
            printWriter.flush();
        }
        catch (FileNotFoundException e)
        {
            throw new OperationFailedException("Couldn't write to the" +
                    "merged .txt file");
        }

        //format the command and return the merged file
        String mergedFilePath;
        try
        {
            mergedFilePath = FileManager.getInstance().
                    getNewFile(directory, ".wav").getPath();
        }
        catch (InvalidDirectoryException e)
        {
            throw new OperationFailedException("couldn't make merged file path");
        }
        String command = String.format(MERGE_COMMAND,
                text.getPath(), mergedFilePath);

        try
        {
            File fileToReturn = runCommand(command, mergedFilePath);
            text.delete();
            return fileToReturn;
        }
        catch (InterruptedException | IOException e)
        {
            throw new OperationFailedException(e.getMessage());
        }
    }

    /**
     * run a FFMPEG command
     * @param command the command to run
     * @param newFilePath the path to the resulting command
     * @return the resulting file from the command operation
     * @throws IOException when the process cannot be executed
     * @throws InterruptedException when the process gets interrupted
     * @throws OperationFailedException when the resulting file cannot be found
     */
    private static File runCommand(String command, String newFilePath)
            throws IOException, InterruptedException, OperationFailedException
    {
        //run the command
        final Process process = Runtime.getRuntime().exec(command);

        //make sure the errStream and outputStream don't get blocked,
        //which would block the command from executing
        new StreamEater(process.getErrorStream(), "error", OUTPUT);
        new StreamEater(process.getInputStream(), "input", OUTPUT);

        //get the return value of the command. if not 0, something
        //went wrong
        int retVal = process.waitFor();
        if(retVal != 0)
        {
            throw new OperationFailedException("operation \"" + command +
                    "\" returned with value " + retVal + "!");
        }

        // Get the newly created file and return it. If it doesn't exist,
        // something went wrong;
        File file = new File(newFilePath);
        if (file.exists())
        {
            return file;
        }
        else
        {
            throw new OperationFailedException("not able to get resulting " +
                    "file " + newFilePath + " from command \"" + command+ "\'");
        }
    }

}
