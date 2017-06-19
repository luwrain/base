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

import java.util.Vector;
import java.util.concurrent.Callable;

/**
 * An interface to web-browser engine. This interface provides
 * web-browser functionality with JavaScript support. The corresponding
 * back-end is based on WebKit implementation included in JavaFX.
 *
 * The methods of this interface don't cover any features related to the
 * view of the loaded page in any form. However, the client application
 * can request showing traditional graphical view of the loaded page.
 * The content of the loaded page is represented by a set of elements
 * which can be enumerated with {@link ElementIterator} class.
 *
 * @see ElementIterator Events
 */
public interface Browser
{
    void close();
    BrowserIterator createIterator();
    String getTitle();
    String getUrl();
    boolean getVisibility();
    Object executeScript(String script);
    void init(BrowserEvents events);
    void loadByUrl(String url);
    void loadByText(String text);
    int numElements();
    void rescanDom();
    Object runSafely(Callable callable);
    void setVisibility(boolean enable);
    void stop();

    //Experimental
    /** update watch array of node's indexes in dom list to observe page modification */
    void setWatchNodes(Iterable<Integer> indexes);
    /** restart update timer as fast as possble */
    void doFastUpdate();
    long getLastTimeChanged();
}
