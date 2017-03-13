
package org.luwrain.player;

public interface Listener
{
    void onNewPlaylist(Playlist playlist);
    void onNewTrack(Playlist playlist, int trackNum);
    void onTrackTime(Playlist playlist, int trackNum,  long msec);
    void onPlayerStop();
}
