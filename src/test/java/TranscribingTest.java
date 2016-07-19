import server.AudioTranscriber;

import java.io.File;

/**
 * Tests whether the transcribing works
 */
public class TranscribingTest
{
    public static void main(String[] args) throws Exception
    {
        File file = new File("src/test/resources/caffeine.wav");
        AudioTranscriber transcriber = new AudioTranscriber();
        System.out.println("result: " + transcriber.transcribeAudioFile(file));
    }
}
