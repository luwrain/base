// SPDX-License-Identifier: Apache-2.0
// Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.app.commander.fileops;

import java.io.*;
import java.nio.file.*;
import java.util.*;

import org.apache.commons.compress.archivers.zip.*;


import org.luwrain.core.*;
import org.luwrain.app.commander.*;
import org.luwrain.util.*;

public final class Unzip extends Operation
{
    static private final String
	CHARSET = "cp866";

    private final Path zipFile;
    private final Path destDir;

    public Unzip(OperationListener listener, String name, Path zipFile, Path destDir)
    {
	super(listener, name);
	ensureValidLocalPath(zipFile);
	ensureValidLocalPath(destDir);
	this.zipFile = zipFile;
	this.destDir = destDir;
}

    @Override public void work() throws IOException
    {
	try (final ZipFile zipFile = new ZipFile(this.zipFile.toFile(), CHARSET, false)) {
	    final Enumeration enumEntry = zipFile.getEntries();
	    while(enumEntry.hasMoreElements())
	    {
		final ZipArchiveEntry entry = (ZipArchiveEntry) enumEntry.nextElement();
		try (final InputStream is = zipFile.getInputStream(entry)) {
		    //handler.onZippedFile(entry.getName(), is);
		}
	    }
	}
    }

    @Override public int getPercent()
    {
	return 0;
    }
}
