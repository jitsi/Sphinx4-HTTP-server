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

import edu.cmu.sphinx.result.WordResult;
import org.jitsi.sphinx4http.server.AudioTranscriber;
import org.junit.Assert;
import org.junit.Test;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * Test the accuracy of transcribing a file
 */
public class AccuracyTest
{

    /**
     * The expected accuracy of the test audio file
     */
    private final static double EXPECTED_ACCURACY = 60; // %

    /**
     * The TRANSCRIPTION of the test audio file
     */
    private final static String TRANSCRIPTION =
        "A hungry Fox saw some fine bunches of Grapes hanging from a vine that " +
        "was trained along a high trellis and did his best to reach them by " +
        "jumping as high as he could into the air But it was all in vain for" +
        " they were just out of reach so he gave up trying and walked away" +
        " with an air of dignity and unconcern remarking I thought those" +
        " Grapes were ripe but I see now they are quite sour";

    @Test
    public void testAccuracy()
        throws Exception
    {
        AudioTranscriber transcriber = new AudioTranscriber();
        ArrayList<WordResult> results = transcriber.transcribe(new FileInputStream(TestFiles.TEST_FILE));
        if(!results.isEmpty())
        {
            ArrayList<String> observedWords = new ArrayList<>();
            for(WordResult result : results)
            {
                if(!result.isFiller())
                {
                    observedWords.add(result.getWord().toString());
                }
            }

            double accuracy = computeAccuracy(getCorrectTranscription(), createWordCountHashMap(observedWords));
            System.out.println(accuracy);
            Assert.assertTrue(accuracy >= EXPECTED_ACCURACY);
        }
        else
        {
            Assert.fail();
        }
    }

    /**
     * Compute the accuracy of two "histograms" of words, by comparing if the observed histogram
     * has the same frequency of words as the expected histogram
     * @param expected the correct histogram of a transcription
     * @param observed the histogram of a transcription to test for accuracy
     * @return the accuracy in % of the observed histogram
     */
    private double computeAccuracy(HashMap<String, Integer> expected, HashMap<String, Integer> observed)
    {
        double total = 0;
        double correct = 0;
        for(String word : expected.keySet())
        {
            System.out.println(word);
            System.out.printf("expected: %d observed: %d%n",
                              expected.get(word),
                              observed.containsKey(word) ? observed.get(word) : 0);
            total += expected.get(word);
            if(observed.containsKey(word))
            {
                correct += observed.get(word);
            }
        }

        return (correct / total) * 100;
    }

    /**
     * Create a HashMap which stores the frequency for each word in an ArrayList of words
     * @param words the arraylist of words to convert to a frequency HashMap
     * @return the frequency HashMap
     */
    private HashMap<String, Integer> createWordCountHashMap(ArrayList<String> words)
    {
        HashMap<String, Integer> histogram = new HashMap<>();
        for(String s : words)
        {
            if(histogram.containsKey(s))
            {
                histogram.put(s, histogram.get(s) + 1);
            }
            else
            {
                histogram.put(s, 1);
            }
        }
        return histogram;
    }

    /**
     * Get the frequency HashMap of the correct transcription of the test audio file
     * @return the frequency HashMap
     */
    private HashMap<String, Integer> getCorrectTranscription()
    {
        String[] transcribedWords = TRANSCRIPTION.split(" ");
        ArrayList<String> strings = new ArrayList<>();
        Collections.addAll(strings, transcribedWords);

        return createWordCountHashMap(strings);
    }

}
