/*
   Copyright 2012-2018 Michael Pozhidaev <michael.pozhidaev@gmail.com>

   This file is part of LUWRAIN.

   LUWRAIN is free software; you can redistribute it and/or
   modify it under the terms of the GNU General Public
   License as published by the Free Software Foundation; either
   version 3 of the License, or (at your option) any later version.

   LUWRAIN is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
   General Public License for more details.
*/

package org.luwrain.app.notepad;

import org.junit.*;

import org.luwrain.core.*;

public class TextAligningTest extends Assert
{
    @Test public void simple() throws Exception
    {
	final TextAligning t = new TextAligning(5);
	assertTrue(t.origHotPointX == -1);
	assertTrue(t.origHotPointY == -1);
	t.origLines = new String[]{"aaa"};
	t.align();
	assertTrue(t.res.size() == 1);
	assertTrue(t.res.get(0).equals("aaa"));
	assertTrue(t.hotPointX == -1);
	assertTrue(t.hotPointY == -1);
	for(int i = 0;i < 3;++i)
	{
	    t.origHotPointY = 0;
	    t.origHotPointX = i;
	    t.align();
	    assertTrue(t.res.size() == 1);
	    assertTrue(t.res.get(0).equals("aaa"));
	    assertTrue(t.hotPointX == i);
	    assertTrue(t.hotPointY == 0);
	}
    }

    @Test public void betweenSpaces() throws Exception
    {
	final TextAligning t = new TextAligning(5);
	t.origLines = new String[]{"   aaa   "};
	t.align();
	assertTrue(t.res.size() == 1);
	assertTrue(t.res.get(0).equals("aaa"));
	assertTrue(t.hotPointX == -1);
	assertTrue(t.hotPointY == -1);
	for(int i = 0;i < 9;++i)
	{
	    t.origHotPointY = 0;
	    t.origHotPointX = i;
	    t.align();
	    assertTrue(t.res.size() == 1);
	    if (i < 3)
	    {
		assertTrue(t.res.get(0).equals(" aaa"));
		assertTrue(t.hotPointX == 0);
		assertTrue(t.hotPointY == 0);
	    } else
		if (i < 6)
		{
		    assertTrue(t.res.get(0).equals("aaa"));
		    assertTrue(t.hotPointX == i - 3);
		    assertTrue(t.hotPointY == 0);
		} else
		    {
			assertTrue(t.res.get(0).equals("aaa "));
			assertTrue(t.hotPointX == 3);
			assertTrue(t.hotPointY == 0);
		    }
	}
    }

    @Test public void coupleWords() throws Exception
    {
	final TextAligning t = new TextAligning(5);
	t.origLines = new String[]{"aaa bbb"};
	t.align();
	assertTrue(t.res.size() == 2);
	assertTrue(t.res.get(0).equals("aaa"));
	assertTrue(t.res.get(1).equals("bbb"));
	assertTrue(t.hotPointX == -1);
	assertTrue(t.hotPointY == -1);
	for(int i = 0;i < 7;++i)
	{
	    t.origHotPointY = 0;
	    t.origHotPointX = i;
	    t.align();
	    assertTrue(t.res.size() == 2);
	    if (i != 3)
		assertTrue(t.res.get(0).equals("aaa")); else
		assertTrue(t.res.get(0).equals("aaa "));
	    assertTrue(t.res.get(1).equals("bbb"));
	    if (i < 4)
	    {
		assertTrue(t.hotPointX == i);
		assertTrue(t.hotPointY == 0);
	    } else
	    {
		assertTrue(t.hotPointX == i - 4);
		assertTrue(t.hotPointY == 1);
	    }
	}
    }

    @Test public void multiline() throws Exception
    {
	final TextAligning t = new TextAligning(9);
	t.origLines = new String[]{"aaa bbb ccc",
				   "aaa bbb ccc",
				   "aaa bbb ccc",
				   "aaa bbb ccc",
				   "aaa bbb ccc"};
	t.align();
	assertTrue(t.res.size() == 8);
	assertTrue(t.res.get(0).equals("aaa bbb"));
	assertTrue(t.res.get(1).equals("ccc aaa"));
	assertTrue(t.res.get(2).equals("bbb ccc"));
	assertTrue(t.res.get(3).equals("aaa bbb"));
	assertTrue(t.res.get(4).equals("ccc aaa"));
	assertTrue(t.res.get(5).equals("bbb ccc"));
	assertTrue(t.res.get(6).equals("aaa bbb"));
	assertTrue(t.res.get(7).equals("ccc"));
	assertTrue(t.hotPointX == -1);
	assertTrue(t.hotPointY == -1);
	for(int i = 0;i < 55;++i)
	{
	    t.origHotPointX = i % 11;
	    t.origHotPointY = i / 11;
	    t.align();
	    //	    System.out.println("kaka " + t.hotPointX + " " + t.hotPointY);
	    final int cycle = i / 22;
	    final int pos = i - (cycle * 22);
	    if (pos < 8)
	    {
		assertTrue(t.hotPointX == pos);
		assertTrue(t.hotPointY == cycle * 3);
	    } else
		if (pos < 15)
		{
		    assertFalse(t.hotPointX == 3);
		    if (pos < 11)
			assertTrue(t.hotPointX == pos - 8); else
			assertTrue(t.hotPointX == pos - 7);
		    assertTrue(t.hotPointY == (cycle * 3) + 1);
		} else
		{
		    assertFalse(t.hotPointX == 7);
		    assertTrue(t.hotPointX == pos - 15);
		    assertTrue(t.hotPointY == (cycle * 3) + 2);
		}
	}
    }
}
