// SPDX-License-Identifier: Apache-2.0
// Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.app.notepad;

import org.luwrain.core.*;
import org.luwrain.core.events.*;
import org.luwrain.controls.*;
import org.luwrain.cpanel.*;

final class SettingsForm extends FormArea implements SectionArea
{
    private ControlPanel controlPanel;
    private Luwrain luwrain;
    private Config conf = null;
    private Strings strings;

    SettingsForm(ControlPanel controlPanel, Strings strings)
    {
	super(new DefaultControlContext(controlPanel.getCoreInterface()), strings.settingsFormName());
	this.controlPanel = controlPanel;
	this.luwrain = controlPanel.getCoreInterface();;
	this.strings = strings;
	fillForm();
    }

    private void fillForm()
    {
	addEdit("narrating-channel-name", strings.settingsFormNarratingChannelName(), conf.getNarratingChannelName());
	addEdit("narrating-channel-params", strings.settingsFormNarratingChannelParams(), conf.getNarratingChannelParams());
	addEdit("narrated-file-len", strings.settingsFormNarratedFileLen(), String.valueOf(conf.getNarratedFileLen()));
	addEdit("narrating-speech-pitch", strings.settingsFormNarratingSpeechPitch(), String.valueOf(conf.getNarratingSpeechPitch()));
	addEdit("narrating-speech-rate", strings.settingsFormNarratingSpeechRate(), String.valueOf(conf.getNarratingSpeechRate()));
	addEdit("narrating-pause-duration", strings.settingsFormNarratingPauseDuration(), String.valueOf(conf.getNarratingPauseDuration()));
    }

    @Override public boolean saveSectionData()
    {
	conf.setNarratingChannelName(getEnteredText("narrating-channel-name"));
	conf.setNarratingChannelParams(getEnteredText("narrating-channel-params"));
	try {
	    final int value = Integer.parseInt(getEnteredText("narrated-file-len"));
	    if (value < 0)
	    {
		luwrain.message(strings.settingsFormFileLenMayNotBeNegative(), Luwrain.MessageType.ERROR);
		return false;
	    }
	    conf.setNarratedFileLen(value);
	}
	catch(NumberFormatException e)
	{
	    luwrain.message(strings.settingsFormFileLenIsNotInteger(), Luwrain.MessageType.ERROR);
	    return false;
	}
	try {
	    final int value = Integer.parseInt(getEnteredText("narrating-speech-pitch"));
	    if (value < -50 || value > 50)
	    {
		luwrain.message("fixme", Luwrain.MessageType.ERROR);
		return false;
	    }
	    conf.setNarratingSpeechPitch(value);
	}
	catch(NumberFormatException e)
	{
	    luwrain.message("fixme" + e.getMessage(), Luwrain.MessageType.ERROR);
	    return false;
	}
	try {
	    final int value = Integer.parseInt(getEnteredText("narrating-speech-rate"));
	    if (value < -50 || value > 50)
	    {
		luwrain.message("fixme", Luwrain.MessageType.ERROR);
		return false;
	    }
	    conf.setNarratingSpeechRate(value);
	}
	catch(NumberFormatException e)
	{
	    luwrain.message("fixme" + e.getMessage(), Luwrain.MessageType.ERROR);
	    return false;
	}
	try {
	    final int value = Integer.parseInt(getEnteredText("narrating-pause-duration"));
	    if (value < 0)
	    {
		luwrain.message("fixme", Luwrain.MessageType.ERROR);
		return false;
	    }
	    conf.setNarratingPauseDuration(value);
	}
	catch(NumberFormatException e)
	{
	    luwrain.message("fixme" + e.getMessage(), Luwrain.MessageType.ERROR);
	    return false;
	}
	return true;
    }

    @Override public boolean onInputEvent(InputEvent event)
    {
	NullCheck.notNull(event, "event");
	if (controlPanel.onInputEvent(event))
	    return true;
	return super.onInputEvent(event);
    }

    @Override public boolean onSystemEvent(SystemEvent event)
    {
	NullCheck.notNull(event, "event");
	if (controlPanel.onSystemEvent(event))
	    return true;
	return super.onSystemEvent(event);
    }

    static SettingsForm create(ControlPanel controlPanel)
    {
	NullCheck.notNull(controlPanel, "controlPanel");
	final Strings strings = (Strings)controlPanel.getCoreInterface().i18n().getStrings(Strings.class.getName());
	return new SettingsForm(controlPanel, strings);
    }
}
