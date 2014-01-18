package nl.ronalddehaan.exceptions;

public class UnknownTypeException extends Exception
{
    public UnknownTypeException()
    {
        super();
    }
    
    public UnknownTypeException(String message)
    {
        super(message);
    }
}
