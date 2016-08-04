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
