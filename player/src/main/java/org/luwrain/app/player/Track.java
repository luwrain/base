// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.app.player;

import java.util.*;

import org.luwrain.core.*;

import static java.util.Objects.*;

final class Track
{
    final String url;
    final Map<String, TrackInfo> trackInfoMap;

    Track(String url, Map<String, TrackInfo> trackInfoMap)
    {
	NullCheck.notEmpty(url, "url");
	requireNonNull(trackInfoMap, "trackInfoMap can't be null");
	this.url = url;
	this.trackInfoMap = trackInfoMap;
    }

    String getTitle()
    {
	final TrackInfo info = trackInfoMap.get(url);
	if (info == null)
	    return url;
	final StringBuilder b = new StringBuilder();
	b.append(info.artist).append(" - ").append(info.title);
	return new String(b);
    }

    @Override public String toString()
    {
	return getTitle();
    }

    @Override public boolean equals(Object o)
    {
	if (o == null || !(o instanceof Track))
	    return false;
	return url.equals(((Track)o).url);
    }
}
