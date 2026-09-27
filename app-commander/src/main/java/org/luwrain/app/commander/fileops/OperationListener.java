// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2026 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.app.commander.fileops;

import java.nio.file.*;

import org.luwrain.core.*;

public interface OperationListener
{
    void onOperationProgress(Operation operation);

    default Operation.ConfirmationChoices confirmOverwrite(Path path)
    {
	NullCheck.notNull(path, "path");
	return Operation.ConfirmationChoices.SKIP;
    }

    default void onStatus(Operation operation, String message)
    {
	NullCheck.notNull(operation, "operation");
	NullCheck.notNull(message, "message");
    }
}