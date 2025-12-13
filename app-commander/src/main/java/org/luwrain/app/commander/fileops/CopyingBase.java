// SPDX-License-Identifier: Apache-2.0
// Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.app.commander.fileops;

import java.util.*;
import java.io.*;
import java.nio.file.*;
import org.apache.logging.log4j.*;

import org.luwrain.core.*;
import org.luwrain.util.*;
import org.luwrain.app.commander.*;

import static java.util.Objects.*;
import static java.nio.file.Files.*;

abstract class CopyingBase extends Operation
{
    static private final Logger log = LogManager.getLogger();
    
    private long totalBytes = 0, processedBytes = 0;
    private int percent = 0, lastPercent = 0;

    CopyingBase(OperationListener listener, String name)
    {
	super(listener, name);
    }

        @Override public int getPercent()
    {
	return percent;
    }

    protected void copy(CopyMoveParams params) throws IOException
    {
	requireNonNull(params, "params can't be null");
	requireNonNull(params.getSource(), "source can't be null");
	requireNonNull(params.getDest(), "dest can't be null");
	if (params.getSource().isEmpty())
	    throw new IllegalArgumentException("source can't be empty");
	for(Path p: params.getSource())
	{
	    requireNonNull(p, "p can't be null");
	    if (!p.isAbsolute())
		throw new IllegalArgumentException("Paths of all source files must be absolute");
	}
	// Calculating the total size of the source files
	totalBytes = 0;
	for(Path f: params.getSource())
	{
	    log.trace("Calculating the size of " + f);
	    totalBytes += getTotalSize(f);
	}
	log.trace("Total size is " + String.valueOf(totalBytes));
	Path d = params.getDest();
	if (!d.isAbsolute())
	{
	    final Path parent = params.getSource().get(0).getParent();
	    requireNonNull(parent, "parent can't be null");
	    d = parent.resolve(d);
	    log.trace("absolute destination path:" + d.toString());
	}
	// Checking that d is not a child of any item of toCopy
	for(final var path: params.getSource())
	    if (d.startsWith(path))
		throw new IOException(SOURCE_IS_A_PARENT_OF_THE_DEST);
	if (params.getSource().size() == 1)
	    singleSource(params.getSource().get(0), d); else
	    multipleSource(params.getSource(), d);
    }

    private void singleSource(Path fileFrom, Path dest) throws IOException
    {
	log.trace("Single source mode:copying " + fileFrom + " to " + dest);
	// If the destination directory already exists, just copying whatever fileFrom is
	if (isDirectory(dest, true))
	{
	    log.trace("" + dest + " exists and is a directory (or a symlink to a directory), copying the source file to it");
	    copyRecurse(List.of(fileFrom), dest);
	    return;
	}
	// We sure the destination isn't a directory, maybe even doesn't exist
		    // If fileFrom is a directory, we should copy its content to newly created directory
	if (isDirectory(fileFrom, false))
	{
	    log.trace("" + fileFrom + " is a directory and isn\'t a symlink");
	    if (exists(dest, false)) // Dest can exist, but it's certainly not a directory
	    {
		switch(confirmOverwrite(dest))
		{
		case SKIP:
		    return;
		case CANCEL:
		    throw new IOException(INTERRUPTED);
		}
		log.trace("Deleting previously existing " + dest.toString());
		Files.delete(dest);
	    }
	    Files.createDirectories(dest);
	    // Copying the content of fileFrom to the newly created directory
	    copyRecurse(Arrays.asList(getDirContent(fileFrom)), dest);
	    return;
	}
	// We sure that fileFrom and dest aren't directories, but dest may exist
	if (!Files.isSymbolicLink(fileFrom) && !isRegularFile(fileFrom, false))
	{
	    log.trace("" + fileFrom + "is not a symlink and is not a regular file, nothing to do");
	    return;
	}
	log.trace("" + fileFrom + " is a symlink or a regular file");
	if (exists(dest, false))
	{
	    log.trace("" + dest + " exists, trying to overwrite it");
	    switch(confirmOverwrite(dest))
	    {
	    case SKIP:
		return;
	    case CANCEL:
		throw new OperationCancelledException();
	    }
	    Files.delete(dest);
	}
	// We must be sure that the parent directory of dest exists (but not dest itself)
	if (dest.getParent() != null)
	    Files.createDirectories(dest.getParent());
	copySingleFile(fileFrom, dest);//This takes care if fromFile is a symlink
    }

