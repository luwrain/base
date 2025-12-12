// SPDX-License-Identifier: Apache-2.0
// Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.app.commander;

import java.io.*;

import org.luwrain.core.*;
import org.luwrain.core.events.*;
import org.luwrain.controls.*;
import org.luwrain.app.base.*;

final class FilesInfoLayout extends LayoutBase
{
    private final App app;
    private final NavigationArea area;

    FilesInfoLayout(App app, File[] files, Runnable closing)
    {
	NullCheck.notNull(app, "app");
	NullCheck.notNullItems(files, "files");
	NullCheck.notNull(closing, "closing");
	this.app = app;
	this.area = new NavigationArea(new DefaultControlContext(app.getLuwrain())) {
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
		@Override public String getAreaName()
		{
		    return app.getStrings().infoAreaName();
		}
		@Override public int getLineCount()
		{
		    //		    return lines.getLineCount() > 0?lines.getLineCount():1;
		    return 1;
		}
		@Override public String getLine(int index)
		{
		    /*
		    if (index >= lines.getLineCount())
			return "";
		    return lines.getLine(index);
		    */
		    return "";
		}
	    };
    }

    AreaLayout getLayout()
    {
	return new AreaLayout(area);
    }
}
