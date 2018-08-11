package linhnb.com.soundcloundmusic_mvp.utils;

import linhnb.com.soundcloundmusic_mvp.BuildConfig;

public class StringUtil {
    public static String convertUrlFetchMusicGenre(String genre, int limit, int offset) {
        return String.format("%s%s%s&%s=%s&%s=%d&%s=%d", Constant.BASE_URL,
                Constant.PARA_MUSIC_GENRE, genre, Constant.CLIENT_ID,
                BuildConfig.API_KEY, Constant.LIMIT, limit, Constant.PARA_OFFSET, offset);
    }

    public static String getUrlStreamTrack(String uriTrack) {
        return String.format("%s/%s?%s=%s", uriTrack, Constant.PARA_STREAM,
                Constant.CLIENT_ID, BuildConfig.API_KEY);
    }

    public static String getUrlDownload(String url) {
        StringBuffer stringBuffer = new StringBuffer(url);
        stringBuffer.append("/stream?")
                .append(Constant.CLIENT_ID)
                .append('=')
                .append(BuildConfig.API_KEY);
        return stringBuffer.toString();
    }

    public static String formatDuration(int duration) {
        duration /= 1000; // milliseconds into seconds
        int minute = duration / 60;
        int hour = minute / 60;
        minute %= 60;
        int second = duration % 60;
        if (hour != 0)
            return String.format("%2d:%02d:%02d", hour, minute, second);
        else
            return String.format("%02d:%02d", minute, second);
    }

    public static String formatCount(int count) {
        float number = (float) count / 1000000;
        if ((int) number > 0) {
            return String.format("%.1f Tr", number);
        }
        number = (float) count / 1000;
        if ((int) number > 0) {
            return String.format("%.1f K", number);
        }

        return String.valueOf(count);
    }

    public static String convertUrlDownloadTrack(String url) {
        return String.format("%s?%s=%s", url, Constant.CLIENT_ID, BuildConfig.API_KEY);
    }

    //
    public static String convertUrlSearchTrack(String trackName) {
        return String.format("%s%s%s&%s=%d&%s=%s", Constant.BASE_URL, Constant.PARA_SEARCH_TRACK, trackName,
                Constant.PARA_OFFSET, 100, Constant.CLIENT_ID,
                BuildConfig.API_KEY);
    }
}
