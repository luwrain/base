
package org.luwrain.core;

public class RegistryException extends RuntimeException
{
    public RegistryException(Exception e)
    {
	super(e);
    }

    public RegistryException(String message, Exception e)
    {
	super(message, e);
    }

    public RegistryException(String message)
    {
	super(message);
    }
}
