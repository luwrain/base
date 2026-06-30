// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2026 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.app.video;

import java.util.*;
import java.io.*;
import java.nio.file.*;

import org.luwrain.core.*;
import org.luwrain.controls.*;
import org.luwrain.controls.list.*;
import org.luwrain.app.base.*;

import static org.luwrain.core.DefaultEventResponse.*;

final class MainLayout extends LayoutBase implements ListArea.ClickHandler<Path>
{
    private final App app;
    final ListArea<Path> filesArea;

    MainLayout(App app)
    {
	super(app);
	this.app = app;
	this.filesArea = new ListArea<Path>(listParams(p -> {
		    p.name = app.getStrings().appName();
		    p.model = new ListModel<>(app.files);
		    p.appearance = new FileAppearance(app);
		    p.clickHandler = this;
		}));
	setAreaLayout(filesArea, null);
    }

    @Override public boolean onListClick(ListArea<Path> area, int index, Path path)
    {
	try {
	    app.openVideo(path);
	}
	catch(IOException e)
	{
	    app.crash(e);
	}
	return true;
    }

    private static final class FileAppearance extends AbstractAppearance<Path>
    {
	final App app;

	FileAppearance(App app)
	{
	    this.app = app;
	}

	@Override public void announceItem(Path path, Set<Flags> flags)
	{
	    app.setEventResponse(listItem(path.getFileName() != null ? path.getFileName().toString() : ""));
	}

	@Override public String getScreenAppearance(Path path, Set<Flags> flags)
	{
	    return path.getFileName() != null ? path.getFileName().toString() : "";
	}
    }
}
