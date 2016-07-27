package exceptions;

/**
 *  Exception thrown in the FileManager class when it's API is used to
 *  check on a file not in the directory of the FileManager
 */
public class NotInDirectoryException extends Exception
{
    public NotInDirectoryException()
    {
        super();
    }

    public NotInDirectoryException(String s)
    {
        super(s);
    }
}
