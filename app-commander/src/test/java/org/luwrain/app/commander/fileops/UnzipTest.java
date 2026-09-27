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

final class UnzipTest
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

    @Test void extractsFilesAndDirectories() throws Exception
    {
	final Path zip = makeZip(Map.of("root.txt", "root", "nested/nested.txt", "nested"));
	final Path dest = tempDir.resolve("dest");

	new Unzip(listener, "unzip", zip, dest).run();

	assertEquals("root", Files.readString(dest.resolve("root.txt")));
	assertEquals("nested", Files.readString(dest.resolve("nested").resolve("nested.txt")));
    }

    @Test void skipsUnsafeEntries() throws Exception
    {
	final Path zip = tempDir.resolve("unsafe.zip");
	try (final ZipOutputStream out = new ZipOutputStream(Files.newOutputStream(zip))) {
	    out.putNextEntry(new ZipEntry("../evil.txt"));
	    out.write("evil".getBytes(java.nio.charset.StandardCharsets.UTF_8));
	    out.closeEntry();
	}
	final Path dest = tempDir.resolve("dest");

	final Unzip op = new Unzip(listener, "unzip", zip, dest);
	op.run();

	assertNull(op.getException());
	assertFalse(Files.exists(tempDir.resolve("evil.txt")));
    }

    @Test void reportsFullProgress() throws Exception
    {
	final Path zip = makeZip(Map.of("file.txt", "data"));
	final Path dest = tempDir.resolve("dest");

	final Unzip op = new Unzip(listener, "unzip", zip, dest);
	op.run();

	assertEquals(100, op.getPercent());
    }

    private Path makeZip(Map<String, String> entries) throws IOException
    {
	final Path zip = tempDir.resolve("test.zip");
	try (final ZipOutputStream out = new ZipOutputStream(Files.newOutputStream(zip))) {
	    for(final var entry: entries.entrySet())
	    {
		out.putNextEntry(new ZipEntry(entry.getKey()));
		out.write(entry.getValue().getBytes(java.nio.charset.StandardCharsets.UTF_8));
		out.closeEntry();
	    }
	}
	return zip;
    }
}