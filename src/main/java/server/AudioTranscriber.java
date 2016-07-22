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
            throws IOException
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
     * @throws IOException when the given audio file cannot be read properly
     */
    public String transcribeAudioFile(File audioFile)
            throws IOException
    {
        StreamSpeechRecognizer recognizer = new StreamSpeechRecognizer(config);
        recognizer.startRecognition(new FileInputStream(audioFile));

        ArrayList<WordResult> utteredWords = new ArrayList<>();
        String text = "";

        SpeechResult result;
        Stats stats = recognizer.createStats(1);
        while ((result = recognizer.getResult()) != null) {
            text += (result.getHypothesis() + "\n");
            utteredWords.addAll(result.getWords());
            try
            {
                stats.collect(result);
            }
            catch (Exception e) {e.printStackTrace();}

            System.out.format("Hypothesis: %s\n", text);
        }
        recognizer.stopRecognition();

        //get the transform
        Transform transform = stats.createTransform();
        recognizer.setTransform(transform);

        //test if stats improve output
        recognizer.startRecognition(new FileInputStream(audioFile));
        String improvedText = "";
        while ((result = recognizer.getResult()) != null) {
            improvedText += (result.getHypothesis() + "\n");
            System.out.format("Hypothesis: %s\n", text);
        }

        //currently for debugging, later for creating JSON output
        for(WordResult wordResult : utteredWords)
        {
            System.out.format("%s: %s\n",
                    wordResult.getTimeFrame(),
                    wordResult.getWord());
        }
        System.out.println("normal output:\n" + text);
        System.out.println("improved output:\n" + improvedText);

        return text;
    }

}
