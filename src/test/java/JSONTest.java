import util.json.JSONObject;
import util.json.JSONPair;

/**
 *
 */
public class JSONTest
{
    public static void main(String[] args)
    {
        JSONObject object = new JSONObject();
        object.addPair(new JSONPair("name", "john doe"));
        object.addPair(new JSONPair("age", 30));
        object.addPair(new JSONPair("gender", "male"));
        object.addPair(new JSONPair("programmer", true));
        System.out.println(object);
    }
}
