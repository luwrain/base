// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2026 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.app.commander.fileops;

import java.util.*;
import java.io.*;
import java.nio.file.*;

import org.luwrain.core.*;
import org.luwrain.app.commander.*;

import static java.util.Objects.*;

public final class Move extends CopyingBase
{
    private final Path[] toMove;
    private final Path moveTo;

    public Move(OperationListener listener, String name,
		Path[] toMove, Path moveTo)
    {
	super(listener, name);
	this.toMove = toMove;
	this.moveTo = moveTo;
	ensureValidLocalPath(toMove);
	ensureValidLocalPath(moveTo);
	if (toMove.length == 0)
	    throw new IllegalArgumentException("toMove may not be empty");
    }

    @Override protected void work() throws IOException
    {
	Path dest = moveTo;
	for(Path path: toMove)
	    if (dest.normalize().startsWith(path.normalize()))
		throw new OperationCancelledException();
	if (toMove.length > 1)
	    multipleSource(dest); else
	    singleSource(dest);
    }

    private void multipleSource(Path dest) throws IOException
    {
	requireNonNull(dest, "dest");
	if (!isDirectory(dest, true))
 	    throw new java.nio.file.FileSystemException(dest.toString(), null, MOVE_DEST_NOT_DIR);
	for(Path p: toMove)
	{
	    checkInterrupted();
	    final Path d = dest.resolve(p.getFileName());
	    if (exists(d, false))
	    {
		switch(confirmOverwrite(d))
		{
		case SKIP:
		    continue;
		case CANCEL:
		    throw new OperationCancelledException();
		case OVERWRITE:
		    break;
		}
		//FIXME:		delete(d);
	    }
	    movePath(p, d);
	}
    }

    private void singleSource(Path dest) throws IOException
    {
	requireNonNull(dest, "dest");
	checkInterrupted();
	final Path d;
	if (exists(dest, false) && isDirectory(dest, true))
	    d = dest.resolve(toMove[0].getFileName()); else
	    d = dest;
	if (exists(d, false))
	{
	    switch(confirmOverwrite(d))
	    {
	    case SKIP:
		return;
	    case CANCEL:
		throw new OperationCancelledException();
	    case OVERWRITE:
		break;
	    }
	    //FIXME:	    delete(d);
	}
	movePath(toMove[0], d);
    }

    private void movePath(Path source, Path dest) throws IOException
    {
	requireNonNull(source, "source");
	requireNonNull(dest, "dest");
	try {
	    status("Moving " + source + " to " + dest);
	    Files.move(source, dest, StandardCopyOption.ATOMIC_MOVE);
	}
	catch(AtomicMoveNotSupportedException e)
	{
	    status("Atomic move is not supported, falling back to copy and delete");
	    moveThroughCopying(source, dest);
	}
	catch(FileSystemException e)
	{
	    if (!sameFileStore(source, dest))
	    {
		status("Moving across file systems, falling back to copy and delete");
		moveThroughCopying(source, dest);
	    } else
		throw e;
	}
    }

    private boolean sameFileStore(Path first, Path second)
    {
	try {
	    return Files.getFileStore(first).equals(Files.getFileStore(second));
	}
	catch(IOException e)
	{
	    return false;
	}
    }

    private void moveThroughCopying(Path source, Path dest) throws IOException
    {
	requireNonNull(source, "source");
	requireNonNull(dest, "dest");
	final var params = new CopyMoveParams(name, List.of(source), dest, getListener());
	copy(params);
	deleteFileOrDir(source);
    }
}
