// SPDX-License-Identifier: Apache-2.0
// Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>

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
