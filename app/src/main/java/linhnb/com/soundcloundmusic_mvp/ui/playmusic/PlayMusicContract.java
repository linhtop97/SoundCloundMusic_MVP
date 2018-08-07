package linhnb.com.soundcloundmusic_mvp.ui.playmusic;

import android.support.annotation.Nullable;

import linhnb.com.soundcloundmusic_mvp.data.model.Track;
import linhnb.com.soundcloundmusic_mvp.ui.base.BasePresenter;
import linhnb.com.soundcloundmusic_mvp.ui.base.BaseView;
import linhnb.com.soundcloundmusic_mvp.ui.playmusic.service.MusicService;
import linhnb.com.soundcloundmusic_mvp.ui.playmusic.service.PlayMode;


public class PlayMusicContract {

    interface View extends BaseView<Presenter> {

        void onServiceBound(MusicService service);

        void onServiceUnbound();

        void onTrackUpdated(@Nullable Track track);

        void updatePlayMode(PlayMode playMode);

        void updatePlayToggle(boolean play);
    }

    interface Presenter extends BasePresenter {

        void retrieveLastPlayMode();

        void bindMusicService();

        void unbindMusicService();
    }
}
