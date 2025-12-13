// SPDX-License-Identifier: Apache-2.0
// Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.app.notepad;

import java.util.*;
import com.google.auto.service.*;

import org.luwrain.core.*;
import org.luwrain.cpanel.*;

@AutoService(org.luwrain.core.Extension.class)
public final class Extension extends EmptyExtension
{
    static private final Element controlPanelElement = new SimpleElement(StandardElements.APPLICATIONS, Extension.class.getName());

    @Override public Command[] getCommands(Luwrain luwrain)
    {
	return new Command[]{
	    new Command(){
		@Override public String getName()
		{
		    return "notepad";
		}
		@Override public void onCommand(Luwrain luwrain)
		{
		    luwrain.launchApp("notepad");
		}
	    }};
    }

    @Override public ExtensionObject[] getExtObjects(Luwrain luwrain)
    {
	return new ExtensionObject[]{
	    new DefaultShortcut("notepad", App.class) {
		@Override public Application[] prepareApp(String[] args)
		{
		    NullCheck.notNullItems(args, "args");
		    if (args.length == 0)
			return new Application[]{new App()};
		    final List<Application> v = new ArrayList<>();
		    for(String s: args)
			v.add(new App(s));
		    if (v.isEmpty())
			return new Application[]{new App()};
		    return v.toArray(new Application[v.size()]);
		}
	    },
	};
    }

    @Override public Factory[] getControlPanelFactories(Luwrain luwrain)
    {
	return new Factory[]{
	    new Factory() {
		@Override public Element[] getElements()
		{
		    return new Element[]{controlPanelElement};
		}
		@Override public Element[] getOnDemandElements(Element parent)
		{
		    return new Element[0];
		}
		@Override public org.luwrain.cpanel.Section createSection(Element el)
		{
		    final Strings strings = (Strings)luwrain.i18n().getStrings(Strings.class.getName());
		    if (el.equals(controlPanelElement))
			return new SimpleSection(controlPanelElement, strings.settingsFormName(), (controlPanel)->SettingsForm.create(controlPanel));
		    return null;
		}
	    }
	};
    }
}
