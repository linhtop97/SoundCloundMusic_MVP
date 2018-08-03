package linhnb.com.soundcloundmusic_mvp;

import android.content.Context;

public class Injection {

    public static Context provideContext() {
        return MusicPlayerApplication.getInstance();
    }
}
