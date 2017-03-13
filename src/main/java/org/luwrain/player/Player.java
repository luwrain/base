
package org.luwrain.player;

public interface Player
{
    static public final String SHARED_OBJECT_NAME = "luwrain.player";

    public enum Result {OK, INVALID_PLAYLIST, UNSUPPORTED_FORMAT_STARTING_TRACK};
    public enum Status {PLAYING, PAUSED, STOPPED};

    /**
     * Starts playing of the specified playlist. This method acts in separate
     * thread and returns execution control immediately. If there is a previous
     * playing, initiated prior to this call, it will be silently
     * cancelled. You may specify the desired track number and a position in
     * audio file to begin playing from.
     *
     * @param playlist A playlist to play
     * @param startingTrackNum A desired 0-based track number to play from
     * @param startingPosMsec A position in audio file in milliseconds to start playing from
     */
    Result play(Playlist playlist, int startingTrackNum, long startingPosMsec);
    void stop();
    void pauseResume();
    void jump(long offsetMsec);
    void nextTrack();
    void prevTrack();
    Playlist getCurrentPlaylist();
    int getCurrentTrackNum();
    void addListener(Listener listener);
    void removeListener(Listener listener);
}
