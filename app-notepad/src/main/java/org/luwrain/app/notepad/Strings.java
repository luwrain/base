// SPDX-License-Identifier: Apache-2.0
// Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.app.notepad;

import org.luwrain.core.annotations.*;

@ResourceStrings(langs = { "en", "ru" })
public interface Strings
{
    String actionCharset();
    String actionIndents();
    String actionNarrating();
    String actionNoIndents();
    String actionOpen();
    String actionReplace();


    String actionSaveAs();
    String appName();
    String cancelNarratingBeforeClosing();

    String enteredPathMayNotBeDir(String fileName);
    String fileIsSaved();
    String initialTitle();
    String modeNatural();
    String modeNone();
    String modeProgramming();
    String narratingAreaName();
    String narratingCancelled();
    String narratingDestDirPopupName();
    String narratingDestDirPopupPrefix();
    String narratingDone();
    String narratingFileWritten(String file);
    String narratingProgress(String status);
    String noChannelToSynth(String channelName);
    String noModificationsToSave();
    String noTextToSynth();
    String openPopupName();
    String openPopupPrefix();
    String propsAreaName();
    String saveChangesPopupName();
    String saveChangesPopupQuestion();
    String savePopupName();
    String savePopupPrefix();
    String settingsFormFileLenIsNotInteger();
    String settingsFormFileLenMayNotBeNegative();
    String settingsFormName();
    String settingsFormNarratedFileLen();
    String settingsFormNarratingChannelName();
    String settingsFormNarratingChannelParams();
    String settingsFormNarratingPauseDuration();
    String settingsFormNarratingSpeechPitch();
    String settingsFormNarratingSpeechRate();


        String charsetPopupPrefix();

    String replacePopupName();
    String replaceExpPopupPrefix();
    String replaceWithPopupPrefix();

        String actionSpellRight();

    String actionWordSuggestions();
    String correctionSuggestionsPopupName();

    String actionAddSpellExclusion();
}
