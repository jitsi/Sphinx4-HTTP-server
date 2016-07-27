package util.json;

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
        return toReturn.substring(0, toReturn.length() - 1) + "}";
    }

}
