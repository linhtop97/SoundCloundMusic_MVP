package linhnb.com.soundcloundmusic_mvp.ui.main;

import linhnb.com.soundcloundmusic_mvp.ui.base.BasePresenter;
import linhnb.com.soundcloundmusic_mvp.ui.base.BaseView;

public interface MainContract {

    interface View extends BaseView<Presenter> {
        void showSplashScreen();
    }

    interface Presenter extends BasePresenter {
        void startPlashScreen();
    }
}
