// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.app.commander;

import java.util.*;

import com.google.auto.service.*;

import org.luwrain.core.*;

@AutoService(org.luwrain.core.Extension.class)
public class Extension extends EmptyExtension
{
    @Override public Command[] getCommands(Luwrain luwrain)
    {
	return new Command[]{ new SimpleShortcutCommand("commander")};
    }

    @Override public ExtensionObject[] getExtObjects(Luwrain luwrain)
    {
	return new ExtensionObject[]{

	    new DefaultShortcut("commander", App.class) {
		@Override public Application[] prepareApp(String[] args)
		{
		    if (args == null || args.length < 1)
			return new Application[]{new App()};
		    final var v = new ArrayList<Application>();
		    for(String s: args)
			if (s != null)
			    v.add(new App(s));
		    if (v.isEmpty())
			return new Application[]{new App()};
		    return v.toArray(new Application[v.size()]);
		}
	    },
	};
    }
}