    private void multipleSource(List<Path> toCopy, Path dest) throws IOException
    {
	log.trace("Multiple source mode");
	if (exists(dest, false) && !isDirectory(dest, true))
	{
	    log.trace("" + dest.toString() + " exists and is not a directory");
	    switch(confirmOverwrite(dest))
	    {
	    case SKIP:
		return;
	    case CANCEL:
		throw new OperationCancelledException();
	    }
	    log.trace("Deleting previously existing " + dest.toString());
	    Files.delete(dest);
	}
	if (!exists(dest, false))//just for the case dest is a symlink to a directory
	    Files.createDirectories(dest);
	copyRecurse(toCopy, dest);
    }

    private void copyRecurse(List<Path> filesFrom, Path fileTo) throws IOException
    {
	log.trace("CopyRecurse:copying " + filesFrom.size() + " entries to " + fileTo);
	//toFile should already exist and should be a directory
	for(Path f: filesFrom)
	{
	    if (!isDirectory(f, false))
	    {
		log.trace("" + f.toString() + " is not a directory, copying it");
		copyFileToDir(f, fileTo);
		continue;
	    }
	    log.trace("" + f.toString() + " is a directory");
	    final Path newDest = fileTo.resolve(f.getFileName());
	    log.trace("New destination is " + newDest.toString());
	    if (exists(newDest, false) && !isDirectory(newDest, true))
	    {
		log.trace("" + newDest + " already exists and isn\'t a directory, asking confirmation and trying to delete it");
		switch(confirmOverwrite(newDest))
		{
		case SKIP:
		    continue;
		case CANCEL:
		    throw new OperationCancelledException();
		}
		status("deleting previously existing " + newDest.toString());
		Files.delete(newDest);
	    }
	    if (!exists(newDest, false))//just for the case newDest  is a symlink to a directory
		Files.createDirectories(newDest);
	    log.trace("" + newDest + " prepared");
	    copyRecurse(Arrays.asList(getDirContent(f)), newDest);
	}
    }

    private void copyFileToDir(Path file, Path destDir) throws IOException
    {
	requireNonNull(file, "file can't be null");
	requireNonNull(destDir, "destDir can't be null");
	copySingleFile(file, destDir.resolve(file.getFileName()));
    }

    private void copySingleFile(Path fromFile, Path toFile) throws IOException
    {
	requireNonNull(fromFile, "fromFile can't be null");
	requireNonNull(toFile, "toFile can't be null");
	if (exists(toFile, false))
	{
	    log.trace("" + toFile + " already exists");
	    switch(confirmOverwrite(toFile))
	    {
	    case SKIP:
		return;
	    case CANCEL:
		throw new OperationCancelledException();
	    }
	    delete(toFile);
	} // toFile exists
	if (isSymbolicLink(fromFile))
	{
	    createSymbolicLink(toFile, readSymbolicLink(fromFile));
	    return;
	}
	try (final var in = new BufferedInputStream(newInputStream(fromFile))) {
	    try (final var out = new BufferedOutputStream(newOutputStream(toFile))) {
		StreamUtils.copyAllBytes(in, out,
					 (chunkNumBytes, totalNumBytes) -> onNewChunk(chunkNumBytes), ()->interrupted);
		out.flush();
		if (interrupted)
		    throw new IOException("INTERRUPTED");
	    }
	}
    }

    private void onNewChunk(int bytes)
    {
	processedBytes += bytes;
	long lPercent = (processedBytes * 100) / totalBytes;
	percent = (int)lPercent;
	if (percent > lastPercent)
	{
	    onProgress(this);
	    lastPercent = percent;
	}
    }
}
