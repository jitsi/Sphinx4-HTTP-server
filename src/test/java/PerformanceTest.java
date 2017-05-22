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

import java.io.*;
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
     * The amount of files which will be transcribed at the same time in the
     * MultipleFilesSpeed test
     */
    private final static int AMOUNT_OF_MULTIPLE_FILES = 6;

    /**
     * The time which is deemed as the maximum allowed for transcribing an
     * audio file of 25 seconds
     */
    private final static int DESIRED_TRANSCRIPTION_TIME = 60000; //in ms

    @Test
    public void testSingleFileSpeed()
        throws Exception
    {
        long time = testFileTranscription();
        Assert.assertTrue(time < DESIRED_TRANSCRIPTION_TIME);
    }

    @Test
    public void testMultipleFilesSpeed()
        throws Exception
    {
        ExecutorService service = Executors.newFixedThreadPool(
            AMOUNT_OF_MULTIPLE_FILES);
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
        service.awaitTermination(DESIRED_TRANSCRIPTION_TIME,
                                 TimeUnit.MILLISECONDS);
        service.shutdownNow();
        long end = System.currentTimeMillis();

        Assert.assertTrue((end - start) < DESIRED_TRANSCRIPTION_TIME);
    }

    /**
     * Test the transcription of "test.wav", which is an audio file of 25
     * seconds
     *
     * @return the time it took the transcribe the file
     */
    private static long testFileTranscription()
        throws Exception
    {
        long start, end;
        AudioTranscriber transcriber = new AudioTranscriber();

        start = System.currentTimeMillis();
        System.err.println("Start transcribing.");
        WavFileInputStream fis = new WavFileInputStream(TestFiles.TEST_FILE);
        transcriber.transcribe(fis);
        end = System.currentTimeMillis();

        long time = end - start;
        System.err.println("Transcribed a 25second file in " + time + "ms.," +
                           " slept for a total of " + fis.totalSleep + "ms.");
        return time;
    }

    /**
     * Implements a {@link FileInputStream} which assumes the input is
     * 16kHz PCM and sleeps the according amount of time after each call to
     * {@link #read(byte[], int, int)}. Simulates a live audio stream.
     */
    private static class WavFileInputStream extends FileInputStream
    {
        int prevRead = -1;
        int totalSleep = 0;

        public WavFileInputStream(File file)
            throws FileNotFoundException
        {
            super(file);
        }

        @Override
        public int read(byte[]buf, int i1, int i2)
            throws IOException
        {
            int read = super.read(buf, i1, i2);
            if (prevRead > 0)
            {
                // 1 sec = 16000 samples * 2 bytes = 32000 bytes
                // 1 ms = 32 bytes
                try
                {
                    int sleep = prevRead/32;
                    totalSleep += sleep;
                    Thread.sleep(sleep);
                }
                catch (InterruptedException ie){}
            }
            prevRead = read;
            return read;
        }
    }
}
