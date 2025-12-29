// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.app.player;

import org.luwrain.core.annotations.*;

@ResourceStrings(langs = { "en", "ru" })
public interface Strings
{
        String actionAddAlbum();
    String albumDeletingPopupName();
    String albumDeletingPopupText(String albumName);
    String albumsAreaName();
    String albumTypeDir();
    String albumTypeM3u();
    String albumTypeSection();
    String albumTypeStreaming();
    String appName();
    String controlAreaName(); 
    String newAlbumPopupName();
    String newAlbumTitlePopupPrefix();
    String newAlbumTypePopupName();
    String newSectionPopupName();
    String newSectionTitlePopupPrefix();
    String playlistAreaName();

    String actionPauseResume();
    String actionPrevTrack();
    String actionNextTrack();
    String actionVolumePlus();
    String actionVolumeMinus();
    String actionJumpForward();
    String actionJumpBackward();

    String albumPropTitle();
    String albumPropUrl();
    String albumPropPath();
    String albumPropSavePosition();
    String albumPropTitleCannotBeEmpty();

    String wizardNewAlbumIntro();
    String wizardNewAlbumTitle();
    String wizardNewAlbumDir();
    String wizardNewAlbumPlaylist();
    String wizardNewAlbumStream();
    String wizardNewAlbumTitleCannotBeEmpty();
    String wizardNewAlbumDirIntro(String title);
    String wizardNewAlbumDirPath();
    String wizardNewAlbumCreate();
    String wizardNewAlbumDirPathCannotBeEmpty();
}
