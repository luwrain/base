// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2026 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.app.commander.fileops;

import java.io.*;
import java.nio.file.*;
import java.util.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.*;

import static org.junit.jupiter.api.Assertions.*;

final class MoveTest
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

    @Test void movesSingleFileToNewName() throws Exception
    {
	final Path src = Files.writeString(tempDir.resolve("src.txt"), "hello");
	final Path dest = tempDir.resolve("dest.txt");

	new Move(listener, "move", new Path[]{src}, dest).run();

	assertFalse(Files.exists(src));
	assertEquals("hello", Files.readString(dest));
    }

    @Test void movesSingleFileIntoExistingDirectory() throws Exception
    {
	final Path src = Files.writeString(tempDir.resolve("src.txt"), "hello");
	final Path destDir = Files.createDirectory(tempDir.resolve("dest"));

	new Move(listener, "move", new Path[]{src}, destDir).run();

	assertFalse(Files.exists(src));
	assertEquals("hello", Files.readString(destDir.resolve("src.txt")));
    }

    @Test void movesMultipleFilesIntoDirectory() throws Exception
    {
	final Path first = Files.writeString(tempDir.resolve("first.txt"), "first");
	final Path second = Files.writeString(tempDir.resolve("second.txt"), "second");
	final Path destDir = Files.createDirectory(tempDir.resolve("dest"));

	new Move(listener, "move", new Path[]{first, second}, destDir).run();

	assertFalse(Files.exists(first));
	assertFalse(Files.exists(second));
	assertEquals("first", Files.readString(destDir.resolve("first.txt")));
	assertEquals("second", Files.readString(destDir.resolve("second.txt")));
    }

    @Test void rejectsNonDirectoryDestinationForMultipleSources() throws Exception
    {
	final Path first = Files.writeString(tempDir.resolve("first.txt"), "first");
	final Path second = Files.writeString(tempDir.resolve("second.txt"), "second");
	final Path dest = Files.writeString(tempDir.resolve("dest.txt"), "dest");

	final Operation op = new Move(listener, "move", new Path[]{first, second}, dest);
	op.run();

	assertNotNull(op.getException());
	assertTrue(Files.exists(first));
	assertTrue(Files.exists(second));
    }

    @Test void rejectsDestinationUnderSource() throws Exception
    {
	final Path dir = Files.createDirectory(tempDir.resolve("dir"));
	Files.writeString(dir.resolve("file.txt"), "data");

	final Operation op = new Move(listener, "move", new Path[]{dir}, dir.resolve("child"));
	op.run();

	assertInstanceOf(OperationCancelledException.class, op.getException());
	assertTrue(Files.exists(dir));
    }
}