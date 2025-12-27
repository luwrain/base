// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.app.commander;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.*;

import org.luwrain.core.*;
import org.luwrain.core.events.*;
import org.luwrain.controls.*;
import org.luwrain.app.base.*;

final class UnixPropertiesLayout extends LayoutBase
{
    private final App app;
    final FormArea formArea;

    UnixPropertiesLayout(App app, File[] files, Runnable closing)
    {
	super(app);
	NullCheck.notNullItems(files, "files");
	NullCheck.notNull(closing, "closing");
	this.app = app;
	this.formArea = new FormArea(getControlContext()){
		@Override public String getAreaName()
		{
		    return app.getStrings().infoAreaName();
		}
	    };
	setAreaLayout(formArea, null);
    }
}
