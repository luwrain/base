// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2026 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.app.video;

import java.util.*;

import com.google.auto.service.*;
import org.luwrain.core.*;
import org.luwrain.cpanel.*;
import org.luwrain.i18n.*;

@AutoService(org.luwrain.core.Extension.class)
public final class Extension extends EmptyExtension
{
    @Override public Command[] getCommands(Luwrain luwrain)
    {
	return new Command[]{ new SimpleShortcutCommand("video") };
    }

    @Override public ExtensionObject[] getExtObjects(Luwrain luwrain)
    {
	return new ExtensionObject[]{
	    new Shortcut() {
		@Override public String getExtObjName() { return "video"; }
		@Override public Set<Flags> getShortcutFlags() { return EnumSet.noneOf(Flags.class); }
		@Override public Application[] prepareApp(String[] args)
		{
		    if (args.length == 0)
			return new Application[]{new App()};
		    final List<Application> v = new ArrayList();
		    for(String s: args)
			v.add(new App(s));
		    if (v.isEmpty())
			return new Application[]{new App()};
		    return v.toArray(new Application[v.size()]);
		}
		@Override public String[] getFileExtensions()
		{
		    return new String[0];
		}
	    },
	};
    }

    @Override public void i18nExtension(Luwrain luwrain, org.luwrain.i18n.I18nExtension i18nExt)
    {
	i18nExt.addCommandTitle(Lang.EN, "video", "Video player");
	i18nExt.addCommandTitle(Lang.RU, "video", "Видеоплеер");
    }
}
