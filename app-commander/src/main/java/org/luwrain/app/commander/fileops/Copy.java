// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.app.commander.fileops;

import java.io.*;
import java.nio.file.*;

import org.luwrain.core.*;
import org.luwrain.app.commander.*;

public final class Copy extends CopyingBase
{
    private final CopyMoveParams params;

    public Copy(CopyMoveParams params)
	    {
		super(params.getListener(), params.getName());
		this.params = params;
    }

    @Override protected void work() throws IOException
    {
	copy(params);
    }
}
