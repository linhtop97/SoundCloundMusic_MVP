package linhnb.com.soundcloundmusic_mvp.ui.home;

import java.util.List;

import linhnb.com.soundcloundmusic_mvp.data.model.Track;
import linhnb.com.soundcloundmusic_mvp.ui.base.BasePresenter;
import linhnb.com.soundcloundmusic_mvp.ui.base.BaseView;

interface HomeContract {

    interface View extends BaseView<Presenter> {

        void showTracks(List<Track> tracks);

        void showLoading();

        void hideLoading();

        void handleError(String msg);
    }

    interface Presenter extends BasePresenter {
        void loadTracks(String genre, int limit, int offSet);
    }
}
