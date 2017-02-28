
package org.luwrain.base;

import java.io.IOException;
import java.nio.file.Path;

import org.luwrain.core.*;

public interface FilesOperation extends Runnable
{
    public enum ConfirmationChoices
    {
	OVERWRITE,
	SKIP,
	CANCEL
    };

    public interface Listener
    {
	void onOperationProgress(FilesOperation operation);
	ConfirmationChoices confirmOverwrite(Path path);
    }

    public final class Result 
    {
	public enum Type { OK, INTERRUPTED, EXCEPTION, MOVE_DEST_NOT_DIR};

	private final Type type;
	private final String extInfo;
	private final Exception exception;

	public Result()
	{
	    this.type = Type.OK;
	    this.extInfo = null;
	    this.exception = null;
	}

	public Result(Type type)
	{
	    NullCheck.notNull(type, "type");
	    this.type = type;
	    this.extInfo = null;
	    this.exception = null;
	}

	public Result(Type type, String extInfo)
	{
	    NullCheck.notNull(type, "type");
	    this.type = type;
	    this.extInfo = extInfo;
	    this.exception = null;
	}

	public Result(Type type, Exception exception)
	{
	    NullCheck.notNull(type, "type");
	    this.type = type;
	    this.extInfo = null;
	    this.exception = exception;
	}

	public Result(Type type, String extInfo, Exception exception)
	{
	    NullCheck.notNull(type, "type");
	    this.type = type;
	    this.extInfo = extInfo;
	    this.exception = exception;
	}

	public Type getType()
	{
	    return type;
	}

	public String getExtInfo()
	{
	    return extInfo;
	}

	public Exception getException()
	{
	    return exception;
	}

	@Override public String toString()
	{
	    return type.toString() + ", " +
	    (extInfo != null?extInfo:"[no extended info]") + ", " +
	    (exception != null?(exception.getClass().getName() + ":" + exception.getMessage()):"[no exception]");
	}
    }

    String getOperationName();
    int getPercents();
    void interrupt();
    boolean isFinished();
    Result getResult();
    Path getExtInfoPath();
    IOException getExtInfoIoException();
    boolean finishingAccepted();
}
