package linhnb.com.soundcloundmusic_mvp.data.source;

import java.util.List;

import linhnb.com.soundcloundmusic_mvp.data.model.PlayList;
import linhnb.com.soundcloundmusic_mvp.data.model.Track;

public interface PlayListDataSource {

    List<PlayList> getAllPlayList();

    boolean addPlayList(PlayList playList);

    boolean deletePlayList(PlayList playList);

    boolean updatePlayList(PlayList playList);

    PlayList getPlayListByName(String playListName);

    int addTrack(String playListName, Track track);

    boolean removeTrack(String playListName, Track track);

    List<Track> getAllTrack(String playListName);

    boolean checkTrackExistPlayList(String playListName, Track track);
}
