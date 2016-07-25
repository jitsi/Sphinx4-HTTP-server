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
        File file = new File("src/test/resources/test.webm");
        System.out.println(file.getPath());

        String path = file.getParentFile().getAbsolutePath() + "/" +
                TimeStrings.getNowString() + ".wav";
        File convertedFile = AudioFileManipulator.convertToWAV(file, path);
        convertedFile.delete();
    }
}
