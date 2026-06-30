// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2026 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.app.video;

import java.util.*;
import java.io.*;
import java.nio.file.*;

import org.luwrain.core.*;
import org.luwrain.core.events.*;
import org.luwrain.controls.*;
import org.luwrain.app.base.*;
import org.luwrain.util.*;

import static java.util.Objects.*;
import static org.luwrain.util.UrlUtils.*;

public class App extends AppBase<Strings>
{
    static final String
	LOG_COMPONENT = "video";

    private final String arg;
    private MainLayout mainLayout = null;

    final List<Path> files = new ArrayList<>();

    public App()
    {
	this(null);
    }

    public App(String arg)
    {
	super(Strings.class);
	this.arg = arg;
    }

    @Override protected AreaLayout onAppInit() throws IOException
    {
	final File file;
	if (arg != null && !arg.isEmpty())
	    file = urlToFile(arg); else
	    file = null;
	if (file != null && isVideoFile(file.getName()))
	{
	    final ViewPlayer viewPlayer = createVideoPlayer(file);
	    viewPlayer.show();
	    setAppName(file.getName());
	} else
	{
	    setAppName(getStrings().appName());
	    if (file != null && file.isDirectory())
		populateFiles(file.toPath()); else
		populateFiles(Paths.get(getLuwrain().getPath("~")));
	}
	this.mainLayout = new MainLayout(this);
	return mainLayout.getAreaLayout();
    }

    @Override public boolean onEscape()
    {
	closeApp();
	return true;
    }

    void openVideo(Path path) throws IOException
    {
	final ViewPlayer viewPlayer = createVideoPlayer(path.toFile());
	viewPlayer.show();
	setAppName(path.getFileName().toString());
    }

    void populateFiles(Path dir) throws IOException
    {
	requireNonNull(dir);
	try (final var stream = Files.list(dir))
	{
	    stream.filter(Files::isRegularFile)
	    .filter(f -> isVideoFile(f.getFileName().toString()))
	    .sorted()
	    .forEach(files::add);
	}
    }

    private ViewPlayer createVideoPlayer(File file) throws IOException
    {
	return new ViewPlayer(getLuwrain(), file){
	    @Override void inaccessible() { getLuwrain().playSound(Sounds.INACCESSIBLE); }
	    @Override void announcePlaying(String fileName) { message(getStrings().playing(fileName), Luwrain.MessageType.OK); }
	    @Override void announcePaused(String fileName) { message(getStrings().paused(fileName), Luwrain.MessageType.OK); }
	    @Override void announceStopped(String fileName) { message(getStrings().stopped(fileName), Luwrain.MessageType.OK); }
	    @Override void announceSeekForward() { message(getStrings().seekForward(), Luwrain.MessageType.OK); }
	    @Override void announceSeekBackward() { message(getStrings().seekBackward(), Luwrain.MessageType.OK); }
	    @Override void announceVolumeUp(int volume) { message(getStrings().volumeUp(volume), Luwrain.MessageType.OK); }
	    @Override void announceVolumeDown(int volume) { message(getStrings().volumeDown(volume), Luwrain.MessageType.OK); }
	};
    }

    static boolean isVideoFile(String name)
    {
	final String upper = name.toUpperCase();
	return upper.endsWith(".MP4") || upper.endsWith(".AVI") || upper.endsWith(".MKV") || upper.endsWith(".MOV") || upper.endsWith(".WMV") || upper.endsWith(".FLV") || upper.endsWith(".WEBM") || upper.endsWith(".MPG") || upper.endsWith(".MPEG");
    }
}
