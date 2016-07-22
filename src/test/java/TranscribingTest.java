import server.AudioTranscriber;

import java.io.File;

/**
 * Tests whether the transcribing works
 */
public class TranscribingTest
{
    public static void main(String[] args) throws Exception
    {
        File file = new File("src/test/resources/AF001.wav");
        AudioTranscriber transcriber = new AudioTranscriber();
        String result = transcriber.transcribeAudioFile(file);
        System.out.println("result:\n" + result);
    }
}
