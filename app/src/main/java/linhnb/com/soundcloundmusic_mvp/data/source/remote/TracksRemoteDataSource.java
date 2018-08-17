package linhnb.com.soundcloundmusic_mvp.data.source.remote;

import android.support.annotation.NonNull;

import linhnb.com.soundcloundmusic_mvp.data.source.TracksDataSource;
import linhnb.com.soundcloundmusic_mvp.utils.StringUtil;

public class TracksRemoteDataSource implements TracksDataSource.RemoteDataSource {
    private static TracksRemoteDataSource sInstance;

    private TracksRemoteDataSource() {
    }

    public static TracksRemoteDataSource getInstance() {
        if (sInstance == null) {
            synchronized (TracksRemoteDataSource.class) {
                if (sInstance == null) {
                    sInstance = new TracksRemoteDataSource();
                }
            }
        }
        return sInstance;
    }

    @Override
    public void getTracks(String genre, int limit, int offSet, @NonNull LoadTracksCallback callback) {
        new FetchTracksFromUrl(callback)
                .execute(StringUtil.convertUrlFetchMusicGenre(genre, limit, offSet));
    }

    public void searchTracks(String trackName, @NonNull LoadTracksCallback callback) {
    }
}

