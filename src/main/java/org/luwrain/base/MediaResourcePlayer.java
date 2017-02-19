
package org.luwrain.base;

import java.util.*;
import java.net.*;

public interface MediaResourcePlayer
{
    public enum Flags {}

    static public final class Result
    {
	public enum Type {OK};

	private final Type type;

	public Result()
	{
	    this.type = Type.OK;
	}

	public Type  getType()
	{
	    return type;
	}

	public boolean isOk()
	{
	    return type == Type.OK;
	}
    }

    public interface Listener
    {
	void onPlayerTime(long msec);
	void onPlayerFinish();
    }

    Result play(URL url, long playFromMsec, Set<Flags> flags);
    void stop();
}
