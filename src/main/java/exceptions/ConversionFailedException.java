package exceptions;

/**
 * Created by workingnik on 14/07/16.
 */
public class ConversionFailedException extends Exception
{
    public ConversionFailedException()
    {
        super();
    }

    public ConversionFailedException(String message)
    {
        super(message);
    }
}