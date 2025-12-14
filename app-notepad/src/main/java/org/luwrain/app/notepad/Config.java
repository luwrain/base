// SPDX-License-Identifier: Apache-2.0
// Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.app.notepad;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
final class Config
{
    private int aligningLineLen, narratedFileLen, narratingSpeechPitch, narratingSpeechRate, narratingPauseDuration;
    private String lameCommand, narratingChannelName, narratingChannelParams;
}
