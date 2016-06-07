
package org.luwrain.core;

public class InitResult 
{
    public enum Type {SUCCESS, FAILURE};

    private Type type = Type.SUCCESS;
    private String message = "";
    private Exception exception = null;

    public InitResult()
    {
	type = Type.SUCCESS;
    }

    public InitResult(Type type)
    {
	NullCheck.notNull(type, "type");
	this.type = type;
    }

    public InitResult(Type type, String message)
    {
	NullCheck.notNull(type, "type");
	NullCheck.notNull(message, "message");
	this.type = type;
	this.message = message;
    }

    public InitResult(Exception exception)
    {
	NullCheck.notNull(exception, "exception");
	this.type = Type.FAILURE;
	this.message = exception.getMessage();
	this.exception = exception;
    }

    public InitResult(String message, Exception exception)
    {
	NullCheck.notNull(message, "message");
	NullCheck.notNull(exception, "exception");
	this.type = Type.FAILURE;
	this.message = message;
	this.exception = exception;
    }

    public InitResult(Type type, String message,
Exception exception)
    {
	NullCheck.notNull(type, "type");
	NullCheck.notNull(message, "message");
	NullCheck.notNull(exception, "exception");
	this.type = type;
	this.message = message;
	this.exception = exception;
    }

    public Type type() {return type;}
    public String message() {return message;}
    public Exception exception() {return exception;}
    public boolean success() {return type == Type.SUCCESS;}
    public boolean failure() {return type == Type.FAILURE;}
}
