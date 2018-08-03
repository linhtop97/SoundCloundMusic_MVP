package linhnb.com.soundcloundmusic_mvp;

import android.app.Application;

public class MusicPlayerApplication extends Application {

    private static MusicPlayerApplication sInstance;

    public static MusicPlayerApplication getInstance() {
        return sInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
    }
}
