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

package org.jitsi.sphinx4http.util.json;

import java.util.ArrayList;

/**
 * A class representing a JSON object, which consists of JSONPair's enclosed by
 * "{" and "}" and separated by ",".
 */
public class JSONObject
{

    /**
     * The array of JSONPairs
     */
    private ArrayList<JSONPair> pairs;

    /**
     * Create a JSONObject
     */
    public JSONObject()
    {
        this.pairs = new ArrayList<>();
    }

    /**
     * Add a JSONPair to the object
     * @param pair
     */
    public void addPair(JSONPair pair)
    {
        pairs.add(pair);
    }

    /**
     * Return the JSON object as a string, with the JSONPairs enclosed by
     * "{ " and "}" and separated by ,
     * @return the JSONObject as a String
     */
    @Override
    public String toString()
    {
        String toReturn  = "{";
        for(JSONPair pair : pairs)
        {
            toReturn += pair.toString() +  ",";
        }
        //remove last ,
        if(toReturn.endsWith(","))
        {
            toReturn = toReturn.substring(0, toReturn.length() - 1);
        }

        return toReturn + "}";
    }



}
