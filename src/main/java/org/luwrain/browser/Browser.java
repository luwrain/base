/*
   Copyright 2012-2015 Michael Pozhidaev <michael.pozhidaev@gmail.com>

   This file is part of the Luwrain.

   Luwrain is free software; you can redistribute it and/or
   modify it under the terms of the GNU General Public
   License as published by the Free Software Foundation; either
   version 3 of the License, or (at your option) any later version.

   Luwrain is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
   General Public License for more details.
*/

package org.luwrain.browser;

import org.luwrain.browser.ElementList.*;
import org.luwrain.core.Interaction;

public interface Browser
{
    public Browser setInteraction(Interaction interaction);
    public void Remove();
    
    public void setVisibility(boolean enable);
    public boolean getVisibility();
    
    public void RescanDOM();
    public void load(String link);
    public void loadContent(String text);
    public void stop();
    
    public String getBrowserTitle();
    public String getTitle();
    public String getUrl();
    
    public Object executeScript(String script);
	
    SelectorALL selectorALL(boolean visible);
	SelectorTEXT selectorTEXT(boolean visible,String filter);
	SelectorTAG selectorTAG(boolean visible,String tagName,String attrName,String attrValue);
	SelectorCSS selectorCSS(boolean visible,String tagName,String styleName,String styleValue);
	ElementList elementList();
    
}
