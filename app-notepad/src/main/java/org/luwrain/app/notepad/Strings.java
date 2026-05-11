// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.app.notepad;

import org.luwrain.core.annotations.*;

@ResourceStrings(langs = { "en", "ru" })
public interface Strings
{
        String appName();
    
    String actionCharset();
    String actionIndents();
    String actionNarrating();
    String actionNoIndents();
    String actionOpen();
        String openPopupName();
    String openPopupPrefix();
    String errorOpeningFile(String message);

    
    String actionReplace();
        String replacePopupName();
    String replaceExpPopupPrefix();
    String replaceWithPopupPrefix();

    String actionSaveAs();
        String saveChangesPopupName();
    String saveChangesPopupQuestion();
    String savePopupName();
    String savePopupPrefix();
    String errorSavingFile(String message);

    
    String actionAiAssist();

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
    String propsAreaName();
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
    String actionSpellRight();
    String actionWordSuggestions();
    String correctionSuggestionsPopupName();
    String actionAddSpellExclusion();
    String settingsAreaName();
    String defaultCharsetList();
    String settingsFormCharsetList();
}
