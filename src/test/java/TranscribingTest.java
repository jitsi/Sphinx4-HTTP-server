import server.AudioTranscriber;

import java.io.File;

/**
 * Created by workingnik on 14/07/16.
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
