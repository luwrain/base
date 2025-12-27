// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.app.notepad;

import java.util.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
final class Config
{
    private int aligningLineLen, narratedFileLen, narratingSpeechPitch, narratingSpeechRate, narratingPauseDuration;
    private String lameCommand, narratingChannelName, narratingChannelParams;
    List<String> charsets;
}
