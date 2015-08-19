/*
   Copyright 2012-2015 Michael Pozhidaev <msp@altlinux.org>

   This file is part of the Luwrain.

   Luwrain is free software; you can redistribute it and/or
   modify it under the terms of the GNU General Public
   License as published by the Free Software Foundation; either
   version 3 of the License, or (at your option) any later version.

   Luwrain is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
   General Public License for more details.
*/

package org.luwrain.core;

public class Event
{
    static public final int KEYBOARD_EVENT = 1;
    public static final int ENVIRONMENT_EVENT = 2;
    public static final int AREA_QUERY_EVENT = 3;
    public static final int UI_EVENT = 4;

    private int eventType;
    private boolean processed = false;
    private Object syncObj = new Object();

    public Event(int eventType)
    {
	this.eventType = eventType;
    }

    public int eventType()
    {
	return eventType;
    }

    /**
     * Signals that the processing of this event is finished. Do not touch
     * this method as it is designed for invocation by 
     * {@code org.luwrain.core.Environment} class only which controls the event
     * loop. Once this method is called, all threads freezed on {@code waitForBeProcessed()}
     * method continue the execution.
     */
    public void markAsProcessed()
    {
	if (processed)
	    return;
	processed = true;
	synchronized (syncObj) 
	{
	    syncObj.notifyAll();
	}
    }

    /**
     * Freezes current thread until this event be processed. This method
     * guarantees that the execution will continue after the core completely
     * finishes processing of the event. This method may not be used in the
     * same thread as the main thread of the core (there are no any
     * corresponding checks). The improper use of this method will hang the
     * system infinitely.
     */
    public void waitForBeProcessed() throws InterruptedException
    {
	if (processed)
	    return;
	synchronized (syncObj)
	{
	    while (!processed)
		syncObj.wait();
	}
    }
}
