// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.app.player;

import java.io.*;

import org.luwrain.core.*;
import org.luwrain.popups.Popups;

public final class Conv
{
    private final Luwrain luwrain;
    private final Strings strings;

    Conv(App app)
    {
	this.luwrain = app.getLuwrain();
	this.strings = app.getStrings();
    }

    Album.Type newAlbumType()
    {
	final String sect = strings.albumTypeSection();
	final String dir = strings.albumTypeDir();
	final String m3u = strings.albumTypeM3u();
	final String streaming = strings.albumTypeStreaming();
	final Object typeRes = Popups.fixedList(luwrain, strings.newAlbumTypePopupName(), new Object[]{dir, streaming/*, m3u*/, sect});
	if (typeRes == null)
	    return null;
	if (typeRes == sect)
	    return Album.Type.SECTION;
	if (typeRes == dir)
	    return Album.Type.DIR;
	if (typeRes == m3u)
	    return Album.Type.M3U;
	if (typeRes == streaming)
	    return Album.Type.STREAMING;
	return null;
    }

    boolean confirmAlbumDeleting(Album album)
    {
	return Popups.confirmDefaultNo(luwrain, strings.albumDeletingPopupName(), strings.albumDeletingPopupText(album.getTitle()));
    }

    String newAlbumTitle()
    {
	return Popups.textNotEmpty(luwrain, strings.newAlbumPopupName(), strings.newAlbumTitlePopupPrefix(), "");
    }

        String newSectionTitle()
    {
	return Popups.textNotEmpty(luwrain, strings.newSectionPopupName(), strings.newSectionTitlePopupPrefix(), "");
    }

    String newStreamingAlbumUrl()
    {
	return Popups.textNotEmpty(luwrain, "Новый альбом", "Адрес потока радиостанции:", "http://");
    }

    public File dirAlbumPath()
    {
	return Popups.existingDir(luwrain, "Каталог с файлами альбома:");//FIXME:
    }
}
