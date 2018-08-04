package linhnb.com.soundcloundmusic_mvp.ui.maincontent;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import linhnb.com.soundcloundmusic_mvp.ui.home.HomeFragment;
import linhnb.com.soundcloundmusic_mvp.ui.local.LocalMusicFragment;
import linhnb.com.soundcloundmusic_mvp.ui.playlist.PlaylistFragment;


public class MainPagerAdapter extends FragmentPagerAdapter {

    public static final int TAB_COUNT = 3;

    public MainPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case TabType.HOME:
                return HomeFragment.newInstance();
            case TabType.LOCAL_MUSIC:
                return LocalMusicFragment.newInstance();
            case TabType.PLAYLIST:
                return PlaylistFragment.newInstance();
        }
        return null;
    }

    @Override
    public int getCount() {
        return TAB_COUNT;
    }
}
