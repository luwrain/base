// SPDX-License-Identifier: Apache-2.0
// Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.app. notepad;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.*;
import java.util.*;
import org.apache.commons.io.*;

import org.luwrain.util.*;

import static java.nio.file.Files.*;
import static org.luwrain.app.notepad.TextFiles.*;

public final class TextFilesTest
{
    private TempDir d = null;

    @Test void charset() throws IOException
    {
	for(var i: List.of("utf-8", "cp1251", "koi8-r", "866"))
	{
	    var t = read(d.getPath().resolve(i + ".txt").toFile(), i);
	    assertNotNull(t);
	    assertEquals(2, t.size());
	    assertEquals("АБВГДабвгд", t.get(0));
	    assertTrue(t.get(1).isEmpty ());
	}
    }

    @BeforeEach void createTempDir() throws IOException
    {
	d = new TempDir();
	for(var i: List.of("utf-8.txt", "koi8-r.txt", "cp1251.txt", "866.txt"))
	{
	    try (var is = getClass().getResourceAsStream(i)) {
		try (var os = newOutputStream(d.getPath().resolve(i))) {
		    IOUtils.copy(is, os);
		    os.flush();
		}
	    }
	}
    }

    @AfterEach void deleteTempDir()
    {
	d.close();
	d = null;
    }
}
