// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2026 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.app.video;

import org.luwrain.core.annotations.*;

@ResourceStrings(langs = { "en", "ru" })
public interface Strings
{
    String appName();
    String playing(String fileName);
    String paused(String fileName);
    String stopped(String fileName);
    String seekForward();
    String seekBackward();
    String volumeUp(int volume);
        String volumeDown(int volume);
}
