
package org.luwrain.player;

public interface Playlist
{
    String getPlaylistTitle();
    String[] getPlaylistItems();
    int getStartingTrackNum();
    long getStartingPosMsec();
    void updateStartingPos(int trackNum, long posMsec);
}
