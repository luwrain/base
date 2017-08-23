/*
   Copyright 2012-2017 Michael Pozhidaev <michael.pozhidaev@gmail.com>
   Copyright 2015-2016 Roman Volovodov <gr.rPman@gmail.com>

   This file is part of the LUWRAIN.

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
	void onPlayerTime(MediaResourcePlayer player, long msec);
	void onPlayerFinish(MediaResourcePlayer player);
    }

    Result play(URL url, long playFromMsec, Set<Flags> flags);
    void stop();
}
