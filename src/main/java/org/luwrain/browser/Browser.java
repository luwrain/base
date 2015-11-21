/*
   Copyright 2015 Roman Volovodov <gr.rPman@gmail.com>
   Copyright 2012-2015 Michael Pozhidaev <michael.pozhidaev@gmail.com>

   This file is part of the LUWRAIN.

   LUWRAIN is free software; you can redistribute it and/or
   modify it under the terms of the GNU General Public
   License as published by the Free Software Foundation; either
   version 3 of the License, or (at your option) any later version.

   LUWRAIN is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
   General Public License for more details.
*/

package org.luwrain.browser;

import org.luwrain.browser.ElementList.*;
import org.luwrain.core.Interaction;

public interface Browser
{
    Browser setInteraction(Interaction interaction);
    void Remove();

    boolean isBusy();

    void setVisibility(boolean enable);
    boolean getVisibility();

    void RescanDOM();
    void load(String link);
    void loadContent(String text);
    void stop();

    String getBrowserTitle();
    String getTitle();
    String getUrl();
    int numElements();

    Object executeScript(String script);

    SelectorAll selectorAll(boolean visible);
    SelectorText selectorText(boolean visible,String filter);
    SelectorTag selectorTag(boolean visible,String tagName,String attrName,String attrValue);
    SelectorCss selectorCss(boolean visible,String tagName,String styleName,String styleValue);
    ElementList elementList();
}
