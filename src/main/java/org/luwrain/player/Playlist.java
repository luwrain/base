
package org.luwrain.player;

public interface Playlist
{
    String getPlaylistTitle();
    String[] getPlaylistItems();
    void updateStartingPos(int trackNum, long posMsec);
}
