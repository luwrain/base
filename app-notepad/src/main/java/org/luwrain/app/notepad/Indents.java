// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.app.notepad;

import java.util.*;
//import java.util.concurrent.*;

//import org.luwrain.core.*;
//import org.luwrain.controls.*;
import org.luwrain.controls.edit.*;
//import org.luwrain.nlp.*;
//import static org.luwrain.core.DefaultEventResponse.*;

abstract class Indents extends MultilineEditModelWrap
{
    Indents(MultilineEdit.Model model)
    {
	super(model);
    }

    abstract App.Mode getMode();
    abstract EditArea getEditArea();
}
