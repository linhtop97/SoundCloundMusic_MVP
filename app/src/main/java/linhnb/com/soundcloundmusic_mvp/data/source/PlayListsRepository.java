package linhnb.com.soundcloundmusic_mvp.data.source;

import java.util.List;

import linhnb.com.soundcloundmusic_mvp.data.model.PlayList;
import linhnb.com.soundcloundmusic_mvp.data.model.Track;
import linhnb.com.soundcloundmusic_mvp.data.source.local.PlayListLocalDataSource;

public class PlayListsRepository implements PlayListDataSource {

    private static PlayListsRepository sInstance;
    private PlayListLocalDataSource mPlayListLocalDataSource;

    private PlayListsRepository(PlayListLocalDataSource playListLocalDataSource) {
        mPlayListLocalDataSource = playListLocalDataSource;
    }

    public static PlayListsRepository getInstance(PlayListLocalDataSource playListLocalDataSource) {
        if (sInstance == null) {
            synchronized (PlayListsRepository.class) {
                if (sInstance == null) {
                    sInstance = new PlayListsRepository(playListLocalDataSource);
                }
            }
        }
        return sInstance;
    }

    @Override
    public List<PlayList> getAllPlayList() {
        return mPlayListLocalDataSource.getAllPlayList();
    }

    @Override
    public boolean addPlayList(PlayList playList) {
        return mPlayListLocalDataSource.addPlayList(playList);
    }

    @Override
    public boolean deletePlayList(PlayList playList) {
        return false;
    }

    @Override
    public boolean updatePlayList(PlayList playList) {
        return mPlayListLocalDataSource.updatePlayList(playList);
    }

    @Override
    public PlayList getPlayListByName(String playListName) {
        return null;
    }

    @Override
    public boolean addTrack(String playListName, Track track) {
        return false;
    }

    @Override
    public boolean removeTrack(String playListName, Track track) {
        return false;
    }

    @Override
    public List<Track> getAllTrack(String playListName) {
        return null;
    }

    @Override
    public boolean checkTrackExistPlayList(String playListName, Track track) {
        return false;
    }
}
