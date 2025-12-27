// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.app.player;

import java.util.*;
import java.util.concurrent.atomic.*;
import java.io.*;
import java.net.*;

import org.luwrain.core.*;
import org.luwrain.player.*;
import org.luwrain.util.*;

final class Starting
{
    static private final long
	SAVE_POS_STEP = 1000;

    private final App app;
    Starting(App app) { this.app = app; }

    private boolean onDir(Album album)
    {
	final String path = album.getPath();
	if (path.isEmpty())
	    return false;
	final List<String> urls = new ArrayList<>();
	final App.TaskId taskId = app.newTaskId();
	return app.runTask(taskId, ()->{
		collectMusicFiles(new File(path), urls);
		app.finishedTask(taskId, ()->{
			final AtomicLong prevPosMsec = new AtomicLong(album.getPosMsec());
			final Playlist playlist = new FixedPlaylist(urls.toArray(new String[urls.size()]),
								    (trackNum, posMsec)->{
									if (!album.isSavePos())
									    return;
									if (Math.abs(posMsec - prevPosMsec.get()) < SAVE_POS_STEP)
									    return;
									prevPosMsec.set(posMsec);
									album.setTrackNum(trackNum);
									album.setPosMsec(posMsec);
									app.albums.save();
								    },
								    (value)->{
									album.setVolume(value);
									app.albums.save();
								    }, album.getVolume());
			app.getPlayer().play(playlist,
					     album.isSavePos()?album.getTrackNum():0,
					     album.isSavePos()?album.getPosMsec():0,
					     EnumSet.noneOf(Player.Flags.class));
		    });
	    });
    }


    private boolean onStreaming(Album album)
    {
	final String url = album.getUrl();
	if (url == null || url.trim().isEmpty())
	    return false;
	final Playlist playlist = new FixedPlaylist(new String[]{url.trim()}, (value)->{
		album.setVolume(value);
		app.albums.save();
	    }, album.getVolume());
	app.getPlayer().play(playlist, 0, 0, EnumSet.of(Player.Flags.STREAMING));
	return true;
    }


    private void collectMusicFiles(File file, List<String> res)
    {
	if (!file.exists())
	    return;
	if (file.isFile())
	{
	    final String name = file.getName().toLowerCase();
	    if (name.endsWith(".mp3"))
		res.add(UrlUtils.fileToUrl(file));
	    return;
	}
	if (!file.isDirectory())
	    return;
	final File[] files = file.listFiles();
	if (files != null)
	{
	    Arrays.sort(files);
	    for(File f: files)
		collectMusicFiles(f, res);
	}
    }

        boolean play(Album album)
    {
	if (album.getType() == null)
	    return false;
	switch(album.getType())
	{
	case STREAMING:
	    return onStreaming(album);
	case DIR:
	    return onDir(album);
	default:
	    return false;
	}
    }

}
