/*
   Copyright 2012-2018 Michael Pozhidaev <michael.pozhidaev@gmail.com>

   This file is part of LUWRAIN.

   LUWRAIN is free software; you can redistribute it and/or
   modify it under the terms of the GNU General Public
   License as published by the Free Software Foundation; either
   version 3 of the License, or (at your option) any later version.

   LUWRAIN is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
   General Public License for more details.
*/

package org.luwrain.base;

import java.net.*;
import java.util.*;

public interface MediaResourcePlayer extends ExtensionObject 
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
	void onPlayerTime(Instance player, long msec);
	void onPlayerFinish(Instance player);
	void onPlayerError(Exception e);
    }

    public interface Instance
    {
	Result play(URL url, long playFromMsec, Set<Flags> flags);
	void stop();
    }

    Instance newMediaResourcePlayer(Listener listener);
    String getSupportedMimeType();//FIXME:multiple types
}
