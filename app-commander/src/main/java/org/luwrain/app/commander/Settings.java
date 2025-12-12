// SPDX-License-Identifier: Apache-2.0
// Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.app.commander;

import org.luwrain.core.*;

interface Settings
{
static final String PATH = "/org/luwrain/app/commander";

    boolean getShowHidden(boolean defValue);
    void setShowHidden(boolean value);
    String getZipFilesEncoding(String defValue);
}
