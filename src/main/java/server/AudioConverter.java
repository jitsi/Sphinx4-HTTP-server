package server;

import exceptions.ConversionFailedException;
import util.StreamEater;
import java.io.*;

/**
 * Converts a given audio file to the correct format required by Sphinx4
 */
public class AudioConverter
{
    /**
     * The program being used to convert the audio file.
     * Recommended are ffmpeg or avconv
     */
    private final static String program = "/usr/bin/avconv";

    /**
     * Command to run the program avconv to convert an audio file to
     * .wav format with mono sound and a KHz of 16000
     * "-i %s" -  the file to convert, where %s needs to be formatted in
     * "-acodec pcm_s16le" - convert to raw pcm for .wav file
     * "-ac 1" - convert to one audio chanel (mono sound)
     * "-ar 16000" - convert to 16000 KHz
     * "%s" the file to convert to, where %s needs to be formatted in
     */
    private final static String command =
            program + " -i %s -acodec pcm_s16le -ac 1 -ar 16000 %s";

    /**
     * Converts the given file to .wav format with a sampling rate of
     * 16 kHz and mono audio
     * @param toConvert the file to convert
     * @param newFilePath the path where the converted file should be
     */
    public static File convertToWAV(File toConvert, String newFilePath)
            throws IOException, InterruptedException, ConversionFailedException
    {
        //build the correct command string
        String formattedCommand = String.format(
                command, toConvert.getAbsolutePath(), newFilePath);

        //run the command
        final Process process = Runtime.getRuntime().exec(formattedCommand);

        //make sure the errStream and outputStream don't get blocked,
        //which would block the command from executing
        new StreamEater(process.getErrorStream(), "error").start();
        new StreamEater(process.getInputStream(), "input").start();

        //get the return value of the command. if not 0, something went wrong
        int retVal = process.waitFor();
        if(retVal != 0)
        {
            throw new ConversionFailedException("command" + process +
                    "returned with value " + retVal + "!");
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
            throw new ConversionFailedException("not able to convert file " +
                    toConvert.getName() + " to .wav");
        }
    }

}
