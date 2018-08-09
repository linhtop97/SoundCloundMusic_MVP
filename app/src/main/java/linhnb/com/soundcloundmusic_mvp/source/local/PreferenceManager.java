package linhnb.com.soundcloundmusic_mvp.source.local;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import linhnb.com.soundcloundmusic_mvp.data.model.Track;
import linhnb.com.soundcloundmusic_mvp.ui.maincontent.TabType;
import linhnb.com.soundcloundmusic_mvp.ui.playmusic.service.PlayMode;

public class PreferenceManager {

    private static final String PREFS_NAME = "config.xml";
    /**
     * Play mode from the last time.
     */
    private static final String KEY_PLAY_MODE = "playMode";
    private static final String KEY_POSITION = "position";
    private static final String KEY_LIST_TRACK = "tracks";
    private static final String KEY_IMAGE_URL = "imageUrl";
    private static final String KEY_IS_PLAYING = "isPlaying";
    private static final String KEY_TAB = "tab";

    private static SharedPreferences preferences(Context context) {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    private static SharedPreferences.Editor edit(Context context) {
        return preferences(context).edit();
    }

    public static PlayMode lastPlayMode(Context context) {
        String playModeName = preferences(context).getString(KEY_PLAY_MODE, null);
        if (playModeName != null) {
            return PlayMode.valueOf(playModeName);
        }
        return PlayMode.getDefault();
    }

    public static void setPlayMode(Context context, PlayMode playMode) {
        edit(context).putString(KEY_PLAY_MODE, playMode.name()).commit();
    }

    public static String getImageUrl(Context context) {
        return preferences(context).getString(KEY_IMAGE_URL, null);
    }

    public static void setTab(Context context, int tab) {
        edit(context).putInt(KEY_TAB, tab).commit();
    }

    public static int getTab(Context context) {
        return preferences(context).getInt(KEY_TAB, TabType.HOME);
    }

    public static void setImageUrl(Context context, String url) {
        edit(context).putString(KEY_IMAGE_URL, url).commit();
    }

    public static int getLastPosition(Context context) {
        return preferences(context).getInt(KEY_POSITION, 0);
    }

    public static void setLastPosition(Context context, int position) {
        edit(context).putInt(KEY_POSITION, position).commit();
    }

    public static boolean getIsPlaying(Context context) {
        return preferences(context).getBoolean(KEY_IS_PLAYING, false);
    }

    public static void setIsPlaying(Context context, boolean isPlaying) {
        edit(context).putBoolean(KEY_IS_PLAYING, isPlaying).commit();
    }

    public static List<Track> getListTrack(Context context) {
        String tracks = preferences(context).getString(KEY_LIST_TRACK, null);
        Type listType = new TypeToken<ArrayList<Track>>() {
        }.getType();
        return new Gson().fromJson(tracks, listType);
    }

    public static void putListTrack(Context context, List<Track> tracks) {
        edit(context).putString(KEY_LIST_TRACK, new Gson().toJson(tracks)).commit();
    }
}
