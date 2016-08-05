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

import org.jitsi.sphinx4http.server.AudioFileManipulator;
import org.jitsi.sphinx4http.util.TimeStrings;
import org.junit.Assert;

import java.io.File;

/**
 * Tests whether the converting of audio files work
 */
public class ConversionTest
{
    @org.junit.Test
    public void testConversion() throws Exception
    {
        File file = TestFiles.TEST_FILE;

        String path = file.getParentFile().getAbsolutePath() + "/" +
                TimeStrings.getNowString() + ".wav";
        File convertedFile = AudioFileManipulator.convertToWAV(file, path);

        Assert.assertTrue("Converted file did not exist",
                convertedFile.exists());
        //delete the temp converted file
        convertedFile.delete();
    }

}
