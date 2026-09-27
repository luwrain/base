// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2026 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.app.commander.fileops;

import java.io.*;
import java.nio.file.*;
import java.util.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.*;

import static org.junit.jupiter.api.Assertions.*;

final class CopyTest
{
    @TempDir Path tempDir;

    private final List<Operation> operations = new ArrayList<>();
    private OperationListener listener;

    @BeforeEach void prepareListener()
    {
	operations.clear();
	listener = new OperationListener(){
		@Override public void onOperationProgress(Operation operation)
		{
		}
	    };
    }

    @Test void copiesSingleFileToNewPath() throws Exception
    {
	final Path src = write("src.txt", "hello");
	final Path dest = tempDir.resolve("dest.txt");

	new Copy(params(List.of(src), dest)).run();

	assertEquals("hello", Files.readString(dest));
    }

    @Test void copiesMultipleFilesToNewNestedDirectory() throws Exception
    {
	final Path first = write("first.txt", "first");
	final Path second = write("second.txt", "second");
	final Path dest = tempDir.resolve("newdir1").resolve("newdir2");

	new Copy(params(List.of(first, second), dest)).run();

	assertEquals("first", Files.readString(dest.resolve("first.txt")));
	assertEquals("second", Files.readString(dest.resolve("second.txt")));
    }

    @Test void copiesDirectoryRecursively() throws Exception
    {
	final Path srcDir = Files.createDirectory(tempDir.resolve("src"));
	final Path nested = Files.createDirectory(srcDir.resolve("nested"));
	Files.writeString(srcDir.resolve("root.txt"), "root");
	Files.writeString(nested.resolve("nested.txt"), "nested");
	final Path destDir = tempDir.resolve("dest");

	new Copy(params(List.of(srcDir), destDir)).run();

	assertEquals("root", Files.readString(destDir.resolve("root.txt")));
	assertEquals("nested", Files.readString(destDir.resolve("nested").resolve("nested.txt")));
    }

    @Test void copiesSingleFileIntoExistingDirectory() throws Exception
    {
	final Path src = write("src.txt", "hello");
	final Path destDir = Files.createDirectory(tempDir.resolve("dest"));

	new Copy(params(List.of(src), destDir)).run();

	assertEquals("hello", Files.readString(destDir.resolve("src.txt")));
    }

    @Test void copiesSymlinkWithoutFollowing() throws Exception
    {
	final Path target = write("target.txt", "target");
	final Path symlink = tempDir.resolve("link");
	Files.createSymbolicLink(symlink, Path.of("target.txt"));
	final Path dest = tempDir.resolve("dest-link");

	new Copy(params(List.of(symlink), dest)).run();

	assertTrue(Files.isSymbolicLink(dest));
	assertEquals(Path.of("target.txt"), Files.readSymbolicLink(dest));
    }

    @Test void rejectsDestinationUnderSource() throws Exception
    {
	final Path srcDir = Files.createDirectory(tempDir.resolve("src"));
	Files.writeString(srcDir.resolve("file.txt"), "data");

	final Copy op = new Copy(params(List.of(srcDir), srcDir.resolve("child")));
	op.run();

	assertInstanceOf(OperationCancelledException.class, op.getException());
    }

    @Test void cancellationFromOverwriteConfirmationStopsCopy() throws Exception
    {
	final Path src = write("src.txt", "hello");
	final Path dest = write("dest.txt", "dest");
	listener = new OperationListener(){
		@Override public void onOperationProgress(Operation operation)
		{
		}

		@Override public Operation.ConfirmationChoices confirmOverwrite(Path path)
		{
		    return Operation.ConfirmationChoices.CANCEL;
		}
	    };

	final Copy op = new Copy(params(List.of(src), dest));
	op.run();

	assertInstanceOf(OperationCancelledException.class, op.getException());
	assertEquals("dest", Files.readString(dest));
    }

    @Test void reportsFullProgressForRegularFile() throws Exception
    {
	final Path src = write("src.txt", "hello");
	final Path dest = tempDir.resolve("dest.txt");

	final Copy op = new Copy(params(List.of(src), dest));
	op.run();

	assertEquals(100, op.getPercent());
    }

    private CopyMoveParams params(List<Path> source, Path dest)
    {
	final var params = new CopyMoveParams("copy", source, dest, listener);
	return params;
    }

    private Path write(String name, String content) throws IOException
    {
	final Path path = tempDir.resolve(name);
	Files.writeString(path, content);
	return path;
    }
}