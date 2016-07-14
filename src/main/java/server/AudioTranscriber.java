package server;
import edu.cmu.sphinx.api.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by workingnik on 14/07/16.
 */
public class AudioTranscriber
{
    private Configuration config;

    public AudioTranscriber()
    {
        this.config = new Configuration();
        config.setAcousticModelPath(SphinxConstants.ACOUSTIC_MODEL_EN_US);
        config.setDictionaryPath(SphinxConstants.DICTIONARY_EN_US);
        config.setLanguageModelPath(SphinxConstants.LANGUAGE_MODEL_EN_US);
    }

    public String transcribeAudioFile(File audioFile)
            throws IOException
    {
        StreamSpeechRecognizer recognizer = new StreamSpeechRecognizer(config);
        recognizer.startRecognition(new FileInputStream(audioFile));

        SpeechResult result = recognizer.getResult();
        recognizer.stopRecognition();

        return result.getHypothesis();
    }

}
