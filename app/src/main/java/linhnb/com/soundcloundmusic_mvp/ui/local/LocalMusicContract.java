package linhnb.com.soundcloundmusic_mvp.ui.local;

import java.util.List;

import linhnb.com.soundcloundmusic_mvp.data.model.Track;
import linhnb.com.soundcloundmusic_mvp.ui.base.BasePresenter;
import linhnb.com.soundcloundmusic_mvp.ui.base.BaseView;

public interface LocalMusicContract {

    interface View extends BaseView<Presenter> {
        void showTracks(List<Track> tracks);
    }

    interface Presenter extends BasePresenter {
        void loadLocalTracks();
    }
}
