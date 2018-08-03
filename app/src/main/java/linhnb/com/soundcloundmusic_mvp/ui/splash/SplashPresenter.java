package linhnb.com.soundcloundmusic_mvp.ui.splash;


import android.os.Handler;

public class SplashPresenter implements SplashContract.Presenter {

    private SplashContract.View mView;

    public SplashPresenter(SplashContract.View view) {
        mView = view;
        mView.setPresenter(this);
    }


    @Override
    public void startingDelay(long millisecond) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mView.showMainApp();
            }
        }, millisecond);
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {

    }
}
