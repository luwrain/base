// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2026 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.app.commander.fileops;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.zip.*;

import org.luwrain.core.*;
import org.luwrain.app.commander.*;
import org.luwrain.util.*;

import static java.util.Objects.*;

public final class ZipCompress extends Operation
{
    private final Path[] toCompress;
    private final Path zipFile;
    private int totalItems = 0;
    private int processedItems = 0;
    private int percent = 0;

    public ZipCompress(OperationListener listener, String name,
		       Path[] toCompress, Path zipFile)
    {
	super(listener, name);
	this.toCompress = toCompress;
	this.zipFile = zipFile;
	ensureValidLocalPath(toCompress);
	ensureValidLocalPath(zipFile);
	if (toCompress.length == 0)
	    throw new IllegalArgumentException("toCompress may not be empty");
    }

    @Override protected void work() throws IOException
    {
	totalItems = countItems(toCompress);
	if (exists(zipFile, false))
	{
	    switch(confirmOverwrite(zipFile))
	    {
	    case SKIP:
		return;
	    case CANCEL:
		throw new OperationCancelledException();
	    case OVERWRITE:
		break;
	    }
	    Files.delete(zipFile);
	}
	final Path root = archiveRoot();
	try (final OutputStream os = Files.newOutputStream(zipFile)) {
	    try(final ZipOutputStream zip = new ZipOutputStream(os)) {
		for(Path p: toCompress)
		    add(p, root, zip);
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

    private Path archiveRoot()
    {
	final Path first = toCompress[0];
	if (toCompress.length == 1 && Files.isDirectory(first, LinkOption.NOFOLLOW_LINKS))
	    return first;
	final Path parent = first.getParent();
	return parent != null?parent:first;
    }

    private void add(Path path, Path root, ZipOutputStream zip) throws IOException
    {
	checkInterrupted();
	requireNonNull(path, "path");
	if (Files.isSymbolicLink(path))
	{
	    addSymlink(path, root, zip);
	    return;
	}
	if (isRegularFile(path, false))
	{
	    addFile(path, root, zip);
	    return;
	}
	if (isDirectory(path, false))
	{
	    addDirectory(path, root, zip);
	    for(Path p: getDirContent(path))
		add(p, root, zip);
	}
    }

    private void addDirectory(Path path, Path root, ZipOutputStream zip) throws IOException
    {
	final String name = entryName(path, root, true);
	final ZipEntry entry = new ZipEntry(name);
	zip.putNextEntry(entry);
	zip.closeEntry();
	onItemProcessed();
    }

    private void addSymlink(Path path, Path root, ZipOutputStream zip) throws IOException
    {
	final Path link = Files.readSymbolicLink(path);
	final String entryName = entryName(path, root, false);
	final ZipEntry entry = new ZipEntry(entryName);
	entry.setComment("symlink:" + link.toString());
	zip.putNextEntry(entry);
	zip.closeEntry();
	onItemProcessed();
    }

    private void addFile(Path file, Path root, ZipOutputStream zip) throws IOException
    {
	requireNonNull(file, "file");
	try (final InputStream is = Files.newInputStream(file)) {
	    final ZipEntry entry = new ZipEntry(entryName(file, root, false));
	    zip.putNextEntry(entry);
	    StreamUtils.copyAllBytes(is, zip, (chunk, total) -> {}, ()->interrupted);
	    zip.closeEntry();
	    if (interrupted)
		throw new OperationCancelledException();
	    onItemProcessed();
	}
    }

    private String entryName(Path path, Path root, boolean directory)
    {
	final String name;
	if (path.equals(root))
	    name = "";
	else
	    name = root.toAbsolutePath().normalize().relativize(path.toAbsolutePath().normalize()).toString().replace(File.separatorChar, '/');
	if (directory && !name.endsWith("/"))
	    return name + "/";
	if (name.isEmpty())
	    return directory?"/":"";
	return name;
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

    static private int countItems(Path[] paths) throws IOException
    {
	int count = 0;
	for(Path p: paths)
	    count += countItems(p);
	return count;
    }

    static private int countItems(Path path) throws IOException
    {
	if (Files.isSymbolicLink(path))
	    return 1;
	if (isRegularFile(path, false))
	    return 1;
	if (isDirectory(path, false))
	{
	    int count = 1;
	    for(Path p: getDirContent(path))
		count += countItems(p);
	    return count;
	}
	return 0;
    }
}