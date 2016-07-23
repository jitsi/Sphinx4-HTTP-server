package util.json;

/**
 *
 */
public class JSONPair
{
    private String name;
    private String value;

    public JSONPair(String name)
    {
        this.name = name;
        this.value = "null";
    }

    public JSONPair(String name, String value)
    {
        this.name =  name;
        this.value = putInBetweenQuotes(value);
    }

    public JSONPair(String name, JSONObject value)
    {
        this.name = name;
        this.value = value.toString();
    }

    public JSONPair(String name, int value)
    {
        this.name = name;
        this.value = Integer.toString(value);
    }

    public JSONPair(String name, double value)
    {
        this.name = name;
        this.value = Double.toString(value);
    }

    public JSONPair(String name, boolean value)
    {
        this.name = name;
        this.value = value ? "true" : "false";
    }

    @Override
    public String toString()
    {
        return putInBetweenQuotes(name) + ":" +  value;
    }

    private String putInBetweenQuotes(String string)
    {
        return "\"" + string + "\"";
    }
}
