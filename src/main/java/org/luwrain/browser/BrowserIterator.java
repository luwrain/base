/*
   Copyright 2012-2017 Michael Pozhidaev <michael.pozhidaev@gmail.com>
   Copyright 2015-2016 Roman Volovodov <gr.rPman@gmail.com>

   This file is part of LUWRAIN.

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

import java.awt.Rectangle;

public interface BrowserIterator
{
	Browser getBrowser();
    int getPos();
    /** although it is strongly discouraged to use this method.*/ 
boolean setPos(int index);
    String getText();

    /** get list of values for HTML SELECT FIXME: it's awful */
    String[] getMultipleText();

    String getComputedText();

    String getLink();

    Rectangle getRect();

    boolean isEditable();

    void setText(String text);
    String getAttributeProperty(String name);
    String getComputedStyleProperty(final String name);
    String getComputedStyleAll();
    String getHtmlTagName();
    void emulateClick();
	void emulateSubmit();

BrowserIterator getParent();
    boolean isVisible();
    boolean forTEXT();
BrowserIterator clone();

	String getAltText();

    boolean hasParent();
    boolean isParent(BrowserIterator it);

}
