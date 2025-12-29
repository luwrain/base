// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.app.player.layouts;

import java.io.*;
import org.apache.logging.log4j.*;
import groovy.util.*;

import org.luwrain.controls.*;
import org.luwrain.controls.wizard.*;
import org.luwrain.app.base.*;
import org.luwrain.app.player.*;

import static org.luwrain.util.ResourceUtils.*;

public final class NewAlbumLayout extends LayoutBase
{
    static private final Logger log = LogManager.getLogger();

    final App app;
    final WizardArea wizardArea;
    final WizardGroovyController controller;
    private String title = "";

    public NewAlbumLayout(App app, EditableListArea<Album> area, ActionHandler closing) throws IOException
    {
	super(app);
	this.app = app;
	wizardArea = new WizardArea(getControlContext()) ;
	controller = new WizardGroovyController(getLuwrain(), wizardArea){
		public Strings getStrings() { return app.getStrings(); }
		public void addDirAlbum(String title, String path)
		{
		    final var m = (Albums)area.getListModel();
		    m.add(new Album(Album.Type.DIR, title, path, null));
		    area.refresh();
		    closing.onAction();
		}
		//		public void skip() {app.layouts().main(); }
	    };
	Eval.me("wizard", controller, getStringResource(this.getClass(), "new-album.groovy"));
	setAreaLayout(wizardArea, null);
	setCloseHandler(closing);
    }
}
