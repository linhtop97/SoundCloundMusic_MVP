package linhnb.com.soundcloundmusic_mvp.data.model;

public interface DBUtils {

    String DB_NAME = "PLAYLIST";
    int DB_VERSION = 1;

    String TRACK_TBL_NAME = "tblTrack";
    String COLUMN_TRACK_ID = "PK_iTrackId";
    String COLUMN_TITLE = "tTitle";
    String COLUMN_ARTIST = "tArtist";
    String COLUMN_DURATION = "iDuration";
    String COLUMN_TRACK_IMAGE = "tTrackImage";

    String PLAYLIST_TBL_NAME = "tblPlayList";
    String COLUMN_PLAYLIST_ID = "PK_iPlayListId";
    String COLUMN_PLAYLIST_NAME = "tPlayListName";
    String COLUMN_NUMBER_TRACKS = "iNumberTracks";
    String COLUMN_PLAYLIST_IMAGE = "tPlayListImage";

    String PLAYLIST_TRACK_TBL_NAME = "tblPlayListTrack";
    String TRACK_ID = "FK_iTrackId";
    String PLAYLIST_ID = "FK_iPlayListId";

    String PRIMARY_KEY = "PRIMARY KEY";
    String AUTOINCREMENT = "AUTOINCREMENT";
    String NOT_NULL = "NOT NULL";
    String NULL = "NULL";
    String DEFAULT = "DEFAULT";

    String TEXT_DATA_TYPE = "TEXT";
    String INTEGER_DATA_TYPE = "INTEGER";
    String REAL_DATA_TYPE = "REAL";
    String BLOB_DATA_TYPE = "BLOB";
}
