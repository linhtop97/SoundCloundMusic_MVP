package linhnb.com.soundcloundmusic_mvp.ui.playmusic;

import android.support.annotation.IntDef;

import static linhnb.com.soundcloundmusic_mvp.ui.playmusic.PageType.LIST;
import static linhnb.com.soundcloundmusic_mvp.ui.playmusic.PageType.LYRICS;
import static linhnb.com.soundcloundmusic_mvp.ui.playmusic.PageType.PLAY;

@IntDef({LIST, PLAY, LYRICS})
public @interface PageType {
    int LIST = 0;
    int PLAY = 1;
    int LYRICS = 2;
}
