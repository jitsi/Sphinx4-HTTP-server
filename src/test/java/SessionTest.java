import util.SessionIdentifierGenerator;

/**
 *
 */
public class SessionTest
{
    public static void main(String[] args)
    {
        SessionIdentifierGenerator generator = new SessionIdentifierGenerator();
        for(int i = 0; i < 10; i++)
        {
            System.out.println(generator.nextID());
        }
    }
}
