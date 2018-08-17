package linhnb.com.soundcloundmusic_mvp.data.source;

import android.support.annotation.NonNull;

import java.util.List;

import linhnb.com.soundcloundmusic_mvp.data.model.Track;

public interface TracksDataSource {

    void searchTracks(String trackName, @NonNull LoadTracksCallback callback);

    interface LoadTracksCallback {

        void onTracksLoaded(List<Track> tracks);

        void onDataNotAvailable(String msg);
    }

    interface LocalDataSource extends TracksDataSource {

        void deleteTrack(@NonNull String taskId);

        void addTrackToPlayList(Track track);

        List<Track> getTracks();

    }

    interface RemoteDataSource extends TracksDataSource {

        void getTracks(String genre, int limit, int offSet, @NonNull LoadTracksCallback callback);
    }
}
