// SPDX-License-Identifier: Apache-2.0
// Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.app.commander;

import java.util.*;
import org.graalvm.polyglot.*;
import org.apache.commons.vfs2.*;

import org.luwrain.core.*;
import org.luwrain.app.base.*;
import static org.luwrain.script.Hooks.*;
import static org.luwrain.script.ScriptUtils.*;
import static org.luwrain.app.commander.PanelArea.*;

final class Hooks
{
static private final String
    PANEL_ACTIONS = "luwrain.commander.panel.actions";

    private final App app;
    Hooks(App app) { this.app = app; }

    LayoutBase.ActionInfo[] panelActions(MainLayout layout, PanelArea panelArea, PanelArea otherPanelArea)
    {
	final List<LayoutBase.ActionInfo> res = new ArrayList<>();
	final Object[] items = collectorForArrays(app.getLuwrain(), PANEL_ACTIONS, new Object[0]);
	if (items == null)
	    return new LayoutBase.ActionInfo[0];
	for(Object item: items)
	{
	    final String
	    name = asString(getMember(item, "name")),
	    title = asString(getMember(item, "title"));
	    if (name == null || name.trim().isEmpty() ||
		title == null || title.trim().isEmpty())
		continue;
	    final Value action = getMember(item, "action");
	    if (action == null || action.isNull() || !action.canExecute())
		continue;
	    res.add(layout.action(name.trim(), title.trim(), ()->{
			final String selected;
			final List<String> marked = new ArrayList<>();
			if (panelArea.getSelectedEntry() != null)
			    selected = asFile(panelArea.getSelectedEntry()).getAbsolutePath(); else
			    selected = null;
			for(FileObject o: panelArea.getToProcess())
			    marked.add(asFile(o).getAbsolutePath());
			final Value actRes = action.execute(new Object[]{
				selected,
				getArray(marked)
			    });
			if (actRes == null || actRes.isNull() || !actRes.isBoolean())
			    return false;
			return actRes.asBoolean();
		    }));
	}
	return res.toArray(new LayoutBase.ActionInfo[res.size()]);
    }
}
