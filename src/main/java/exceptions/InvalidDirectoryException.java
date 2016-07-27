package exceptions;

/**
 * Used in the FileManager class when it's API is used to get a file from a
 * directory not managed by the class
 */
public class InvalidDirectoryException extends Exception
{
    public InvalidDirectoryException(String s)
    {
        super(s);
    }
}
