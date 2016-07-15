package server;

/**
 * Stores the Paths to acoustic models, dictionaries and language models
 * in the Sphinx4 library, which are needed to accomplish speech recognition.
 * They are used for creating the Configurations for Sphinx4's
 * SpeechRecognition objects.
 */
public class SphinxConstants
{
    /**
     * Acoustic model for american english
     */
    public final static String ACOUSTIC_MODEL_EN_US =
            "resource:/edu/cmu/sphinx/models/en-us/en-us";

    /**
     * Dictionary of american english words
     */
    public final static String DICTIONARY_EN_US =
            "resource:/edu/cmu/sphinx/models/en-us/cmudict-en-us.dict";

    /**
     * Language model for american english
     */
    public final static String LANGUAGE_MODEL_EN_US =
            "resource:/edu/cmu/sphinx/models/en-us/en-us.lm.bin";
}
