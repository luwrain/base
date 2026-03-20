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

    boolean confirmAlbumDeleting(Album album)
    {
	return Popups.confirmDefaultNo(luwrain, strings.albumDeletingPopupName(), strings.albumDeletingPopupText(album.getTitle()));
    }

    String newSectionTitle()
    {
	return Popups.textNotEmpty(luwrain, strings.newSectionPopupName(), strings.newSectionTitlePopupPrefix(), "");
    }

    public File dirAlbumPath()
    {
	return Popups.existingDir(luwrain, strings.albumDirPopupName());
    }
}
