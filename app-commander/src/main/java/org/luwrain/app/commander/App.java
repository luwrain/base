// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2026 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.app.commander;

import java.util.*;
import java.util.concurrent.*;
import java.nio.file.*;

import org.luwrain.core.*;
import org.luwrain.core.events.*;
import org.luwrain.app.base.*;
import org.luwrain.app.commander.fileops.*;

import static java.util.Objects.*;

public final class App extends AppBase<Strings>
{
    enum Side {LEFT, RIGHT};

    public interface Layouts
    {
	void main();
	void operations();
    }

    final String startFrom;
    final List<Operation> operations = new ArrayList<>();
    final OperationListener opListener = newOperationListener();
    public Config conf;
    public Conv conv;
    public Hooks hooks;
    private MainLayout mainLayout;
    private OperationsLayout operationsLayout;

    App(String startFrom)
    {
	super(Strings.class, "luwrain.commander");
	if (startFrom != null && !startFrom.isEmpty())
	    this.startFrom = startFrom; else
	    this.startFrom = null;
    }

    App() { this(null); }

    @Override public AreaLayout onAppInit()
    {
	this.conf = requireNonNullElse(getLuwrain().loadConf(Config.class), new Config());
	this.conv = new Conv(this);
	this.hooks = new Hooks(this);
	this.mainLayout = new MainLayout(this);
	this.operationsLayout = new OperationsLayout(this);
	setAppName(getStrings().appName());
	return mainLayout.getAreaLayout();
    }

    void runOperation(Operation op)
    {
	this.operations.add(0, op);
	getLuwrain().executeBkg(new FutureTask<>(op, null));
    }

    boolean allOperationsFinished()
    {
	for(Operation op:operations)
	    if (!op.isDone())
		return false;
	return true;
    }

    boolean closeOperation(int index)
    {
	if (index < 0 || index >= operations.size())
	    throw new IllegalArgumentException("index (" + String.valueOf(index) + ") must be positive and less than the number of operations (" + String.valueOf(operations.size()) + ")");
	if (!operations.get(index).isDone())
	    return false;
	operations.remove(index);
	operationsLayout.operationsArea.refresh();
	return true;
    }

    String getOperationResultDescr(Operation op)
    {
	requireNonNull(op, "op");
	if (op.getException() == null)
	    return getStrings().opResultOk();
	if (op.getException() instanceof OperationCancelledException)
	    return getStrings().opResultInterrupted();
	if (op.getException() instanceof java.nio.file.FileSystemException)
	    return op.getException().getMessage();
	return getLuwrain().i18n().getExceptionDescr(op.getException());
    }

    private OperationListener newOperationListener()
    {
	return new OperationListener(){
	    @Override public void onOperationProgress(Operation operation)
	    {
		getLuwrain().runUiSafely(()->{
			operationsLayout.operationsArea.redraw();
			if (operation.isDone())
			{
			    if (operation.getException() == null)
				getLuwrain().playSound(Sounds.DONE); else
				getLuwrain().playSound(Sounds.ERROR);
			    mainLayout.leftPanel.reread(false);
			    mainLayout.rightPanel.reread(false);
			}
		    });
	    }

	    @Override public Operation.ConfirmationChoices confirmOverwrite(Path path)
	    {
		if (conv == null)
		    return Operation.ConfirmationChoices.SKIP;
		return conv.overwriteConfirmation(path);
	    }
	};
    }

    @Override public boolean onEscape()
    {
	closeApp();
	return true;
    }

    @Override public void closeApp()
    {
	if (!allOperationsFinished())
	{
	    getLuwrain().message(getStrings().notAllOperationsFinished(), Luwrain.MessageType.ERROR);
	    return;
	}
	if (mainLayout != null)
	{
	    mainLayout.leftPanel.close();
	    mainLayout.rightPanel.close();
	}
	super.closeApp();
    }

    void layout(AreaLayout layout)
    {
	getLayout().setBasicLayout(layout);
	getLuwrain().announceActiveArea();
    }

    void layout(AreaLayout layout, Area activeArea)
    {
	getLayout().setBasicLayout(layout);
	getLuwrain().announceActiveArea();
	getLuwrain().setActiveArea(activeArea);
    }

    public Layouts layouts()
    {
	return new Layouts(){
	    @Override public void main()
	    {
		setAreaLayout(mainLayout);
		getLuwrain().announceActiveArea();
	    }
	    @Override public void operations()
	    {
		setAreaLayout(operationsLayout);
		getLuwrain().announceActiveArea();
	    }
	};
    }
}