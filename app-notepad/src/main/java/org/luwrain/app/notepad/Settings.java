// SPDX-License-Identifier: Apache-2.0
// Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.app.notepad;

import org.luwrain.core.*;

interface Settings
{
    static final String PATH = "/org/luwrain/app/notepad";

    int getAligningLineLen(int defValue);
    void setAligningLineLen(int value);
    String getLameCommand(String defValue);
    void setLameCommand(String command);
    String getNarratingChannelName(String defValue);
    void setNarratingChannelName(String value);
    String getNarratingChannelParams(String defValue);
    void setNarratingChannelParams(String name);
    int getNarratedFileLen(int defValue);
    void setNarratedFileLen(int value);
    int getNarratingSpeechPitch(int defValue);
    void setNarratingSpeechPitch(int value);
    int getNarratingSpeechRate(int defValue);
    void setNarratingSpeechRate(int value);
    int getNarratingPauseDuration(int defValue);
    void setNarratingPauseDuration(int value);

    static Settings create(Registry registry)
    {
	NullCheck.notNull(registry, "registry");
	return 	RegistryProxy.create(registry, PATH, Settings.class);
    }
}
