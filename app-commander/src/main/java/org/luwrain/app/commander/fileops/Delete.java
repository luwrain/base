// SPDX-License-Identifier: Apache-2.0
// Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.app.commander.fileops;

import java.io.*;
import java.nio.file.Path;

import org.luwrain.core.*;
import org.luwrain.app.commander.*;

public class Delete extends Operation
{
    private final Path[] toDelete;

    public Delete(OperationListener listener, String name, Path[] toDelete)
    {
	super(listener, name);
	this.toDelete = toDelete;
	NullCheck.notNullItems(toDelete, "toDelete");
	NullCheck.notEmptyArray(toDelete, "toDelete");
	for(int i = 0;i < toDelete.length;++i)
	    if (!toDelete[i].isAbsolute())
		throw new IllegalArgumentException("toDelete[" + i + "] must be absolute");
    }

    @Override protected void work() throws IOException
    {
	Log.debug("proba", "starting");
	for(Path p: toDelete)
	{
	    Log.debug("proba", "deleting " + p);
	    deleteFileOrDir(p);
	}
    }

    public int getPercent()
    {
	return 0;
    }
}
