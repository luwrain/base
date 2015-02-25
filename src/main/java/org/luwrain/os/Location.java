/*
   Copyright 2012-2015 Michael Pozhidaev <msp@altlinux.org>

   This file is part of the Luwrain.

   Luwrain is free software; you can redistribute it and/or
   modify it under the terms of the GNU General Public
   License as published by the Free Software Foundation; either
   version 3 of the License, or (at your option) any later version.

   Luwrain is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
   General Public License for more details.
*/

package org.luwrain.os;

import java.io.File;

public class Location
{
    public static final int REGULAR = 0;
    public static final int REMOVABLE = 1;
    public static final int REMOTE = 2;
    public static final int USER_HOME = 3;
    public static final int ROOT = 4;

    private int type;
    private File file;
    private String name;

    public Location(int type,
		    File file,
		    String name)
    {
	this.type = type;
	this.file = file;
	this.name = name;
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
}
