// SPDX-License-Identifier: Apache-2.0
// Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.app.commander.fileops;

import java.io.*;

public final class OperationCancelledException extends IOException
{
    public OperationCancelledException()
    {
	super("The file operation is cancelled");
    }
}
