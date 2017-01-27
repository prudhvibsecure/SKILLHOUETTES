package com.sharmastech.skillhouettes.controls;

//**************************************************************************/

import android.media.MediaPlayer;

import java.io.IOException;

//**************************************************************************/

public class StatefulMediaPlayer extends MediaPlayer {
    // **************************************************************************/

    public interface StatefulMediaPlayerObserver {
        void onPlayerStateChanged(PlayerState state);
    }

    // **************************************************************************/

    public enum PlayerState {
        Preparing,
        Playing,
        Paused,
        Stopped
    }

    // **************************************************************************/

    private static final String mLogTag = "StatefulMediaPlayer";
    private PlayerState mPlayerState = PlayerState.Stopped;
    private StatefulMediaPlayerObserver mObserver;

    // **************************************************************************/

    public StatefulMediaPlayer() {
        super();
    }

    // **************************************************************************/

    public void start() {
        super.start();

        setState(PlayerState.Playing);

        //Log.d(mLogTag, "start:" + this);
    }

    // **************************************************************************/

    public void stop() {
        super.stop();

        reset();

        setState(PlayerState.Stopped);

        //Log.d(mLogTag, "stop:" + this);
    }

    // **************************************************************************/

    public void pause() {
        super.pause();

        setState(PlayerState.Paused);

        //Log.d(mLogTag, "pause:" + this);
    }

    // **************************************************************************/

    public void prepare() throws IOException, IllegalStateException {
        super.prepare();

        setState(PlayerState.Preparing);

        //Log.d(mLogTag, "prepare:" + this);
    }

    // **************************************************************************/

    public void prepareAsync() {
        super.prepareAsync();

        setState(PlayerState.Preparing);

        //Log.d(mLogTag, "prepareAsync:" + this);
    }

    // **************************************************************************/

    public void reset() {
        super.reset();

        setState(PlayerState.Stopped);

        //Log.d(mLogTag, "reset:" + this);
    }

    // **************************************************************************/

    private void setState(PlayerState state) {
        mPlayerState = state;

        if (mObserver != null) {
            mObserver.onPlayerStateChanged(state);
        }

        //Log.d(mLogTag, String.format("Player state changed to %s", mPlayerState.name()));
    }

    // **************************************************************************/

    public PlayerState getState() {
        return mPlayerState;
    }

    // **************************************************************************/

    public boolean isPreparing() {
        return (mPlayerState == PlayerState.Preparing);
    }

    // **************************************************************************/

    public boolean isPlaying() {
        return (mPlayerState == PlayerState.Playing);
    }

    // **************************************************************************/

    public boolean isPaused() {
        return (mPlayerState == PlayerState.Paused);
    }

    // **************************************************************************/

    public boolean isStopped() {
        return (mPlayerState == PlayerState.Stopped);
    }

    // **************************************************************************/

    public void setObserver(StatefulMediaPlayerObserver observer) {
        mObserver = observer;
    }

    // **************************************************************************/

    public StatefulMediaPlayerObserver getObserver() {
        return mObserver;
    }

    // **************************************************************************/
}
