package util.json;

import java.util.ArrayList;

/**
 *
 */
public class JSONObject
{

    private ArrayList<JSONPair> pairs;

    public JSONObject()
    {
        this.pairs = new ArrayList<>();
    }

    public void addPair(JSONPair pair)
    {
        pairs.add(pair);
    }

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
