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

package org.jitsi.sphinx4http.util;

import java.security.SecureRandom;

/**
 *  Generator for random ID's. Very low change of getting a non-unique ID.
 *  If the default length is lowered, the change for non-unique ID's goes up.
 */
public class SessionIdentifierGenerator
{

    /**
     * All characters used for creating the ID
     */
    private final static char[] alphabet =
            {
                    'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o',
                    'p','q','r','s','t','u','v','w','x','y','z','A','B','C','D',
                    'E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S',
                    'T','U','V','W','X','Y','Z','0','1','2','3','4','5','6','7',
                    '8','9'
            };

    /**
     * The length of the generated ID
     */
    private int idLength = 32;

    /**
     * Secure random number generator for the creation of the ID's
     */
    private SecureRandom rng = new SecureRandom();

    /**
     * Constructor for generating ID's.
     */
    public SessionIdentifierGenerator()
    {
        rng = new SecureRandom();
    }

    /**
     * Constructor for generating ID's
     * @param length the length of the generated ID's
     */
    public SessionIdentifierGenerator(int length)
    {
        this();
        this.idLength = length;
    }

    /**
     * Generated a random ID
     * @return the newly generated ID
     */
    public String nextID()
    {
        String id = "";
        for(int i = 0; i < idLength; i++)
        {
            id += alphabet[(rng.nextInt(alphabet.length))];
        }
        return id;
    }

    /**
     * gets the length of the generated ID's
     * @return the length of the generated ID's
     */
    public int getIdLength()
    {
        return idLength;
    }

}
