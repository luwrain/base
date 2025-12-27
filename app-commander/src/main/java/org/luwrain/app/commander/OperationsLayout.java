// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.app.commander;

import java.util.*;

import org.luwrain.core.*;
import org.luwrain.core.events.*;
import org.luwrain.controls.*;
import org.luwrain.app.base.*;
import org.luwrain.app.commander.App.Side;
import org.luwrain.app.commander.fileops.*;

final class OperationsLayout extends LayoutBase implements ListArea.ClickHandler<Operation>
{
    private final App app;
    final ListArea<Operation> operationsArea;

    OperationsLayout(App app)
    {
	super(app);
	this.app = app;
	setCloseHandler(()->{ app.layouts().main(); return true; });
	this.operationsArea = new ListArea<Operation>(listParams((params)->{
	    params.name = app.getStrings().operationsAreaName();
	    params.clickHandler = this;
	    params.model = new ListUtils.ListModel<>(app.operations);
	    params.appearance = new OperationsAppearance(app);
		})) {
		    @Override protected String noContentStr()
		    {
			return "Файловые операции отсутствуют";//FIXME:
		    }
		};
	final Actions operationsActions = actions();
	setAreaLayout(operationsArea, operationsActions);
    }

    @Override public boolean onListClick(ListArea area, int index, Operation op)
    {
	app.operations.remove(index);
	operationsArea.refresh();
	app.getLuwrain().playSound(Sounds.OK);
	return true;
    }
}
