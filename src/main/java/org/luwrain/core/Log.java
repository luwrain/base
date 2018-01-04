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

package org.luwrain.core;

import java.util.*;

public class Log
{
    public enum Level {DEBUG, INFO, WARNING, ERROR, FATAL};

    static public class Message
    {
	public final Level level;
	public final String component;
	public final String message;

	Message(Level level, String component, String message)
	{
	    NullCheck.notNull(level, "level");
	    NullCheck.notNull(component, "component");
	    NullCheck.notNull(message, "message");
	    this.level = level;
	    this.component = component;
	    this.message = message;
	}
    }

    public interface Listener
    {
	void onLogMessage(Message message);
    }

    static private final Vector<Listener> listeners = new Vector<Listener>();
    static private boolean briefMode = false;
    static private final Object syncObj = new Object();

    static public void addListener(Listener listener)
    {
	synchronized (syncObj) {
	NullCheck.notNull(listener, "listener");
	for(int i = 0;i < listeners.size();++i)
	    if (listeners.get(i) == listener)
		return;
	listeners.add(listener);
	}
    }

    static public void removeListener(Listener listener)
    {
	synchronized (syncObj) {
	NullCheck.notNull(listener, "listener");
	for(int i = 0;i < listeners.size();++i)
	    if (listeners.get(i) == listener)
	    {
		listeners.remove(i);
		return;
	    }
	}
    }

    static void enableBriefMode()
    {
	briefMode = true;
    }

    static private void message(Level level,
				String component, String message)
    {
	if (level == null || component == null || message == null)
	    return;
	synchronized (syncObj) 
{
	final Message msg = new Message(level, component, message);
	for(Listener l: listeners)
	    l.onLogMessage(msg);
	switch(level)
	{
	case DEBUG:
	    if (!briefMode)
		System.out.println(component + ":" + message);
	    break;
	case INFO:
	    if (briefMode)
	    {
		if (component.equals("init") || component.equals("core"))
		    		System.out.println(message); else
		System.out.println(component + ":" + message);
	    }else
						System.out.println(level.toString() + ":" + component + ":" + message);
	    break;
	default:
	    						System.out.println(level.toString() + ":" + component + ":" + message);
			    			}
}
    }

    public static void debug(String component, String message)
    {
	if (message != null)
	    message(Level.DEBUG, (component != null && !component.isEmpty())?component:"-", message);
    }

    static public void info(String component, String message)
    {
	if (message != null)
	    message(Level.INFO, (component != null && !component.isEmpty())?component:"-", message);
    }

    static public void warning(String component, String message)
    {
	if (message != null)
	    message(Level.WARNING, (component != null && !component.isEmpty())?component:"-", message);
    }

    static public void error(String component, String message)
    {
	if (message != null)
	    message(Level.ERROR, (component != null && !component.isEmpty())?component:"-", message);
    }

    static public void fatal(String component, String message)
    {
	if (message != null)
	    message(Level.FATAL, (component != null && !component.isEmpty())?component:"-", message);
    }
}
