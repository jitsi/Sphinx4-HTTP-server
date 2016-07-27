package util.json;

import java.util.ArrayList;

/**
 * a class representing a JSON array, with values between "[" and "]"
 * and separated by a "," .
 */
public class JSONArray
{
    /**
     * Holds the string representation of each added value
     */
    private ArrayList<String> values;

    /**
     * Create a JSON array
     */
    public JSONArray()
    {
        values = new ArrayList<>();
    }

    /**
     * Add a json object value to the array
     * @param value the JSONObject to be added to the array
     */
    public void addValue(JSONObject value)
    {
        values.add(value.toString());
    }

    /**
     * Add a json array value to the array
     * @param value the JSONArray to be added to the array
     */
    public void addValue(JSONArray value)
    {
        values.add(value.toString());
    }

    /**
     * Add a string value to the array
     * @param value the String to be added to the array
     */
    public void addValue(String value)
    {
        values.add(putInBetweenQuotes(value));
    }

    /**
     * Add an integer number value to the array
     * @param value the integer to be added to the array
     */
    public void addValue(int value)
    {
        values.add(Integer.toString(value));
    }

    /**
     * Add a double number value to the array
     * @param value the double to be added to the array
     */
    public void addValue(double value)
    {
        values.add(Double.toString(value));
    }

    /**
     * Add a boolean value to the array
     * @param value the boolean to be added to the array
     */
    public void addValue(boolean value)
    {
        if(value)
        {
            values.add("true");
        }
        else
        {
            values.add("false");
        }
    }

    /**
     * Return the array in String format, with values between "[" and "]" and
     * seperated by ",".
     * @return the String representation of the array
     */
    @Override
    public String toString()
    {
        String toReturn  = "[";
        for(String value : values)
        {
            toReturn += value +  ",";
        }
        //remove last ,
        if(toReturn.endsWith(","))
        {
            toReturn = toReturn.substring(0, toReturn.length() - 1);
        }

        return toReturn + "]";
    }

    /**
     * Put a given String in between quotes, such that the first and last
     * characters of the String are ' " '
     * @param string the string to put between quotes
     * @return the string with a quote as first and last character
     */
    private String putInBetweenQuotes(String string)
    {
        return "\"" + string + "\"";
    }
}
