// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2026 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.app.commander;

import java.util.*;
import java.io.*;
import java.nio.file.*;

import org.luwrain.core.*;
import org.luwrain.core.events.*;
import org.luwrain.controls.*;

final class DirectoryInfoSecondArea extends NavigationArea implements SecondArea
{
    final App app;
    final Path path;
    final Runnable closing;
    private final List<String> lines = new ArrayList<>();
    
    DirectoryInfoSecondArea(App app, ControlContext controlContext, Path path, Runnable closing)
    {
	super(controlContext);
	this.app = app;
	this.path = path;
	this.closing = closing;
    }

    @Override public boolean cancel()
    {
	closing.run();
	return true;
    }
    
    @Override public int getLineCount()
    {
	return Math.max(lines.size(), 1);
    }

    @Override public String getLine(int index)
    {
	return index < lines.size() ? lines.get(index) : "";
    }

    @Override public String getAreaName()
    {
	return app.getStrings().directoryInfoAreaName();
    }
}
