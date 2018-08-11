package linhnb.com.soundcloundmusic_mvp.ui.splash;

import linhnb.com.soundcloundmusic_mvp.ui.base.BasePresenter;
import linhnb.com.soundcloundmusic_mvp.ui.base.BaseView;

public interface SplashContract {

    interface View extends BaseView<Presenter> {

        void showMainApp();
    }

    interface Presenter extends BasePresenter {

        void startingDelay(long millisecond);
    }
}
