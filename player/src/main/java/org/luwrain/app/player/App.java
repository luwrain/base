// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.app.player;

import java.util.*;
import java.util.concurrent.*;
import java.io.*;
import java.net.*;
import org.apache.logging.log4j.*;

import org.luwrain.core.*;
import org.luwrain.core.events.*;
import org.luwrain.controls.*;
import org.luwrain.player.*;
import org.luwrain.app.base.*;

public final class App extends AppBase<Strings> implements Application, MonoApp, org.luwrain.player.Listener
{
    static private final Logger log = LogManager.getLogger();

    private final String[] args;
    final Starting starting = new Starting(this);
    Config conf = null;
    private org.luwrain.player.Player player = null;
    private Conv conv = null;
    private MainLayout mainLayout = null;
    Albums albums = null;
    private Hooks hooks = null;
    final ConcurrentMap<String, TrackInfo> trackInfoMap = new ConcurrentHashMap<>();

    App() { this(null); }
    App(String[] args)
    {
	super(Strings.class, "luwrain.player");
	this.args = args != null?args.clone():new String[0];
    }

    @Override public AreaLayout onAppInit() throws Exception
    {
conv = new Conv(this);
conf = getLuwrain().loadConf(Config.class);
	if (conf == null)
	{
	    conf = new Config();
	}
albums = new Albums(this);
player = getLuwrain().getPlayer();
	if (player == null)
	    throw new Exception("No system player");
player.addListener(this);
hooks = new Hooks(getLuwrain());
mainLayout = new MainLayout(this, this.player);
	setAppName(getStrings().appName());
	return mainLayout.getAreaLayout();
    }

    void fillTrackInfoMap(Playlist playlist, ListArea listArea)
    {
	final List<String> tracks = new ArrayList<>();
	int count = playlist.getTrackCount();
	for(int i = 0;i < count;i++)
	    tracks.add(playlist.getTrackUrl(i));
	getLuwrain().executeBkg(()->{
		for(String s: tracks)
		{
		    try {
			trackInfoMap.put(s, new TrackInfo(new URL(s)));
			getLuwrain().runUiSafely(()->listArea.refresh());
		    }
		    catch(Throwable e)
		    {
			log.debug("Unable to read tags of the file: " + e.getClass().getName() + ": " + e.getMessage());
		    }
		}
	    });
    }

    @Override public boolean onEscape()
    {
	closeApp();
	return true;
    }

    @Override public void closeApp()
    {
	this.player.removeListener(this);
	super.closeApp();
    }

    @Override public MonoApp.Result onMonoAppSecondInstance(Application app)
    {
	return MonoApp.Result.BRING_FOREGROUND;
    }

    @Override public void onNewPlaylist(org.luwrain.player.Playlist playlist)
    {
	getLuwrain().runUiSafely(()->{
		if (mainLayout != null)
		    mainLayout.onNewPlaylist(playlist);
	    });
    }

    @Override public void onNewTrack(org.luwrain.player.Playlist playlist, int trackNum)
    {
	getLuwrain().runUiSafely(()->{
		if (mainLayout == null)
		    return;
		final TrackInfo trackInfo = trackInfoMap.get(playlist.getTrackUrl(trackNum));
		if (trackInfo != null)
		{
		    mainLayout.controlArea.setPlaylistTitle(trackInfo.artist);
		    mainLayout.controlArea.setTrackTitle(trackInfo.title);
		} else
		{
		    mainLayout.controlArea.setPlaylistTitle("-");
		    mainLayout.controlArea.setTrackTitle("-");
		}
		mainLayout.controlArea.setTrackTime(0);
	    });
    }

    @Override public void onTrackTime(org.luwrain.player.Playlist playlist, int trackNum, long msec)
    {
	getLuwrain().runUiSafely(()->{
		if (mainLayout != null)
		    mainLayout.controlArea.setTrackTime(msec);
	    });
    }

    @Override public void onNewState(org.luwrain.player.Playlist playlist, Player.State state)
    {
	getLuwrain().runUiSafely(()->{
		if (mainLayout != null)
		    switch(state)
		    {
		    case STOPPED:
			mainLayout.controlArea.setMode(ControlArea.Mode.STOPPED);
			break;
		    case PAUSED:
			if (Utils.isStreamingPlaylist(playlist))
			    mainLayout.controlArea.setMode(ControlArea.Mode.PLAYING_STREAMING); else
			    mainLayout.controlArea.setMode(ControlArea.Mode.PAUSED);
			break;
		    case PLAYING:
			if (Utils.isStreamingPlaylist(playlist))
			    mainLayout.controlArea.setMode(ControlArea.Mode.PLAYING_STREAMING); else
			    mainLayout.controlArea.setMode(ControlArea.Mode.PLAYING);
			break;
		    }
	    });
    }

    @Override public void onPlayingError(org.luwrain.player.Playlist playlist, Exception e)
    {
	crash(e);
    }

    //    Albums getAlbums() { return this.albums; }
    Player getPlayer() { return this.player; }
    Hooks getHooks() { return this.hooks; }
    public Conv getConv() { return conv; }
}
