// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.app.player;

import java.util.*;
import java.io.*;
import org.apache.logging.log4j.*;

import org.luwrain.core.*;
import org.luwrain.controls.*;

final class Albums extends ArrayList<Album> implements EditableListArea.Model<Album>
{
    static private final Logger log = LogManager.getLogger();

    private final App app;

    Albums(App app)
    {
	this.app = app;
	load();
    }

    private synchronized void load()
    {
	clear();
	if (app.conf.albums == null)
	    return;
	addAll(app.conf.albums);
    }

    synchronized void save()
    {
	app.conf.albums = new ArrayList<>();
	app.conf.albums.addAll(this);
	app.getLuwrain().saveConf(app.conf);
    }

    synchronized int addAlbum(int pos, Album album)
    {
	if (pos < 0 || pos >= size())
	{
	    add(album);
	    	save();
	    return size() - 1;
	}
	add(pos, album);
	save();
	return pos;
    }

    synchronized void deleteAlbum(int index) throws IOException
    {
	if (index < 0 || index >= size())
	    throw new IllegalArgumentException("index (" + String.valueOf(index) + ") must be non-negative and less than " + String.valueOf(size()));
	remove(index);
	save();
    }

    @Override public synchronized boolean addToModel(int pos, java.util.function.Supplier<Object> supplier)
    {
	final Object supplied = supplier.get();
	if (supplied == null)
	    return false;
	final Object[] newObjs;
	if (supplied instanceof Object[])
	    newObjs = (Object[])supplied; else
	    newObjs = new Object[]{supplied};
	if (newObjs.length == 0)
	    return false;
	for(Object o: newObjs)
	    if (!(o instanceof Album))
	    {
				log.error("Illegal class of album object: " + o.getClass().getName());
		return false;
	    }
	    addAll(pos, Arrays.asList(Arrays.copyOf(newObjs, newObjs.length, Album[].class)));
	save();
	return true;
    }

    @Override public synchronized boolean removeFromModel(int posFrom, int posTo)
    {
	removeRange(posFrom, posTo);
	save();
	return true;
    }

    @Override public synchronized int getItemCount()
    {
	return size();
    }

    @Override public synchronized Album getItem(int index)
    {
	return get(index);
    }

    @Override public synchronized void refresh()
    {
    }
}
