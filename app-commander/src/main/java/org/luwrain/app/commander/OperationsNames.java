// SPDX-License-Identifier: Apache-2.0
// Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.app.commander;

import java.util.*;
import java.io.*;
import java.nio.file.*;
import org.apache.commons.vfs2.*;

import org.luwrain.core.*;
import org.luwrain.controls.*;
import org.luwrain.app.commander.fileops.*;

class OperationsNames
{
    protected final App app;

    OperationsNames(App app)
    {
	NullCheck.notNull(app, "app");
	this.app = app;
    }

    String copyOperationName(Path[] whatToCopy, Path copyTo)
    {
	if (whatToCopy.length < 1)
	    return "";
	if (whatToCopy.length > 1)
	    return app.getStrings().copyOperationName(whatToCopy[0].getFileName().toString() + ",...", copyTo.getFileName().toString());
	return app.getStrings().copyOperationName(whatToCopy[0].getFileName().toString(), copyTo.getFileName().toString());
    }

        String moveOperationName(Path[] whatToMove, Path moveTo)
    {
	if (whatToMove.length < 1)
	    return "";
	if (whatToMove.length > 1)
	    return app.getStrings().moveOperationName(whatToMove[0].getFileName().toString() + ",...", moveTo.getFileName().toString());
	return app.getStrings().moveOperationName(whatToMove[0].getFileName().toString(), moveTo.getFileName().toString());
    }


    /*
    String moveOperationName(File[] whatToMove, File moveTo)
    {
	if (whatToMove.length < 1)
	    return "";
	if (whatToMove.length > 1)
	    return app.getStrings().moveOperationName(whatToMove[0].getName() + ",...", moveTo.getName());
	return app.getStrings().moveOperationName(whatToMove[0].getName(), moveTo.getName());
    }
    */
}
