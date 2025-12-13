// SPDX-License-Identifier: Apache-2.0
// Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.app.notepad;

import java.util.concurrent.atomic.*;

import java.util.*;

import org.luwrain.core.*;
import org.luwrain.core.events.*;
import org.luwrain.controls.*;
import org.luwrain.controls.edit.*;
import org.luwrain.script.*;
import org.luwrain.script.hooks.*;

final class Hooks
{
    private final App app;

    Hooks(App app)
    {
	NullCheck.notNull(app, "app");
	this.app = app;
    }

    boolean runActionHooks(SystemEvent event, EditArea editArea)
    {
	/*
	NullCheck.notNull(event, "event");
	NullCheck.notNull(editArea, "editArea");
	if (!(event instanceof ActionEvent))
	    return false;
	final ActionEvent actionEvent = (ActionEvent)event;
	final MultilineEditCorrector corrector = (MultilineEditCorrector)editArea.getEdit().getMultilineEditModel();
	final AtomicBoolean res = new AtomicBoolean(false);
	corrector.doEditAction((lines, hotPoint)->{
		try {
		    res.set(new ChainOfResponsibilityHook(app.getLuwrain()).runNoExcept("luwrain.notepad.action", new Object[]{
				actionEvent.getActionName(),
				org.luwrain.script.TextScriptUtils.createTextEditHookObject(editArea, lines, hotPoint, editArea.getRegionPoint())
			    }));
		}
		catch(RuntimeException e)
		{
		    app.getLuwrain().crash(e);
		}
	    });
	return res.get();
	*/
	return false;
    }
}
