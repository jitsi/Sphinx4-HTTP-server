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

import java.io.File;

/**
 * Files for testing with
 */
public class TestFiles
{

    public static final File TEST_FILE =
            new File("src/test/resources/test.wav");

    public static final File[] TEST_FILE_CHUNKS =
            {
                new File("src/test/resources/test_chunk1.wav"),
                new File("src/test/resources/test_chunk2.wav"),
                new File("src/test/resources/test_chunk3.wav"),
                new File("src/test/resources/test_chunk4.wav"),
                new File("src/test/resources/test_chunk5.wav"),
            };

}
