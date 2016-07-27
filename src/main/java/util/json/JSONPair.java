package util.json;

/**
 * Class representing a JSONPair, which is a value with a String identifier
 * separated by a ":"
 */
public class JSONPair
{
    /**
     * The string identifier of the value
     */
    private String name;
    /**
     * The value stored as a string
     */
    private String value;

    /**
     * Construct a JSONPair with a null value
     * @param name the name of the value
     */
    public JSONPair(String name)
    {
        this.name = name;
        this.value = "null";
    }

    /**
     * Construct a JSONPair with a string value
     * @param name the name of the value
     * @param value the String value to be stored
     */
    public JSONPair(String name, String value)
    {
        this.name =  name;
        this.value = putInBetweenQuotes(value);
    }

    /**
     * Construct a JSONPair with a JSONObject value
     * @param name the name of the value
     * @param value the JSONObject value to be stored
     */
    public JSONPair(String name, JSONObject value)
    {
        this.name = name;
        this.value = value.toString();
    }

    /**
     * Construct a JSONPair with a JSONArray value
     * @param name the name of the value
     * @param value the JSONArray value to be stored
     */
    public JSONPair(String name, JSONArray value)
    {
        this.name = name;
        this.value = value.toString();
    }

    /**
     * Construct a JSONPair with a integer number value
     * @param name the name of the value
     * @param value the integer value to be stored
     */
    public JSONPair(String name, int value)
    {
        this.name = name;
        this.value = Integer.toString(value);
    }

    /**
     * Construct a JSONPair with a double number value
     * @param name the name of the value
     * @param value the double value to be stored
     */
    public JSONPair(String name, double value)
    {
        this.name = name;
        this.value = Double.toString(value);
    }

    public JSONPair(String name, long value)
    {
        this.name = name;
        this.value = Long.toString(value);
    }

    /**
     * Construct a JSONPair with a boolean value
     * @param name the name of the value
     * @param value the boolean value to be stored
     */
    public JSONPair(String name, boolean value)
    {
        this.name = name;
        this.value = value ? "true" : "false";
    }

    /**
     * String representation of the JSONPair, with the name in between quotes
     * and the value as a string separated by a semicolon ":"
     * @return the JSONPair as a string
     */
    @Override
    public String toString()
    {
        return putInBetweenQuotes(name) + ":" +  value;
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
