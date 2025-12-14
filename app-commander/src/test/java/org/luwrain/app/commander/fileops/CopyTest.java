// SPDX-License-Identifier: Apache-2.0
// Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.app.commander.fileops;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.*;
import java.util.*;
import org.luwrain.util.*;

import static java.nio.file.Files.*;
import static org.luwrain.util.FileUtils.*;
import static org.luwrain.util.Sha1.*;

public class CopyTest
{
    private TempDir d = null;
    private OperationListener listener = (op) -> {};
    

    
    @Test void single1() throws Exception
    {
	final var files = new HashMap<String, String>();
	int len = 2;
	for(int i = 0;i < 14;i++)
	{
	    final var hash = writeRandomFile(d.getPath().resolve("src" + len), len);
	    files.put("src" + len, hash);
	    len *= 2;
	}
	len = 5;
	for(int i = 0;i < 100;i++)
	{
	    final var hash = writeRandomFile(d.getPath().resolve("src" + len), len);
	    files.put("src" + len, hash);
	    len += 5;
	}
	len = 10;
	for(int i = 0;i < 7;i++)
	{
	    final var hash = writeRandomFile(d.getPath().resolve("src" + len), len);
	    files.put("src" + len, hash);
	    len *= 10;
	}
	for(final var e: files.entrySet())
	{
	    final var p = new CopyMoveParams();
	    p.setListener(listener);
	    p.setName("test");
	    p.setSource(List.of(d.getPath().resolve(e.getKey())));
	    p.setDest(d.getPath().resolve(e.getKey() + "-res"));
	    new Copy(p).run();
	    assertEquals(e.getValue(), getSha1(d.getPath().resolve(e.getKey() + "-res")));
	}
    }

        @Test void multipleToNewDir() throws Exception
    {
	final var files = new HashMap<String, String>();
	int len = 2;
	for(int i = 0;i < 14;i++)
	{
	    final var hash = writeRandomFile(d.getPath().resolve("src" + len), len);
	    files.put("src" + len, hash);
	    len *= 2;
	}
	len = 5;
	for(int i = 0;i < 100;i++)
	{
	    final var hash = writeRandomFile(d.getPath().resolve("src" + len), len);
	    files.put("src" + len, hash);
	    len += 5;
	}
	len = 10;
	for(int i = 0;i < 7;i++)
	{
	    final var hash = writeRandomFile(d.getPath().resolve("src" + len), len);
	    files.put("src" + len, hash);
	    len *= 10;
	}
	    final var p = new CopyMoveParams();
	    p.setListener(listener);
	    p.setName("test");
	    p.setSource(files.entrySet().stream().map(e -> d.getPath().resolve(e.getKey())).toList());
	    p.setDest(d.getPath().resolve("newdir"));
	    new Copy(p).run();
	    	for(final var e: files.entrySet())
		    assertEquals(e.getValue(), getSha1(d.getPath().resolve("newdir").resolve(e.getKey())));
    }

            @Test void multipleToNewDirs() throws Exception
    {
	final var files = new HashMap<String, String>();
	int len = 2;
	for(int i = 0;i < 14;i++)
	{
	    final var hash = writeRandomFile(d.getPath().resolve("src" + len), len);
	    files.put("src" + len, hash);
	    len *= 2;
	}
	len = 5;
	for(int i = 0;i < 100;i++)
	{
	    final var hash = writeRandomFile(d.getPath().resolve("src" + len), len);
	    files.put("src" + len, hash);
	    len += 5;
	}
	len = 10;
	for(int i = 0;i < 7;i++)
	{
	    final var hash = writeRandomFile(d.getPath().resolve("src" + len), len);
	    files.put("src" + len, hash);
	    len *= 10;
	}
	    final var p = new CopyMoveParams();
	    p.setListener(listener);
	    p.setName("test");
	    p.setSource(files.entrySet().stream().map(e -> d.getPath().resolve(e.getKey())).toList());
	    p.setDest(d.getPath().resolve("newdir1").resolve("newdir2"));
	    new Copy(p).run();
	    	for(final var e: files.entrySet())
		    assertEquals(e.getValue(), getSha1(d.getPath().resolve("newdir1").resolve("newdir2").resolve(e.getKey())));
    }

            @Test void multipleToExistingDir() throws Exception
    {
	final var files = new HashMap<String, String>();
	int len = 2;
	for(int i = 0;i < 14;i++)
	{
	    final var hash = writeRandomFile(d.getPath().resolve("src" + len), len);
	    files.put("src" + len, hash);
	    len *= 2;
	}
	len = 5;
	for(int i = 0;i < 100;i++)
	{
	    final var hash = writeRandomFile(d.getPath().resolve("src" + len), len);
	    files.put("src" + len, hash);
	    len += 5;
	}
	len = 10;
	for(int i = 0;i < 7;i++)
	{
	    final var hash = writeRandomFile(d.getPath().resolve("src" + len), len);
	    files.put("src" + len, hash);
	    len *= 10;
	}
		    createDirectories(d.getPath().resolve("newdir"));
	    final var p = new CopyMoveParams();
	    p.setListener(listener);
	    p.setName("test");
	    p.setSource(files.entrySet().stream().map(e -> d.getPath().resolve(e.getKey())).toList());
	    p.setDest(d.getPath().resolve("newdir"));
	    new Copy(p).run();
	    	for(final var e: files.entrySet())
		    assertEquals(e.getValue(), getSha1(d.getPath().resolve("newdir").resolve(e.getKey())));
    }

    @Test void symlink() throws Exception
    {
	final Path
	symlink = d.getPath().resolve("symlink"),
		destLink = d.getPath().resolve("destlink");
	createSymbolicLink(symlink, Paths.get("testfile"));
	assertTrue(isSymbolicLink(symlink));
	assertEquals(Paths.get("testfile"), readSymbolicLink(symlink));
		    final var p = new CopyMoveParams();
	    p.setListener(listener);
	    p.setName("test");
	    p.setSource(List.of(symlink));
	    p.setDest(destLink);
	    new Copy(p).run();
	    assertTrue(isSymbolicLink(destLink));
	assertEquals(Paths.get("testfile"), readSymbolicLink(destLink));
    }



    //multipleToExistingDir
    //symlink to a dir
    //symlink in a dir
    //error copying directory to iteself
    //rights

    
    @BeforeEach void createTempDir()
    {
	d = new TempDir();
    }

    @AfterEach void deleteTempDir()
    {
	d.close();
	d = null;
    }
}
