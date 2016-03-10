/*
   Copyright 2012-2016 Michael Pozhidaev <michael.pozhidaev@gmail.com>

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

package org.luwrain.player;

public interface Player
{
    static public final String SHARED_OBJECT_NAME = "luwrain.player";

    void play(Playlist playlist);
    void stop();
    Playlist getCurrentPlaylist();
    int getCurrentTrackNum();
    Playlist[] loadRegistryPlaylists();
    void addListener(Listener listener);
    void removeListener(Listener listener);
}
