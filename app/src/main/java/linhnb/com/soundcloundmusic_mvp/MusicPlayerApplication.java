package linhnb.com.soundcloundmusic_mvp;

import android.app.Application;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class MusicPlayerApplication extends Application {

    private static MusicPlayerApplication sInstance;

    public static MusicPlayerApplication getInstance() {
        return sInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;

        // Custom fonts
        CalligraphyConfig.initDefault(
                new CalligraphyConfig.Builder()
                        .setDefaultFontPath("fonts/Roboto-Medium.ttf")
                        .setFontAttrId(R.attr.fontPath)
                        .build()
        );
    }
}
