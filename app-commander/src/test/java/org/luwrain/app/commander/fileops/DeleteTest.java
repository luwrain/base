// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2026 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.app.commander.fileops;

import java.io.*;
import java.nio.file.*;
import java.util.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.*;

import static org.junit.jupiter.api.Assertions.*;

final class DeleteTest
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

    @Test void deletesSingleFile() throws Exception
    {
	final Path file = Files.writeString(tempDir.resolve("file.txt"), "hello");

	new Delete(listener, "delete", new Path[]{file}).run();

	assertFalse(Files.exists(file));
    }

    @Test void deletesDirectoryRecursively() throws Exception
    {
	final Path dir = Files.createDirectory(tempDir.resolve("dir"));
	final Path nested = Files.createDirectory(dir.resolve("nested"));
	Files.writeString(dir.resolve("root.txt"), "root");
	Files.writeString(nested.resolve("nested.txt"), "nested");

	new Delete(listener, "delete", new Path[]{dir}).run();

	assertFalse(Files.exists(dir));
    }

    @Test void reportsFullProgress() throws Exception
    {
	final Path file = Files.writeString(tempDir.resolve("file.txt"), "hello");

	final Delete op = new Delete(listener, "delete", new Path[]{file});
	op.run();

	assertEquals(100, op.getPercent());
    }

    @Test void respectsInterruption() throws Exception
    {
	final Path first = Files.writeString(tempDir.resolve("first.txt"), "first");
	final Path second = Files.writeString(tempDir.resolve("second.txt"), "second");

	final Delete op = new Delete(listener, "delete", new Path[]{first, second});
	op.interrupt();
	op.run();

	assertInstanceOf(OperationCancelledException.class, op.getException());
	assertTrue(Files.exists(first));
	assertTrue(Files.exists(second));
    }
}