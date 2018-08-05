package linhnb.com.soundcloundmusic_mvp.source;


import android.support.annotation.NonNull;

import linhnb.com.soundcloundmusic_mvp.data.model.Track;
import linhnb.com.soundcloundmusic_mvp.source.local.TracksLocalDataSource;
import linhnb.com.soundcloundmusic_mvp.source.remote.TracksRemoteDataSource;

public class TracksRepository implements TracksDataSource.RemoteDataSource, TracksDataSource.LocalDataSource {

    private static TracksRepository sInstance;
    private TracksRemoteDataSource mTrackRemoteDataSource;
    private TracksLocalDataSource mTracksLocalDataSource;

    private TracksRepository(TracksRemoteDataSource trackRemoteDataSource,
                             TracksLocalDataSource tracksLocalDataSource) {
        mTrackRemoteDataSource = trackRemoteDataSource;
        mTracksLocalDataSource = tracksLocalDataSource;
    }

    public static TracksRepository getInstance(TracksRemoteDataSource trackRemoteDataSource,
                                               TracksLocalDataSource tracksLocalDataSource) {
        if (sInstance == null) {
            synchronized (TracksRepository.class) {
                if (sInstance == null) {
                    sInstance = new TracksRepository(trackRemoteDataSource, tracksLocalDataSource);
                }
            }
        }
        return sInstance;
    }

    @Override
    public void searchTracks(String trackName, @NonNull LoadTracksCallback callback) {

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
    public void getTracks(String genre, int limit, int offSet, @NonNull LoadTracksCallback callback) {
        mTrackRemoteDataSource.getTracks(genre, limit, offSet, callback);
    }
}
