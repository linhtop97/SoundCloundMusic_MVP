package linhnb.com.soundcloundmusic_mvp.ui.main;

public class MainPresenter implements MainContract.Presenter {

    private MainContract.View mView;

    public MainPresenter(MainContract.View view) {
        mView = view;
        mView.setPresenter(this);
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {

    }

    @Override
    public void startPlashScreen() {
        mView.showSplashScreen();
    }
}
