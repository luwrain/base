/*
   Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>

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

package org.luwrain.app.commander.layouts;

import java.util.*;

import org.luwrain.core.*;
import org.luwrain.core.events.*;
import org.luwrain.core.events.InputEvent.*;
import org.luwrain.controls.*;
import org.luwrain.app.base.*;
import org.luwrain.app.commander.*;

public final class CommandOutputLayout extends LayoutBase
{
final App app;
    final Job job;
    final List<String> text = new ArrayList<>();
    final NavigationArea area;

    public CommandOutputLayout(App app, Job job)
    {
	super(app);
	this.app = app;
	this.job = job;
	area = new NavigationArea(getControlContext()){
		@Override public String getLine(int index) { return index < text.size()?text.get(index):""; }
		@Override public int getLineCount() { return Math.max(text.size(), 1); }
		@Override public String getAreaName() { return job.getInstanceName(); }
	    };
	final var actions = actions(
				    action("cancel", "cancel", new InputEvent(Special.F5), this::onCancel)
				    );
	setAreaLayout(area, actions);
	setCloseHandler(()->{
		app.layouts().main();
		return true;
	    });
    }

boolean onCancel()
{
    job.stop();
    return true;
}

    public void update(List<String> text)
    {
	this.text.clear();
	this.text.addAll(text);
	area.redraw();
    }
}
