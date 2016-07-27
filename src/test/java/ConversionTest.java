import server.AudioFileManipulator;
import util.TimeStrings;

import java.io.File;

/**
 * Tests whether the converting of audio files work
 */
public class ConversionTest
{
    public static void main(String[] args) throws Exception
    {
        try
        {
            File file = TestFiles.TEST_FILE;

            String path = file.getParentFile().getAbsolutePath() + "/" +
                    TimeStrings.getNowString() + ".wav";
            File convertedFile = AudioFileManipulator.convertToWAV(file, path);
            if(!convertedFile.exists())
            {
                throw new Exception();
            }
            convertedFile.delete();
            System.exit(0);
        }
        catch (Exception e)
        {
            throw new Exception("Conversion test has failed");
        }
    }
}
