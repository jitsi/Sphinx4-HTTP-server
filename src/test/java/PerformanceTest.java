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

import org.jitsi.sphinx4http.server.AudioTranscriber;
import org.junit.Assert;
import org.junit.Test;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Test the speed performance of transcribing a file
 */
public class PerformanceTest
{
    /**
     * The amount of files which will be transcribed at the same time in the MultipleFilesSpeed test
     */
    private final static int AMOUNT_OF_MULTIPLE_FILES = 6;

    /**
     * The time which is deemed as the maximum allowed for transcribing an audio file of 25 seconds
     */
    private final static int DESIRED_TRANSCRIPTION_TIME = 60000; //in ms

    @Test
    public void testSingleFileSpeed()
        throws Exception
    {
        long time = testFileTranscription();
        Assert.assertTrue(time < DESIRED_TRANSCRIPTION_TIME); // file is 25 seconds long
    }

    @Test
    public void testMultipleFilesSpeed() throws Exception
    {
        ExecutorService service = Executors.newFixedThreadPool(AMOUNT_OF_MULTIPLE_FILES);
        Collection<Callable<Long>> callables = new ArrayList<>();
        for(int i = 0; i < AMOUNT_OF_MULTIPLE_FILES; i++)
        {
            callables.add(new Callable<Long>()
            {
                @Override
                public Long call()
                    throws Exception
                {
                    return testFileTranscription();
                }
            });
        }

        long start = System.currentTimeMillis();
        service.invokeAll(callables);
        service.shutdown();
        service.awaitTermination(DESIRED_TRANSCRIPTION_TIME, TimeUnit.MILLISECONDS);
        service.shutdownNow();
        long end = System.currentTimeMillis();

        Assert.assertTrue((end - start) < DESIRED_TRANSCRIPTION_TIME);
    }

    /**
     * Test the transcription of "test.wav", which is an audio file of 25 seconds
     *
     * @return the time it took the transcribe the file
     */
    private static long testFileTranscription()
        throws Exception
    {
        long start, end;
        AudioTranscriber transcriber = new AudioTranscriber();

        start = System.currentTimeMillis();
        transcriber.transcribe(new FileInputStream(TestFiles.TEST_FILE));
        end = System.currentTimeMillis();

        return end - start;
    }
}
