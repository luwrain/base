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

public interface BrowserEvents
{
    public enum State {CANCELLED, FAILED, READY, RUNNING, SCHEDULED, SUCCEEDED};

    void onChangeState(State state);
    void onProgress(Number progress);
    void onAlert(String message);
    String onPrompt(String message,String value);
    void onError(String message);
    boolean onDownloadStart(String url);
    Boolean onConfirm(String message);
void onPageChanged();
}
