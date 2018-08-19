package linhnb.com.soundcloundmusic_mvp.data.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import linhnb.com.soundcloundmusic_mvp.data.source.PlayListDataSource;

public class PlayListDataBase extends SQLiteOpenHelper implements DBUtils, PlayListDataSource {

    public static final int ERROR = 0;
    public static final int EXISTS = 1;
    public static final int SUCCESS = 2;
    public static final String FAVORITE_PLAYLIST = "Favorite";
    public static final String CREATE_TRACK_TABLE_SQL =
            "CREATE TABLE " + TRACK_TBL_NAME + "("
                    + COLUMN_TRACK_ID + " " + INTEGER_DATA_TYPE + " " + PRIMARY_KEY + " " + AUTOINCREMENT + ", "
                    + COLUMN_TITLE + " " + TEXT_DATA_TYPE + " " + NOT_NULL + ", "
                    + COLUMN_ARTIST + " " + TEXT_DATA_TYPE + ", "
                    + COLUMN_DURATION + " " + INTEGER_DATA_TYPE + " " + NOT_NULL + ", "
                    + COLUMN_TRACK_URI + " " + TEXT_DATA_TYPE + " " + NOT_NULL
                    + ")";

    public static final String CREATE_PLAYLIST_TABLE_SQL =
            "CREATE TABLE " + PLAYLIST_TBL_NAME + "("
                    + COLUMN_PLAYLIST_ID + " " + INTEGER_DATA_TYPE + " " + PRIMARY_KEY + " " + AUTOINCREMENT + ", "
                    + COLUMN_PLAYLIST_NAME + " " + TEXT_DATA_TYPE + " " + NOT_NULL + ", "
                    + COLUMN_NUMBER_TRACKS + " " + INTEGER_DATA_TYPE + " " + DEFAULT + " 0"
                    + ")";

    public static final String CREATE_PLAYLIST_TRACK_TABLE_SQL =
            "CREATE TABLE " + PLAYLIST_TRACK_TBL_NAME + "("
                    + COLUMN_PLAYLIST_NAME + " " + TEXT_DATA_TYPE + " " + NOT_NULL + ", "
                    + TRACK_ID + " " + INTEGER_DATA_TYPE + " " + NOT_NULL
                    + ")";

