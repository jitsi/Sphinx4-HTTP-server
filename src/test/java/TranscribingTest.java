import server.AudioFileManipulator;
import server.AudioTranscriber;
import util.FileManager;
import util.TimeStrings;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;

/**
 * Tests whether the transcribing works
 */
public class TranscribingTest
{
    public static void main(String[] args) throws Exception
    {
        try
        {
            AudioTranscriber transcriber = new AudioTranscriber();
            transcriber.transcribe(TestFiles.TEST_FILE);
        }
        catch (Exception e)
        {
            throw new Exception("Transcribing test has failed");
        }
    }
}
