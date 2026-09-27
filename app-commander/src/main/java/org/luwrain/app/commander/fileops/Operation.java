// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2026 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.app.commander.fileops;

import java.util.*;
import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.*;

import org.luwrain.core.*;
import org.luwrain.app.commander.*;

import static java.util.Objects.*;

public abstract class Operation implements Runnable
{
    static public final String
	INTERRUPTED = "LWR_INTERRUPTED",
	SOURCE_IS_A_PARENT_OF_THE_DEST = "LWR_SOURCE_IS_A_PARENT_OF_THE_DEST",
	MOVE_DEST_NOT_DIR = "LWR_MOVE_DEST_NOT_DIR";

    public enum ConfirmationChoices {
	OVERWRITE,
	SKIP,
	CANCEL
    }

    private final OperationListener listener;
    public final String name;
    private boolean finished = false;
    private Throwable ex = null;
    private boolean finishingAccepted = false;
    protected boolean interrupted = false;

    Operation(OperationListener listener, String name)
    {
	this.listener = requireNonNull(listener, "listener");
	this.name = requireNonNull(name, "name");
	if (name.trim().isEmpty())
	    throw new IllegalArgumentException("name may not be empty");
    }

    protected abstract void work() throws IOException;
    public abstract int getPercent();

    @Override public void run()
    {
	this.ex = null;
	try {
	    try {
		if (interrupted)
		    throw new OperationCancelledException();
		work();
	    }
	    catch(OperationCancelledException e)
	    {
		this.ex = e;
	    }
	    catch (Throwable e)
	    {
		this.ex = e;
		Log.error("commander", name + ": " + e.getClass().getSimpleName() + ": " + e.getMessage());
	    }
	}
	finally {
	    finished = true;
	    listener.onOperationProgress(this);
	}
    }

    public synchronized void interrupt()
    {
	interrupted = true;
    }

    public boolean isDone()
    {
	return finished;
    }

    public boolean finishingAccepted()
    {
	if (finishingAccepted)
	    return true;
	finishingAccepted = true;
	return false;
    }

    public Throwable getException()
    {
	return this.ex;
    }

    protected final OperationListener getListener()
    {
	return listener;
    }

    protected final boolean checkInterrupted() throws OperationCancelledException
    {
	if (!interrupted)
	    return false;
	throw new OperationCancelledException();
    }

    static protected boolean isDirectory(Path path, boolean followSymlinks) throws IOException
    {
	if (followSymlinks)
	    return Files.isDirectory(path); else
	    return Files.isDirectory(path, LinkOption.NOFOLLOW_LINKS);
    }

    static protected Path[] getDirContent(final Path path) throws IOException
    {
	requireNonNull(path, "path");
	final List<Path> res = new ArrayList<>();
	try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(path)) {
	    for (Path p : directoryStream)
		res.add(p);
	}
	return res.toArray(new Path[res.size()]);
    }

    static protected boolean isRegularFile(Path path, boolean followSymlinks) throws IOException
    {
	requireNonNull(path, "path");
	if (followSymlinks)
	    return Files.isRegularFile(path); else
	    return Files.isRegularFile(path, LinkOption.NOFOLLOW_LINKS);
    }

    static protected boolean exists(Path path, boolean followSymlinks) throws IOException
    {
	requireNonNull(path, "path");
	if (followSymlinks)
	    return Files.exists(path); else
	    return Files.exists(path, LinkOption.NOFOLLOW_LINKS);
    }

    protected void deleteFileOrDir(Path p) throws IOException
    {
	requireNonNull(p, "p");
	checkInterrupted();
	if (isDirectory(p, false))
	{
	    final Path[] content = getDirContent(p);
	    for(Path pp: content)
		deleteFileOrDir(pp);
	}
	Files.delete(p);
    }

    protected final void status(String message)
    {
	requireNonNull(message, "message");
	Log.debug("fileops", message);
	listener.onStatus(this, message);
    }

    protected final ConfirmationChoices confirmOverwrite(Path path)
    {
	requireNonNull(path, "path");
	final ConfirmationChoices res = listener.confirmOverwrite(path);
	return res != null?res:ConfirmationChoices.SKIP;
    }

    protected final void onProgress()
    {
	listener.onOperationProgress(this);
    }

    static long getTotalSize(Path p) throws IOException
    {
	requireNonNull(p, "p");
	if (Files.isRegularFile(p, LinkOption.NOFOLLOW_LINKS))
	    return Files.size(p);
	if (!Files.isDirectory(p, LinkOption.NOFOLLOW_LINKS))
	    return 0;
	long res = 0;
	try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(p)) {
	    for (Path pp : directoryStream)
		res += getTotalSize(pp);
	}
	return res;
    }

    static protected void ensureValidLocalPath(Path[] paths)
    {
	requireNonNull(paths, "paths");
	for(Path p: paths)
	    ensureValidLocalPath(p);
    }

    static protected void ensureValidLocalPath(Path path)
    {
	requireNonNull(path, "path");
	if (!path.isAbsolute())
	    throw new IllegalArgumentException(path.toString() + " can't be relative");
    }
}