    public static final String INSERT_FAVORITE_LIST_TRACK_TO_PLAYLIST =
            "INSERT INTO " + PLAYLIST_TBL_NAME + "("
                    + COLUMN_PLAYLIST_NAME + ")" + " VALUES "
                    + "('" + FAVORITE_PLAYLIST + "')";

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
                COLUMN_PLAYLIST_ID,
                COLUMN_PLAYLIST_NAME,
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
        if (cursor != null) {
            cursor.close();
        }
        return playLists;
    }

    private PlayList cursorToPlayList(Cursor cursor) {
        PlayList playList = new PlayList();
        playList.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PLAYLIST_ID)));
        playList.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PLAYLIST_NAME)));
        playList.setNumberTracks(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_NUMBER_TRACKS)));
        return playList;
    }

    @Override
    public boolean addPlayList(PlayList playList) {
        if (playList == null) {
            return false;
        }
        SQLiteDatabase database = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_PLAYLIST_NAME, playList.getName());
        long result = database.insert(PLAYLIST_TBL_NAME, null, contentValues);
        return result != -1;
    }

    @Override
    public boolean deletePlayList(PlayList playList) {
        if (playList == null) {
            return false;
        }
        SQLiteDatabase database = this.getWritableDatabase();
        int index = database.delete(PLAYLIST_TBL_NAME, COLUMN_PLAYLIST_ID + " = ?",
                new String[]{String.valueOf(playList.getId())});
        return index > 0;
    }

    @Override
    public boolean updatePlayList(PlayList playList) {
        if (playList == null) {
            return false;
        }
        SQLiteDatabase database = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_PLAYLIST_NAME, playList.getName());
        String where = COLUMN_PLAYLIST_ID + " = " + playList.getId();
        long result = database.update(PLAYLIST_TBL_NAME, contentValues, where, null);
        return result != -1;
    }

    @Override
    public PlayList getPlayListByName(String playListName) {
        return null;
    }

    @Override
    public int addTrack(String playListName, Track track) {
        //check track is exists on table tracks?
        List<Track> tracks = getAllTrack();
        int trackId;
        boolean isExist = false;
        for (Track t : tracks) {
            if (t.getTitle().equals(track.getTitle()) && t.getUri().equals(track.getUri())) {
                isExist = true;
                break;
            }
        }
        if (!isExist) {
            //insert track to DB and get id inserted
            if (insertTrackToDB(track)) {
                String sql = "SELECT MAX(" + COLUMN_TRACK_ID + ") FROM " + TRACK_TBL_NAME;
                SQLiteDatabase database = getReadableDatabase();
                Cursor cursor = database.rawQuery(sql, null);
                cursor.moveToFirst();
                trackId = cursor.getInt(0);
                cursor.close();
            } else {
                return ERROR;
            }
        } else {
            //find track id via track.uri
            trackId = getTrackIdByTrackUri(track);
        }
        //Get track id done => check track is exists Playlist
        if (checkTrackIsExistsInPlaylist(playListName, trackId)) {
            return EXISTS;
        }
        //3.insert track_id + playlist_id to play list track table
        if (insertTrackToPlayList(playListName, trackId)) {
            if (updatePlayList(playListName)) {
                return SUCCESS;
            }
        }
        return ERROR;
    }

    private boolean updatePlayList(String playListName) {
        int numberTracks = getNumberTrack(playListName) + 1;
        SQLiteDatabase database = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NUMBER_TRACKS, numberTracks);
        String where = COLUMN_PLAYLIST_NAME + " = '" + playListName + "'";
        long result = database.update(PLAYLIST_TBL_NAME, contentValues, where, null);
        return result != -1;
    }

    private int getNumberTrack(String playListName) {
        int numberTrack = 0;
        String selection = COLUMN_PLAYLIST_NAME + "=?";
        //get number tracks in playlist
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.query(PLAYLIST_TBL_NAME, new String[]{COLUMN_NUMBER_TRACKS}, selection,
                new String[]{String.valueOf(playListName)}, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            numberTrack = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_NUMBER_TRACKS));
            cursor.close();
        }

        return numberTrack;
    }

    private boolean insertTrackToPlayList(String playListName, int trackId) {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_PLAYLIST_NAME, playListName);
        contentValues.put(TRACK_ID, trackId);
        long result = database.insert(PLAYLIST_TRACK_TBL_NAME, null, contentValues);
        return result != -1;
    }

    private boolean checkTrackIsExistsInPlaylist(String playListName, int trackId) {
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.query(PLAYLIST_TRACK_TBL_NAME,
                new String[]{TRACK_ID}, COLUMN_PLAYLIST_NAME + "=? AND " + TRACK_ID + "=?",
                new String[]{playListName, String.valueOf(trackId)},
                null, null, null, null);
        int count = cursor.getCount();
        cursor.close();
        return count > 0;
    }

    private int getTrackIdByTrackUri(Track track) {
        int trackId = 0;
        String selection = COLUMN_TRACK_URI + "=?";
        //find track id by track uri in DB
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.query(TRACK_TBL_NAME, new String[]{COLUMN_TRACK_ID}, selection,
                new String[]{track.getUri()}, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            trackId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_TRACK_ID));
            cursor.close();
        }
        return trackId;
    }

    private List<Track> getAllTrack() {
        List<Track> tracks = new ArrayList<>();
        SQLiteDatabase database = getReadableDatabase();
        String[] columns = {
                COLUMN_TRACK_ID,
                COLUMN_TITLE,
                COLUMN_ARTIST,
                COLUMN_DURATION,
                COLUMN_TRACK_URI
        };
        Cursor cursor = database.query(TRACK_TBL_NAME, columns, null, null,
                null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                Track track = cursorToTrack(cursor);
                tracks.add(track);
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
        return tracks;
    }

    private boolean insertTrackToDB(Track track) {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_TITLE, track.getTitle());
        contentValues.put(COLUMN_ARTIST, track.getUserName());
        contentValues.put(COLUMN_DURATION, track.getDuration());
        contentValues.put(COLUMN_TRACK_URI, track.getUri());
        long result = database.insert(TRACK_TBL_NAME, null, contentValues);
        return result != -1;
    }

    private Track cursorToTrack(Cursor cursor) {
        return new Track.Builder().setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_TRACK_ID)))
                .setTitle(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE)))
                .setUserName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ARTIST)))
                .setDuration(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_DURATION)))
                .setUri(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TRACK_URI)))
                .build();
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
