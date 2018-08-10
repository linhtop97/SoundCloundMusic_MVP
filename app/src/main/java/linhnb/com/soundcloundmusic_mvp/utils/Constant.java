package linhnb.com.soundcloundmusic_mvp.utils;

public class Constant {
    // Network
    public static final String BASE_URL = "https://api-v2.soundcloud.com/";
    public static final String PARA_MUSIC_GENRE = "charts?kind=top&genre=soundcloud%3Agenres%3A";
    public static final String PARA_SEARCH_TRACK = "search/tracks?facet=genre&limit=10&linked_partitioning=1&q=";
    public static final String PARA_OFFSET = "offset";
    public static final String PARA_STREAM = "stream";
    public static final String CLIENT_ID = "client_id";
    public static final String REQUEST_METHOD_GET = "GET";
    public static final String LIMIT = "limit";
    public static final int READ_TIME_OUT = 5000; /* milliseconds */
    public static final int CONNECT_TIME_OUT = 5000; /* milliseconds */
    public static final int LIMIT_DEFAULT = 50;
    public static final int OFFSET_DEFAULT = 0;
    public static final String NULL_RESULT = "null";
    public static final String[] MUSIC_GENRES = {"pop", "all-music", "all-audio",
            "alternativerock", "ambient", "classical", "country"};

    // String
    public static final String BREAK_LINE = "\n";
    public static final String INTERNET_NOT_AVAIABLE = "internet not avaiable";
    public static final String FILE_EXTENTION = ".mp3";
    public static final String ARGUMENT_TIME_DELAY = "ARGUMENT_PLAY_ANIM";

    //extras
    public static final String EXTRA_POSITION = "EXTRA_POSITION";
    public static final String EXTRA_LIST_TRACK = "EXTRA_LIST_TRACK";

    //argument
    public static final String ARGUMENT_TRACKS = "ARGUMENT_TRACKS";
    public static final String ARGUMENT_IS_OPEN = "ARGUMENT_IS_OPEN";

    //Action
    public static final String ACTION_MAIN = "linhnb.com.soundcloundmusic_mvp.ACTION_MAIN";
    public static final String ACTION_PLAY_NOW = "linhnb.com.soundcloundmusic_mvp.ACTION_PLAY_NOW";
    public static final String ACTION_PLAY_TOGGLE = "linhnb.com.soundcloundmusic_mvp.ACTION.PLAY_TOGGLE";
    public static final String ACTION_PLAY_PREVIOUS = "linhnb.com.soundcloundmusic_mvp.ACTION_PLAY_PREVIOUS";
    public static final String ACTION_PLAY_NEXT = "linhnb.com.soundcloundmusic_mvp.ACTION.PLAY_NEXT";
    public static final String ACTION_STOP_SERVICE = "linhnb.com.soundcloundmusic_mvp.ACTION.STOP_SERVICE";
}
