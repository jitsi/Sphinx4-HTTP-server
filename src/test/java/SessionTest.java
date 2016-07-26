
import util.SessionIdentifierGenerator;
import util.SessionManager;

/**
 *
 */
public class SessionTest
{
    public static void main(String[] args)
    {
        SessionManager sessionManager = new SessionManager();
        for(int i = 0; i < 10; i++)
        {
            System.out.println(sessionManager.createNewSession().getId());
        }
    }
}
