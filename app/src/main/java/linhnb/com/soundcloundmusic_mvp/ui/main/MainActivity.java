package linhnb.com.soundcloundmusic_mvp.ui.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import linhnb.com.soundcloundmusic_mvp.R;
import linhnb.com.soundcloundmusic_mvp.ui.splash.SplashFragment;
import linhnb.com.soundcloundmusic_mvp.utils.FragmentManagerUtils;

public class MainActivity extends AppCompatActivity implements MainContract.View {

    private final int SPLASH_DISPLAY_LENGTH = 3500;
    private MainContract.Presenter mPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        showSplashScreen();

    }

    @Override
    public void setPresenter(MainContract.Presenter presenter) {
        mPresenter = presenter;
    }

    private void showSplashScreen() {
        SplashFragment fragment = SplashFragment.newInstance(SPLASH_DISPLAY_LENGTH);
        FragmentManager manager = getSupportFragmentManager();
        FragmentManagerUtils.addFragment(manager, fragment, R.id.main_content,
                fragment.getClass().getSimpleName(), false);
    }

}
