/*
 * Sphinx4 HTTP server
 *
 * Copyright @ 2016 Atlassian Pty Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jitsi.sphinx4http.server;
import edu.cmu.sphinx.api.*;
import edu.cmu.sphinx.result.WordResult;
import java.io.IOException;
import java.io.InputStream;
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
     * Constructs an AudioTranscriber object
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
     * @param audioStream the audio stream on which speech prediction is desired
     * @return the hypotheses of the speech in the given audio fragment
     * as a list of words
     * @throws IOException when the given audio file cannot be read properly
     */
    public ArrayList<WordResult> transcribe(InputStream audioStream)
            throws IOException
    {
        StreamSpeechRecognizer recognizer = new StreamSpeechRecognizer(config);
        recognizer.startRecognition(audioStream);

        ArrayList<WordResult> utteredWords = new ArrayList<>();
        SpeechResult result;
        while ((result = recognizer.getResult()) != null)
        {
            utteredWords.addAll(result.getWords());
        }
        recognizer.stopRecognition();

        return utteredWords;
    }

}
