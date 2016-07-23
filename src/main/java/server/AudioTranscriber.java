package server;
import edu.cmu.sphinx.api.*;
import edu.cmu.sphinx.decoder.adaptation.Stats;
import edu.cmu.sphinx.decoder.adaptation.Transform;
import edu.cmu.sphinx.result.WordResult;
import edu.cmu.sphinx.util.TimeFrame;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;


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
    {
        this.config = new Configuration();
        config.setAcousticModelPath(SphinxConstants.ACOUSTIC_MODEL_EN_US);
        config.setDictionaryPath(SphinxConstants.DICTIONARY_EN_US);
        config.setLanguageModelPath(SphinxConstants.LANGUAGE_MODEL_EN_US);
    }

    /**
     * Tries to predict the speech in a given audio fragment
     * @param audioFile the audio file on which speech prediction is desired
     * @return the hypotheses of the speech in the given audio fragment
     * as a list of words
     * @throws IOException when the given audio file cannot be read properly
     */
    public ArrayList<WordResult> transcribe(File audioFile)
            throws IOException
    {
        StreamSpeechRecognizer recognizer = new StreamSpeechRecognizer(config);
        recognizer.startRecognition(new FileInputStream(audioFile));

        ArrayList<WordResult> utteredWords = new ArrayList<>();
        SpeechResult result;
        while ((result = recognizer.getResult()) != null) {
            utteredWords.addAll(result.getWords());
        }
        recognizer.stopRecognition();

        return utteredWords;
    }

}
