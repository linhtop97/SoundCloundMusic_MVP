package linhnb.com.soundcloundmusic_mvp.data.model;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import linhnb.com.soundcloundmusic_mvp.data.source.PlayListDataSource;

public class PlayListDataBase extends SQLiteOpenHelper implements DBUtils, PlayListDataSource {

    public static final String CREATE_TRACK_TABLE_SQL =
            "CREATE TABLE " + TRACK_TBL_NAME + "("
                    + COLUMN_TRACK_ID + " " + INTEGER_DATA_TYPE + " " + PRIMARY_KEY + " " + AUTOINCREMENT + ", "
                    + COLUMN_TITLE + " " + TEXT_DATA_TYPE + " " + NOT_NULL + ", "
                    + COLUMN_ARTIST + " " + TEXT_DATA_TYPE + " " + NOT_NULL + ", "
                    + COLUMN_DURATION + " " + TEXT_DATA_TYPE + " " + NOT_NULL + ", "
                    + COLUMN_TRACK_IMAGE + " " + TEXT_DATA_TYPE + " " + NOT_NULL
                    + ")";

    public static final String CREATE_PLAYLIST_TABLE_SQL =
            "CREATE TABLE " + PLAYLIST_TBL_NAME + "("
                    + COLUMN_PLAYLIST_ID + " " + INTEGER_DATA_TYPE + " " + PRIMARY_KEY + " " + AUTOINCREMENT + ", "
                    + COLUMN_PLAYLIST_NAME + " " + TEXT_DATA_TYPE + " " + NOT_NULL + ", "
                    + COLUMN_PLAYLIST_IMAGE + " " + TEXT_DATA_TYPE + ", "
                    + COLUMN_NUMBER_TRACKS + " " + INTEGER_DATA_TYPE + " " + DEFAULT + " 0"
                    + ")";

    public static final String CREATE_PLAYLIST_TRACK_TABLE_SQL =
            "CREATE TABLE " + PLAYLIST_TRACK_TBL_NAME + "("
                    + PLAYLIST_ID + " " + INTEGER_DATA_TYPE + ", "
                    + TRACK_ID + " " + INTEGER_DATA_TYPE
                    + ")";

    public static final String INSERT_FAVORITE_LIST_TRACK_TO_PLAYLIST =
            "INSERT INTO " + PLAYLIST_TBL_NAME + "("
                    + COLUMN_PLAYLIST_NAME + ")" + " VALUES " + "('Favorite')";

    public PlayListDataBase(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TRACK_TABLE_SQL);
        db.execSQL(CREATE_PLAYLIST_TABLE_SQL);
        db.execSQL(CREATE_PLAYLIST_TRACK_TABLE_SQL);
        db.execSQL(INSERT_FAVORITE_LIST_TRACK_TO_PLAYLIST);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    @Override
    public List<PlayList> getAllPlayList() {
        List<PlayList> playLists = new ArrayList<>();
        SQLiteDatabase database = getReadableDatabase();
        String[] columns = {
                COLUMN_PLAYLIST_NAME,
                COLUMN_PLAYLIST_IMAGE,
                COLUMN_NUMBER_TRACKS
        };
        Cursor cursor = database.query(PLAYLIST_TBL_NAME, columns, null, null,
                null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                PlayList playList = cursorToPlayList(cursor);
                playLists.add(playList);
            } while (cursor.moveToNext());
        }
        return playLists;
    }

    private PlayList cursorToPlayList(Cursor cursor) {
        PlayList playList = new PlayList();
        playList.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PLAYLIST_NAME)));
        playList.setImage(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PLAYLIST_IMAGE)));
        playList.setNumberTracks(Integer.parseInt(cursor.
                getString(cursor.getColumnIndexOrThrow(COLUMN_NUMBER_TRACKS))));
        return playList;
    }

    @Override
    public boolean addPlayList(PlayList playList) {
        return false;
    }

    @Override
    public boolean deletePlayList(PlayList playList) {
        return false;
    }

    @Override
    public boolean updatePlayList(PlayList playList) {
        return false;
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
