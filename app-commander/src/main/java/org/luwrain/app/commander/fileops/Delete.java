// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2026 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.app.commander.fileops;

import java.io.*;
import java.nio.file.*;

import org.luwrain.core.*;
import org.luwrain.app.commander.*;

import static java.util.Objects.*;

public final class Delete extends Operation
{
    private final Path[] toDelete;
    private int items = 0;
    private int deletedItems = 0;
    private int percent = 0;

    public Delete(OperationListener listener, String name, Path[] toDelete)
    {
	super(listener, name);
	this.toDelete = toDelete;
	ensureValidLocalPath(toDelete);
	if (toDelete.length == 0)
	    throw new IllegalArgumentException("toDelete may not be empty");
    }

    @Override protected void work() throws IOException
    {
	items = countItems(toDelete);
	if (items == 0)
	{
	    percent = 100;
	    onProgress();
	    return;
	}
	for(Path p: toDelete)
	{
	    checkInterrupted();
	    deleteFileOrDir(p);
	    onItemDeleted();
	}
    }

    @Override public int getPercent()
    {
	return percent;
    }

    private void onItemDeleted()
    {
	deletedItems++;
	final int newPercent = calcPercent(deletedItems, items);
	if (newPercent != percent)
	{
	    percent = newPercent;
	    onProgress();
	}
    }

    static private int calcPercent(int done, int total)
    {
	if (total == 0)
	    return 100;
	final long value = (done * 100L) / total;
	return value > 100?100:(int)value;
    }

    static private int countItems(Path[] paths) throws IOException
    {
	int count = 0;
	for(Path p: paths)
	    count += countItems(p);
	return count;
    }

    static private int countItems(Path path) throws IOException
    {
	requireNonNull(path, "path");
	if (isDirectory(path, false))
	{
	    int count = 1;
	    for(Path p: getDirContent(path))
		count += countItems(p);
	    return count;
	}
	return Files.exists(path, LinkOption.NOFOLLOW_LINKS)?1:0;
    }
}