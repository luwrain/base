// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.app.commander.fileops;

import org.luwrain.app.commander.fileops.*;

public interface OperationListener
{
    void onOperationProgress(Operation operation);
}
