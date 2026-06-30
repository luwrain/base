// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2026 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.app.video;

import java.io.*;
import java.util.*;

import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.input.KeyEvent;
import javafx.util.Duration;

import org.luwrain.core.*;
import org.luwrain.core.events.*;
import org.luwrain.graphical.*;

import static org.luwrain.graphical.FxThread.*;
import static org.luwrain.app.video.App.*;

abstract class ViewPlayer
{
    static private final double
	SEEK_STEP = 10.0,
	VOLUME_STEP = 0.05;

    private final Luwrain luwrain;
    private final File file;
    private Media media = null;
    private MediaPlayer player = null;
    private MediaView mediaView = null;
    private StackPane pane = null;

    private boolean playing = false;

    ViewPlayer(Luwrain luwrain, File file) throws IOException
    {
	this.luwrain = luwrain;
	this.file = file;
    }

    abstract void inaccessible();
    abstract void announcePlaying(String fileName);
    abstract void announcePaused(String fileName);
    abstract void announceStopped(String fileName);
    abstract void announceSeekForward();
    abstract void announceSeekBackward();
    abstract void announceVolumeUp(int volume);
    abstract void announceVolumeDown(int volume);

    void show()
    {
	luwrain.showGraphical((graphicalModeControl)->{
		try {
		    this.pane = new StackPane();
		    this.media = new Media(file.toURI().toString());
		    this.player = new MediaPlayer(media);
		    this.mediaView = new MediaView(player);
		    this.pane.getChildren().add(mediaView);
		    mediaView.fitWidthProperty().bind(pane.widthProperty());
		    mediaView.fitHeightProperty().bind(pane.heightProperty());
		    pane.setFocusTraversable(true);
		    pane.setOnKeyPressed((event)->onKey(graphicalModeControl, event));
		    pane.requestFocus();
		    player.play();
		    playing = true;
		    final String fileName = file.getName();
		    announcePlaying(fileName);
		    Log.debug(LOG_COMPONENT, "started video playback: " + fileName);
		    return pane;
		}
		catch(Throwable e)
		{
		    Log.error(LOG_COMPONENT, "unable to initialize the video player: " + e.getClass().getName() + ": " + e.getMessage());
		    e.printStackTrace();
		    this.pane = null;
		    return null;
		}
	    });
    }

    private void togglePlayPause()
    {
	ensure();
	if (player == null)
	    return;
	final String fileName = file.getName();
	if (playing)
	{
	    player.pause();
	    playing = false;
	    announcePaused(fileName);
	} else
	{
	    player.play();
	    playing = true;
	    announcePlaying(fileName);
	}
    }

    private void seekForward()
    {
	ensure();
	if (player == null)
	    return;
	final Duration current = player.getCurrentTime();
	final Duration total = player.getTotalDuration();
	if (total == null || total.isUnknown())
	{
	    inaccessible();
	    return;
	}
	final Duration seekTo = current.add(Duration.seconds(SEEK_STEP));
	if (seekTo.greaterThanOrEqualTo(total))
	{
	    player.seek(total);
	    player.pause();
	    playing = false;
	    announceStopped(file.getName());
	    return;
	}
	player.seek(seekTo);
	announceSeekForward();
    }

    private void seekBackward()
    {
	ensure();
	if (player == null)
	    return;
	final Duration current = player.getCurrentTime();
	final Duration seekTo = current.subtract(Duration.seconds(SEEK_STEP));
	if (seekTo.lessThanOrEqualTo(Duration.ZERO))
	{
	    player.seek(Duration.ZERO);
	    announceSeekBackward();
	    return;
	}
	player.seek(seekTo);
	announceSeekBackward();
    }

    private void volumeUp()
    {
	ensure();
	if (player == null)
	    return;
	double vol = player.getVolume();
	if (vol >= 1.0)
	{
	    inaccessible();
	    return;
	}
	vol = Math.min(1.0, vol + VOLUME_STEP);
	player.setVolume(vol);
	announceVolumeUp((int)Math.round(vol * 100));
    }

    private void volumeDown()
    {
	ensure();
	if (player == null)
	    return;
	double vol = player.getVolume();
	if (vol <= 0.0)
	{
	    inaccessible();
	    return;
	}
	vol = Math.max(0.0, vol - VOLUME_STEP);
	player.setVolume(vol);
	announceVolumeDown((int)Math.round(vol * 100));
    }

    private void fastForward()
    {
	ensure();
	if (player == null)
	    return;
	final Duration current = player.getCurrentTime();
	final Duration total = player.getTotalDuration();
	if (total == null || total.isUnknown())
	{
	    inaccessible();
	    return;
	}
	final Duration seekTo = current.add(Duration.seconds(SEEK_STEP * 6));
	if (seekTo.greaterThanOrEqualTo(total))
	{
	    player.seek(total);
	    player.pause();
	    playing = false;
	    announceStopped(file.getName());
	    return;
	}
	player.seek(seekTo);
	announceSeekForward();
    }

    private void fastBackward()
    {
	ensure();
	if (player == null)
	    return;
	final Duration current = player.getCurrentTime();
	final Duration seekTo = current.subtract(Duration.seconds(SEEK_STEP * 6));
	if (seekTo.lessThanOrEqualTo(Duration.ZERO))
	{
	    player.seek(Duration.ZERO);
	    announceSeekBackward();
	    return;
	}
	player.seek(seekTo);
	announceSeekBackward();
    }

    public void close()
    {
	runSync(()->{
		if (player != null)
		{
		    player.stop();
		    player.dispose();
		}
	    });
    }

    private void onKey(Interaction.GraphicalModeControl graphicalModeControl, KeyEvent event)
    {
	ensure();
	switch(event.getCode())
	{
	case ESCAPE:
	    if (player != null)
		player.stop();
	    graphicalModeControl.close();
	    break;
	case SPACE:
	    togglePlayPause();
	    break;
	case RIGHT:
	    seekForward();
	    break;
	case LEFT:
	    seekBackward();
	    break;
	case UP:
	    volumeUp();
	    break;
	case DOWN:
	    volumeDown();
	    break;
	case PAGE_DOWN:
	    fastForward();
	    break;
	case PAGE_UP:
	    fastBackward();
	    break;
	case HOME:
	    if (player != null)
	    {
		player.seek(Duration.ZERO);
		announceSeekBackward();
	    }
	    break;
	case END:
	    if (player != null)
	    {
		final Duration total = player.getTotalDuration();
		if (total != null && !total.isUnknown())
		{
		    player.seek(total);
		    player.pause();
		    playing = false;
		    announceStopped(file.getName());
		}
	    }
	    break;
	case ENTER:
	    togglePlayPause();
	    break;
	default:
	    break;
	}
    }
}
