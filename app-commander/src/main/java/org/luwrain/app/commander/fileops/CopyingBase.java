// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2026 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.app.commander.fileops;

import java.util.*;
import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.*;

import org.apache.logging.log4j.*;

import org.luwrain.util.*;
import org.luwrain.app.commander.*;

import static java.util.Objects.*;
import static java.nio.file.Files.*;
import static java.nio.file.LinkOption.*;

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
	requireNonNull(params, "params");
	validateParams(params);
	resetProgress();
	for(Path f: params.getSource())
	    totalBytes += getTotalSize(f);
	if (totalBytes == 0)
	{
	    percent = 100;
	    lastPercent = 100;
	}
	Path d = params.getDest();
	if (!d.isAbsolute())
	{
	    final Path parent = params.getSource().get(0).getParent();
	    requireNonNull(parent, "parent");
	    d = parent.resolve(d);
	}
	for(final Path path: params.getSource())
	    if (d.normalize().startsWith(path.normalize()))
		throw new OperationCancelledException();
	if (params.getSource().size() == 1)
	    singleSource(params.getSource().get(0), d); else
	    multipleSource(params.getSource(), d);
    }

    private void singleSource(Path fileFrom, Path dest) throws IOException
    {
	checkInterrupted();
	log.trace("Single source mode: copying {} to {}", fileFrom, dest);
	if (isDirectory(dest, true))
	{
	    copyRecurse(List.of(fileFrom), dest);
	    return;
	}
	if (isDirectory(fileFrom, false))
	{
	    if (exists(dest, false))
	    {
		switch(confirmOverwrite(dest))
		{
		case SKIP:
		    return;
		case CANCEL:
		    throw new OperationCancelledException();
		case OVERWRITE:
		    break;
		}
		Files.delete(dest);
	    }
	    Files.createDirectories(dest);
	    copyRecurse(Arrays.asList(getDirContent(fileFrom)), dest);
	    return;
	}
	if (!isSymbolicLink(fileFrom) && !isRegularFile(fileFrom, false))
	    return;
	if (exists(dest, false))
	{
	    switch(confirmOverwrite(dest))
	    {
	    case SKIP:
		return;
	    case CANCEL:
		throw new OperationCancelledException();
	    case OVERWRITE:
		break;
	    }
	    delete(dest);
	}
	if (dest.getParent() != null)
	    createDirectories(dest.getParent());
	copySingleFile(fileFrom, dest);
    }

    private void multipleSource(List<Path> toCopy, Path dest) throws IOException
    {
	checkInterrupted();
	log.trace("Multiple source mode");
	if (exists(dest, false) && !isDirectory(dest, true))
	{
	    switch(confirmOverwrite(dest))
	    {
	    case SKIP:
		return;
	    case CANCEL:
		throw new OperationCancelledException();
	    case OVERWRITE:
		break;
	    }
	    delete(dest);
	}
	if (!exists(dest, false))
	    createDirectories(dest);
	copyRecurse(toCopy, dest);
    }

    private void copyRecurse(List<Path> filesFrom, Path fileTo) throws IOException
    {
	checkInterrupted();
	log.trace("copyRecurse: copying {} entries to {}", filesFrom.size(), fileTo);
	for(Path f: filesFrom)
	{
	    checkInterrupted();
	    if (!isDirectory(f, false))
	    {
		copyFileToDir(f, fileTo);
		continue;
	    }
	    final Path newDest = fileTo.resolve(f.getFileName());
	    if (exists(newDest, false) && !isDirectory(newDest, true))
	    {
		switch(confirmOverwrite(newDest))
		{
		case SKIP:
		    continue;
		case CANCEL:
		    throw new OperationCancelledException();
		case OVERWRITE:
		    break;
		}
		delete(newDest);
	    }
	    if (!exists(newDest, false))
		createDirectories(newDest);
	    copyRecurse(Arrays.asList(getDirContent(f)), newDest);
	}
    }

    private void copyFileToDir(Path file, Path destDir) throws IOException
    {
	requireNonNull(file, "file");
	requireNonNull(destDir, "destDir");
	copySingleFile(file, destDir.resolve(file.getFileName()));
    }

    private void copySingleFile(Path fromFile, Path toFile) throws IOException
    {
	requireNonNull(fromFile, "fromFile");
	requireNonNull(toFile, "toFile");
	checkInterrupted();
	if (exists(toFile, false))
	{
	    switch(confirmOverwrite(toFile))
	    {
	    case SKIP:
		return;
	    case CANCEL:
		throw new OperationCancelledException();
	    case OVERWRITE:
		break;
	    }
	    delete(toFile);
	}
	if (isSymbolicLink(fromFile))
	{
	    createSymbolicLink(toFile, readSymbolicLink(fromFile));
	    return;
	}
	final long singleSize = size(fromFile);
	try (final var in = new BufferedInputStream(newInputStream(fromFile))) {
	    try (final var out = new BufferedOutputStream(newOutputStream(toFile))) {
		StreamUtils.copyAllBytes(in, out,
					 (chunkNumBytes, totalNumBytes) -> onNewChunk(chunkNumBytes, singleSize),
					 ()->interrupted);
		out.flush();
	    }
	}
	if (interrupted)
	    throw new OperationCancelledException();
	copyAttributes(fromFile, toFile);
    }

    private void copyAttributes(Path fromFile, Path toFile)
    {
	try {
	    final BasicFileAttributes attrs = Files.readAttributes(fromFile, BasicFileAttributes.class,
								  NOFOLLOW_LINKS);
	    if (attrs != null)
		Files.setLastModifiedTime(toFile, attrs.lastModifiedTime());
	    try {
		final var posix = Files.getFileAttributeView(toFile, PosixFileAttributeView.class,
							   NOFOLLOW_LINKS);
		if (posix != null)
		{
		    final var posixAttrs = Files.readAttributes(fromFile, PosixFileAttributes.class,
							      NOFOLLOW_LINKS);
		    posix.setPermissions(posixAttrs.permissions());
		}
	    }
	    catch(UnsupportedOperationException | IOException ignored)
	    {
		// POSIX permission copying is optional.
	    }
	}
	catch(UnsupportedOperationException | IOException e)
	{
	    log.debug("Unable to copy attributes of {} to {}", fromFile, toFile, e);
	}
    }

    private void resetProgress()
    {
	totalBytes = 0;
	processedBytes = 0;
	percent = 0;
	lastPercent = 0;
    }

    private void onNewChunk(int bytes, long singleSize)
    {
	processedBytes += bytes;
	final long base = totalBytes > 0?totalBytes:Math.max(singleSize, 1L);
	final int newPercent = (int)Math.min(100L, (processedBytes * 100L) / base);
	if (newPercent != percent)
	{
	    percent = newPercent;
	    if (percent > lastPercent)
	    {
		lastPercent = percent;
		onProgress();
	    }
	}
    }

    static private void validateParams(CopyMoveParams params)
    {
	requireNonNull(params, "params");
	requireNonNull(params.getSource(), "source");
	requireNonNull(params.getDest(), "dest");
	if (params.getSource().isEmpty())
	    throw new IllegalArgumentException("source may not be empty");
	for(Path p: params.getSource())
	{
	    requireNonNull(p, "source item may not be null");
	    ensureValidLocalPath(p);
	}
    }
}