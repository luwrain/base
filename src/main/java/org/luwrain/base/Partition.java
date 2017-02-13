/*
   Copyright 2012-2017 Michael Pozhidaev <michael.pozhidaev@gmail.com>
   Copyright 2015-2016 Roman Volovodov <gr.rPman@gmail.com>

   This file is part of the LUWRAIN.

   LUWRAIN is free software; you can redistribute it and/or
   modify it under the terms of the GNU General Public
   License as published by the Free Software Foundation; either
   version 3 of the License, or (at your option) any later version.

   LUWRAIN is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
   General Public License for more details.
*/

package org.luwrain.base;

import java.io.File;

public class Partition
{
    static public final int REGULAR = 0;
    static public final int REMOVABLE = 1;
    static public final int REMOTE = 2;
    static public final int USER_HOME = 3;
    static public final int ROOT = 4;

    private int type;
    private File file;
    private String name;
    private boolean mounted = false;

    public Partition(int type, File file,
	      String name, boolean mounted)
    {
	this.type = type;
	this.file = file;
	this.name = name;
	this.mounted = mounted;
	if (file == null)
	    throw new NullPointerException("file may not be null");
	if (name == null)
	    throw new NullPointerException("name may not be null");
    }

    public int type()
    {
	return type;
    }

    public File file()
    {
	return file;
    }

public String name()
{
return name;
}

    public boolean mounted()
    {
	return mounted;
    }
}
