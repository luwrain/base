// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2026 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.app.commander.fileops;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.zip.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.*;

import static org.junit.jupiter.api.Assertions.*;

final class ZipCompressTest
{
    @TempDir Path tempDir;

    private OperationListener listener;

    @BeforeEach void prepareListener()
    {
	listener = new OperationListener(){
		@Override public void onOperationProgress(Operation operation)
		{
		}
	    };
    }

    @Test void compressesFilesWithRelativeEntryNames() throws Exception
    {
	final Path first = Files.writeString(tempDir.resolve("first.txt"), "first");
	final Path second = Files.writeString(tempDir.resolve("second.txt"), "second");
	final Path zip = tempDir.resolve("archive.zip");

	new ZipCompress(listener, "zip", new Path[]{first, second}, zip).run();

	final List<String> names = entryNames(zip);
	assertTrue(names.contains("first.txt"));
	assertTrue(names.contains("second.txt"));
    }

    @Disabled
    @Test void compressesDirectoryRecursively() throws Exception
    {
	final Path dir = Files.createDirectory(tempDir.resolve("dir"));
	final Path nested = Files.createDirectory(dir.resolve("nested"));
	Files.writeString(dir.resolve("root.txt"), "root");
	Files.writeString(nested.resolve("nested.txt"), "nested");
	final Path zip = tempDir.resolve("archive.zip");

	new ZipCompress(listener, "zip", new Path[]{dir}, zip).run();

	final List<String> names = entryNames(zip);
	assertTrue(names.contains("dir/"));
	assertTrue(names.contains("dir/root.txt"));
	assertTrue(names.contains("dir/nested/nested.txt"));
    }

    @Test void skipsWhenArchiveExistsAndListenerSkips() throws Exception
    {
	final Path source = Files.writeString(tempDir.resolve("first.txt"), "first");
	final Path zip = Files.writeString(tempDir.resolve("archive.zip"), "existing");
	listener = new OperationListener(){
		@Override public void onOperationProgress(Operation operation)
		{
		}

		@Override public Operation.ConfirmationChoices confirmOverwrite(Path path)
		{
		    return Operation.ConfirmationChoices.SKIP;
		}
	    };

	new ZipCompress(listener, "zip", new Path[]{source}, zip).run();

	assertEquals("existing", Files.readString(zip));
    }

    @Test void reportsFullProgress() throws Exception
    {
	final Path source = Files.writeString(tempDir.resolve("first.txt"), "first");
	final Path zip = tempDir.resolve("archive.zip");

	final ZipCompress op = new ZipCompress(listener, "zip", new Path[]{source}, zip);
	op.run();

	assertEquals(100, op.getPercent());
    }

    private List<String> entryNames(Path zip) throws IOException
    {
	final List<String> names = new ArrayList<>();
	try (final ZipFile file = new ZipFile(zip.toFile())) {
	    final Enumeration<? extends ZipEntry> entries = file.entries();
	    while(entries.hasMoreElements())
		names.add(entries.nextElement().getName());
	}
	return names;
    }
}
