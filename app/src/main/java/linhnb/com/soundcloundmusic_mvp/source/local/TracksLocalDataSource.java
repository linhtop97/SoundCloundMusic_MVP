package linhnb.com.soundcloundmusic_mvp.source.local;

import android.support.annotation.NonNull;

import linhnb.com.soundcloundmusic_mvp.data.model.Track;
import linhnb.com.soundcloundmusic_mvp.source.TracksDataSource;

public class TracksLocalDataSource implements TracksDataSource.LocalDataSource {
    private static TracksLocalDataSource sInstance;

    private TracksLocalDataSource() {
    }

    public static TracksLocalDataSource getInstance() {
        if (sInstance == null) {
            synchronized (TracksLocalDataSource.class) {
                if (sInstance == null) {
                    sInstance = new TracksLocalDataSource();
                }
            }
        }
        return sInstance;
    }

    @Override
    public void deleteTrack(@NonNull String taskId) {

    }

    @Override
    public void addTrackToPlayList(Track track) {

    }

    @Override
    public void getTracks() {

    }

    @Override
    public void searchTracks(String trackName, @NonNull LoadTracksCallback callback) {

    }
}

