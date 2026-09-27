// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2026 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.app.commander.fileops;

import java.io.*;
import java.nio.file.*;
import java.util.*;

import org.apache.commons.compress.archivers.zip.*;

import org.luwrain.core.*;
import org.luwrain.app.commander.*;
import org.luwrain.util.*;

import static java.util.Objects.*;

public final class Unzip extends Operation
{
    private final Path zipFile;
    private final Path destDir;
    private int totalItems = 0;
    private int processedItems = 0;
    private int percent = 0;

    public Unzip(OperationListener listener, String name, Path zipFile, Path destDir)
    {
	super(listener, name);
	this.zipFile = zipFile;
	this.destDir = destDir;
	ensureValidLocalPath(zipFile);
	ensureValidLocalPath(destDir);
    }

    @Override public void work() throws IOException
    {
	Files.createDirectories(destDir);
	try (final ZipFile zip = new ZipFile(zipFile.toFile(), "UTF-8", false)) {
	    final Enumeration<? extends ZipArchiveEntry> entries = zip.getEntries();
	    final List<ZipArchiveEntry> safeEntries = new ArrayList<>();
	    while(entries.hasMoreElements())
	    {
		checkInterrupted();
		final ZipArchiveEntry entry = entries.nextElement();
		final Path target = targetOf(entry);
		if (target == null)
		{
		    status("Skipping unsafe archive entry: " + entry.getName());
		    continue;
		}
		safeEntries.add(entry);
	    }
	    totalItems = safeEntries.size();
	    for(ZipArchiveEntry entry: safeEntries)
	    {
		checkInterrupted();
		final Path target = targetOf(entry);
		if (entry.isDirectory())
		{
		    Files.createDirectories(target);
		    onItemProcessed();
		    continue;
		}
		final Path parent = target.getParent();
		if (parent != null)
		    Files.createDirectories(parent);
		try (final InputStream is = zip.getInputStream(entry)) {
		    try (final OutputStream os = Files.newOutputStream(target)) {
			StreamUtils.copyAllBytes(is, os, (chunk, total) -> {}, ()->interrupted);
		    }
		}
		if (interrupted)
		    throw new OperationCancelledException();
		onItemProcessed();
	    }
	}
	if (totalItems == 0)
	{
	    percent = 100;
	    onProgress();
	}
    }

    @Override public int getPercent()
    {
	return percent;
    }

    private Path targetOf(ZipArchiveEntry entry) throws IOException
    {
	requireNonNull(entry, "entry");
	Path target = destDir.resolve(entry.getName()).normalize();
	if (!target.startsWith(destDir.normalize()))
	    return null;
	return target;
    }

    private void onItemProcessed()
    {
	processedItems++;
	final int newPercent = totalItems > 0?(int)Math.min(100L, (processedItems * 100L) / totalItems):100;
	if (newPercent != percent)
	{
	    percent = newPercent;
	    onProgress();
	}
    }
}