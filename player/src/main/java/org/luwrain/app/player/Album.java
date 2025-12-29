// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.app.player;

import lombok.*;

import org.luwrain.player.Player;

import static java.util.Objects.*;

@Data
@NoArgsConstructor
public final class Album implements Comparable
{
    public enum Type {SECTION, STREAMING, DIR, M3U, UNKNOWN};

        private Type type;
    private String title, 	url, path;

    private Integer volume = null;
    private boolean savePos;
    private int trackNum;
    private long posMsec;

    public Album(Type type, String title, String path, String url)
    {
	this.type = requireNonNull(type, "type");
	this.title = requireNonNull(title, "title can't be null");
	this.path = path;
	this.url = url;
    }

public int getVolume()
    {
	if (volume == null)
	    return Player.MAX_VOLUME;
	return Math.min(Math.max(volume.intValue(), Player.MIN_VOLUME), Player.MAX_VOLUME);
    }

    public boolean isSection()
    {
	return this.type == Type.SECTION;
    }

    @Override public int compareTo(Object o)
    {
	if (o == null || !(o instanceof Album))
	    return 0;
	return getTitle().compareTo(((Album)o).getTitle());
    }

    @Override public String toString()
    {
	return getTitle();
    }
}
