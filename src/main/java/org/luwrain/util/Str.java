/*
   Copyright 2012-2016 Michael Pozhidaev <michael.pozhidaev@gmail.com>
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

package org.luwrain.util;

public class Str
{
    static public String removeIsoControlChars(String value)
    {
	final StringBuilder b = new StringBuilder();
	for(int i = 0;i < value.length();++i)
	    if (!Character.isISOControl(value.charAt(i)))
		b.append(value.charAt(i));
	return b.toString();
    }

    static public String replaceIsoControlChars(String value, char replaceWith)
    {
	final StringBuilder b = new StringBuilder();
	for(int i = 0;i < value.length();++i)
	    if (!Character.isISOControl(value.charAt(i)))
		b.append(value.charAt(i)); else
		b.append(replaceWith);
	return b.toString();
    }

    static public String replaceIsoControlChars(String value)
    {
	return replaceIsoControlChars(value, ' ');
    }
}
