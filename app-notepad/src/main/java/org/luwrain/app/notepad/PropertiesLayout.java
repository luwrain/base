// SPDX-License-Identifier: Apache-2.0
// Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.app.notepad;

import java.util.*;
import java.io.*;

import org.luwrain.core.*;
import org.luwrain.core.events.*;
import org.luwrain.controls.*;
import org.luwrain.app.base.*;

final class PropertiesLayout extends LayoutBase
{
    private final App app;
    private final SimpleArea propsArea;

    PropertiesLayout(App app, String[] lines, Runnable closing)
    {
	NullCheck.notNull(app, "app");
	NullCheck.notNullItems(lines, "lines");
	NullCheck.notNull(closing, "closing");
	this.app = app;
	this.propsArea = new SimpleArea(new DefaultControlContext(app.getLuwrain()), app.getStrings().propsAreaName()) {
		@Override public boolean onInputEvent(InputEvent event)
		{
		    NullCheck.notNull(event, "event");
		    if (app.onInputEvent(this, event, closing))
			return true;
		    return super.onInputEvent(event);
		}
		@Override public boolean onSystemEvent(SystemEvent event)
		{
		    NullCheck.notNull(event, "event");
		    if (app.onSystemEvent(this, event))
			return true;
		    			return super.onSystemEvent(event);
		}
		@Override public boolean onAreaQuery(AreaQuery query)
		{
		    NullCheck.notNull(query, "query");
		    if (app.onAreaQuery(this, query))
			return true;
		    return super.onAreaQuery(query);
		}
	    };
	propsArea.update((text)->{
text.addLine("");
	for(String s: lines)
	    text.addLine(s);
	text.addLine("");
	    });
    }

    AreaLayout getLayout()
    {
	return new AreaLayout(propsArea);
    }
}
