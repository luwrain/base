// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2026 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.app.viewer;

import java.util.*;

import org.luwrain.core.*;
import org.luwrain.controls.*;
import org.luwrain.app.base.*;

import static org.luwrain.core.DefaultEventResponse.*;

final class MainLayout extends LayoutBase
{
    private final App app;
    final NavigationArea infoArea;

    private List<String> info = new ArrayList<>();

    MainLayout(App app)
    {
	super(app);
	this.app = app;
	this.infoArea = new NavigationArea(getControlContext()){
		@Override public int getLineCount()
		{
		    final int count = info.size();
		    return count >= 1?count:1;
		}
		@Override public String getLine(int index)
		{
		    if (index < info.size())
			return info.get(index);
		    return "";
		}
		@Override public String getAreaName() { return app.getStrings().appName(); }
	    };
	setAreaLayout(infoArea, null);
    }
}
