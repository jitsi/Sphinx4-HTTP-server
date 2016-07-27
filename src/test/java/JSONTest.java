import util.json.JSONArray;
import util.json.JSONObject;
import util.json.JSONPair;

/**
 *
 */
public class JSONTest
{
    public static void main(String[] args)
    {
        //define john doe
        JSONObject john = new JSONObject();
        john.addPair(new JSONPair("name", "john doe"));
        john.addPair(new JSONPair("age", 30));
        john.addPair(new JSONPair("gender", "male"));
        john.addPair(new JSONPair("programmer", true));

        JSONArray hobbies = new JSONArray();
        hobbies.addValue("tennis");
        hobbies.addValue("programming");
        hobbies.addValue("browsing reddit");
        john.addPair(new JSONPair("hobbies", hobbies));

        //define a few friends
        //anna
        JSONObject anna = new JSONObject();
        anna.addPair(new JSONPair("name", "anna doe"));
        anna.addPair(new JSONPair("age", 27));
        anna.addPair(new JSONPair("gender", "female"));
        anna.addPair(new JSONPair("programmer", false));

        JSONArray hobbiesAnna = new JSONArray();
        hobbiesAnna.addValue("volleyball");
        hobbiesAnna.addValue("reading");
        hobbiesAnna.addValue("sunbathing");
        anna.addPair(new JSONPair("hobbies", hobbiesAnna));

        //bill
        JSONObject bill = new JSONObject();
        bill.addPair(new JSONPair("name", "bill doe"));
        bill.addPair(new JSONPair("age", 34));
        bill.addPair(new JSONPair("gender", "male"));
        bill.addPair(new JSONPair("programmer", false));

        JSONArray hobbiesbill = new JSONArray();
        hobbiesbill.addValue("soccer");
        hobbiesbill.addValue("boxing");
        hobbiesbill.addValue("riding a motorcycle");
        bill.addPair(new JSONPair("hobbies", hobbiesbill));

        //add friends to john doe
        JSONArray friends = new JSONArray();
        friends.addValue(anna);
        friends.addValue(bill);

        john.addPair(new JSONPair("friends", friends));

        System.out.println(john);
        System.out.println(anna);
        System.out.println(bill);
    }
}
