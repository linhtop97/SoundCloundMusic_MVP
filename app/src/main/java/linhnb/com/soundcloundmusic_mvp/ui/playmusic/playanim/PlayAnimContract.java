package linhnb.com.soundcloundmusic_mvp.ui.playmusic.playanim;


import linhnb.com.soundcloundmusic_mvp.ui.base.BasePresenter;
import linhnb.com.soundcloundmusic_mvp.ui.base.BaseView;

public class PlayAnimContract {

    interface View extends BaseView<Presenter> {
        void setImage();

        void startAnimation();

        void cancelAnimation();
    }

    interface Presenter extends BasePresenter {
    }
}
