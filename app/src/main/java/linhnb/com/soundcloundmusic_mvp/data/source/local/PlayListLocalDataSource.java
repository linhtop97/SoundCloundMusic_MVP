package linhnb.com.soundcloundmusic_mvp.data.source.local;

import android.content.Context;

import java.util.List;

import linhnb.com.soundcloundmusic_mvp.data.model.PlayList;
import linhnb.com.soundcloundmusic_mvp.data.model.PlayListDataBase;
import linhnb.com.soundcloundmusic_mvp.data.model.Track;
import linhnb.com.soundcloundmusic_mvp.data.source.PlayListDataSource;

public class PlayListLocalDataSource implements PlayListDataSource {

    private static PlayListLocalDataSource sInstance;
    private PlayListDataBase mPlayListDataBase;

    private PlayListLocalDataSource(Context context) {
        mPlayListDataBase = new PlayListDataBase(context);
    }

    public static PlayListLocalDataSource getInstance(Context context) {
        if (sInstance == null) {
            synchronized (PlayListLocalDataSource.class) {
                if (sInstance == null) {
                    sInstance = new PlayListLocalDataSource(context);
                }
            }
        }
        return sInstance;
    }

    @Override
    public List<PlayList> getAllPlayList() {
        return mPlayListDataBase.getAllPlayList();
    }

    @Override
    public boolean addPlayList(PlayList playList) {
        return mPlayListDataBase.addPlayList(playList);
    }

    @Override
    public boolean deletePlayList(PlayList playList) {
        return mPlayListDataBase.deletePlayList(playList);
    }

    @Override
    public boolean updatePlayList(PlayList playList) {
        return mPlayListDataBase.updatePlayList(playList);
    }

    @Override
    public PlayList getPlayListByName(String playListName) {
        return null;
    }

    @Override
    public int addTrack(String playListName, Track track) {
        return mPlayListDataBase.addTrack(playListName, track);
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
