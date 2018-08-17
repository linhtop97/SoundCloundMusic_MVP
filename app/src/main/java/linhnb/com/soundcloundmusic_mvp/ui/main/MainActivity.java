package linhnb.com.soundcloundmusic_mvp.ui.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

import linhnb.com.soundcloundmusic_mvp.R;
import linhnb.com.soundcloundmusic_mvp.data.model.Track;
import linhnb.com.soundcloundmusic_mvp.data.source.local.PreferenceManager;
import linhnb.com.soundcloundmusic_mvp.ui.home.HomeFragment;
import linhnb.com.soundcloundmusic_mvp.ui.maincontent.MainFragment;
import linhnb.com.soundcloundmusic_mvp.ui.playmusic.PlayMusicFragment;
import linhnb.com.soundcloundmusic_mvp.ui.splash.SplashFragment;
import linhnb.com.soundcloundmusic_mvp.utils.Constant;
import linhnb.com.soundcloundmusic_mvp.utils.FragmentManagerUtils;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity implements MainContract.View, HomeFragment.IPlayTrack {

    private final int SPLASH_DISPLAY_LENGTH = 3500;
    private MainContract.Presenter mPresenter;
    private Toolbar mToolbar;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolbar = findViewById(R.id.toolbar);
        mToolbar.setTitle(getString(R.string.title_home));
        setSupportActionBar(mToolbar);
        mPresenter = new MainPresenter(this);
        //get intent here and open mainfragment  and next open playmusic fragment
        Intent intent = getIntent();
        String action = intent.getAction();
        if (action != null && action.equals(Constant.ACTION_MAIN)) {
            FragmentManager manager = getSupportFragmentManager();
            MainFragment fragment = MainFragment.newInstance(true);
            FragmentManagerUtils.addFragment(manager, fragment, R.id.app_content,
                    fragment.getClass().getName(), false);
        } else {
            mPresenter.startPlashScreen();
        }
    }

    @Override
    public void setPresenter(MainContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showSplashScreen() {
        SplashFragment fragment = SplashFragment.newInstance(SPLASH_DISPLAY_LENGTH);
        FragmentManager manager = getSupportFragmentManager();
        FragmentManagerUtils.addFragment(manager, fragment, R.id.main_content,
                fragment.getClass().getSimpleName(), false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.dashbroad, menu);
        MenuItem mSearch = menu.findItem(R.id.action_search);

        SearchView mSearchView = (SearchView) mSearch.getActionView();
        mSearchView.setQueryHint(getString(R.string.search_message));

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void playTrack(int position) {
        PreferenceManager.setLastPosition(this, position);
        List<Track> tracks = PreferenceManager.getListTrack(this);
        PreferenceManager.setImageUrl(this, tracks.get(position).getArtworkUrl());
        PlayMusicFragment playMusicFragment = PlayMusicFragment.newInstance();
        FragmentManager manager = getSupportFragmentManager();
        FragmentManagerUtils.addFragment(manager, playMusicFragment,
                R.id.main_content, playMusicFragment.getClass().getName(), true);
    }

    @Override
    public void playTrack(List<Track> tracks, int position) {
        PreferenceManager.setLastPosition(this, position);
        PreferenceManager.putListTrack(this, tracks);
        PreferenceManager.setImageUrl(this, tracks.get(position).getArtworkUrl());
        PlayMusicFragment playMusicFragment = PlayMusicFragment.newInstance();
        FragmentManager manager = getSupportFragmentManager();
        FragmentManagerUtils.addFragment(manager, playMusicFragment,
                R.id.main_content, playMusicFragment.getClass().getName(), true);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String action = intent.getAction();
        if (action != null && action.equals(Constant.ACTION_MAIN)) {
            FragmentManager manager = getSupportFragmentManager();
            PlayMusicFragment musicFragment = PlayMusicFragment.newInstance();
            FragmentManagerUtils.addFragment(manager, musicFragment, R.id.main_content,
                    musicFragment.getClass().getName(), true);
        }
    }

    public void setTitle(String title) {
        mToolbar.setTitle(title);
    }
}
