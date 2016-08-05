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
import org.jitsi.sphinx4http.util.FileManager;
import org.junit.Assert;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * test the merging method of the program ffmpeg
 */
public class MergeTest
{
    @org.junit.Test
    public void testMerging() throws Exception
    {
        File[] testFiles = TestFiles.TEST_FILE_CHUNKS;
        File[] filesToMerge = new File[testFiles.length];
        for (int i = 0; i < testFiles.length; i++)
        {
            File file = testFiles[i];
            File newFile = new File(
                    FileManager.getInstance()
                    .getNewFile(FileManager.CONVERTED_DIR, ".wav")
                    .toString());

            FileInputStream in = new FileInputStream(file);
            FileOutputStream out = new FileOutputStream(newFile);
            while (in.available() > 0)
            {
                out.write(in.read());
            }
            filesToMerge[i] = newFile;
        }

        File merged = AudioFileManipulator.mergeWAVFiles(filesToMerge);
        Assert.assertTrue("Merged file did not exist", merged.exists());

        //delete test files
        merged.delete();
        for (File file : filesToMerge)
        {
            file.delete();
        }
    }
}
