package server;
import edu.cmu.sphinx.api.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;


/**
 *  Uses the Sphinx4 speech-to-text library to get hypothesises of the
 *  spoken text in a given audio fragment
 *  Currently only predicts U.S english text
 *  The Sphinx4 API requires audio fragments to be in .wav format, have
 *  mono sound and have 16000 KHz audio rate
 */
public class AudioTranscriber
{
    /**
     * The configuration used for creating the speech recognizer
     */
    private Configuration config;

    /**
     * This object gets an InputStream from an audio file and
     * tries to predict what was said
     */
    private StreamSpeechRecognizer recognizer;

    /**
     * Constructs an AudioTranscriber object
     * @throws IOException when the given Paths in the config do not point
     * to a valid file.
     * */
    public AudioTranscriber()
            throws IOException
    {
        this.config = new Configuration();
        config.setAcousticModelPath(SphinxConstants.ACOUSTIC_MODEL_EN_US);
        config.setDictionaryPath(SphinxConstants.DICTIONARY_EN_US);
        config.setLanguageModelPath(SphinxConstants.LANGUAGE_MODEL_EN_US);
        recognizer = new StreamSpeechRecognizer(config);
    }

    /**
     * Tries to predict the speech in a given audio fragment
     * @param audioFile the audio file on which speech prediction is desired
     * @return the hypotheses of the speech in the given audio fragment
     * @throws IOException when the given audio file cannot be read properly
     */
    public synchronized String transcribeAudioFile(File audioFile)
            throws IOException
    {
        recognizer.startRecognition(new FileInputStream(audioFile));

        SpeechResult result = recognizer.getResult();
        recognizer.stopRecognition();
        if (result != null)
        {
            return result.getHypothesis();
        }
        else
        {
            return "";
        }
    }

}
