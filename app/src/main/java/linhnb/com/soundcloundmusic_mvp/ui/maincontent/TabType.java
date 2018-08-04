package linhnb.com.soundcloundmusic_mvp.ui.maincontent;

import android.support.annotation.IntDef;

import static linhnb.com.soundcloundmusic_mvp.ui.maincontent.TabType.HOME;
import static linhnb.com.soundcloundmusic_mvp.ui.maincontent.TabType.LOCAL_MUSIC;
import static linhnb.com.soundcloundmusic_mvp.ui.maincontent.TabType.PLAYLIST;

@IntDef({HOME, LOCAL_MUSIC, PLAYLIST})
public @interface TabType {
    int HOME = 0;
    int LOCAL_MUSIC = 1;
    int PLAYLIST = 2;
}
