/*
   Copyright 2012-2017 Michael Pozhidaev <michael.pozhidaev@gmail.com>

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

package org.luwrain.player;

import org.luwrain.core.*;

public final class Playlist
{
    private final String[] urls;
    private final String title;

    public Playlist(String[] urls)
    {
	NullCheck.notNullItems(urls, "urls");
	this.title = "";
	this.urls = urls;
    }

    public Playlist(String title, String[] urls)
    {
	NullCheck.notNull(title, "title");
	NullCheck.notNullItems(urls, "urls");
	this.title = title;
	this.urls = urls;
    }

    public Playlist(String url)
    {
	NullCheck.notNull(url, "url");
	this.title = "";
	this.urls = new String[]{url};
    }

    public Playlist(String title, String url)
    {
	NullCheck.notNull(title, "title");
	NullCheck.notNull(url, "url");
	this.title = title;
	this.urls = new String[]{url};
    }

    public String getPlaylistTitle()
    {
	return title;
    }

    public String[] getPlaylistUrls()
    {
	return urls;
    }

    @Override public String toString()
    {
	return title;
    }
}
