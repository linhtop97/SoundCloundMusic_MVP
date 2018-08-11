package linhnb.com.soundcloundmusic_mvp.ui.playmusic.service;

import android.support.annotation.Nullable;

import java.util.List;

import linhnb.com.soundcloundmusic_mvp.data.model.Track;


public interface IPlay {

    void setPlayList(List<Track> tracks);

    void play();

    void play(List<Track> tracks);

    void play(List<Track> tracks, int startIndex);

    void play(Track track);

    void play(int position);

    void playPrevious();

    void playNext();

    void pause();

    boolean isPlaying();

    int getProgress();

    Track getPlayingTrack();

    void seekTo(int progress);

    void setPlayMode(PlayMode playMode);

    void registerCallback(Callback callback);

    void unregisterCallback(Callback callback);

    void removeCallbacks();

    void releasePlayer();

    interface Callback {

        void onSwitchPrevious(@Nullable Track previous);

        void onSwitchNext(@Nullable Track next);

        void onComplete(@Nullable Track next);

        void onPlayStatusChanged(boolean isPlaying);
    }
}
