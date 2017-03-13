
package org.luwrain.player;

import org.luwrain.core.*;

public class DefaultPlaylist implements Playlist, Comparable
{
    public interface Listener
    {
	void onTrackNum(int value);
	void onPosMsec(int value);
    }

    protected final String title;
    protected final String[] items;
    protected final int startingTrackNum;
    protected final long startingPosMsec;
    protected final Listener listener;

    public DefaultPlaylist(String title, String[] items,
			   int startingTrackNum, long startingPosMsec,
			   Listener listener)
    {
	NullCheck.notNull(title, "title");
	NullCheck.notNullItems(items, "items");
	this.title = title;
	this.items = items;
	this.startingTrackNum = startingTrackNum;
	this.startingPosMsec = startingPosMsec;
	this.listener = listener;
    }

    @Override public int getStartingTrackNum()
    {
	return startingTrackNum;
    }

    @Override public long getStartingPosMsec()
    {
	return startingPosMsec;
    }

    @Override public void updateStartingPos(int trackNum, long posMsec)
    {
	if (listener == null)
	    return;
	listener.onTrackNum(trackNum);
	listener.onPosMsec((int)posMsec);
    }

    @Override public String[] getPlaylistItems()
    {
	return items;
    }

    @Override public String getPlaylistTitle()
    {
	return title;
    }

    @Override public String toString()
    {
	return title;
    }

    @Override public int compareTo(Object obj)
    {
	if (obj == null || !(obj instanceof DefaultPlaylist))
	    return 0;
	return title.compareTo(((DefaultPlaylist)obj).title);
    }
}
