package linhnb.com.soundcloundmusic_mvp.ui.playmusic;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import linhnb.com.soundcloundmusic_mvp.ui.playmusic.listtrack.ListTrackFragment;
import linhnb.com.soundcloundmusic_mvp.ui.playmusic.lyric.LyricsFragment;
import linhnb.com.soundcloundmusic_mvp.ui.playmusic.playanim.PlayAnimationFragment;

public class PlayMusicPagerAdapter extends FragmentPagerAdapter {

    public static final int TAB_COUNT = 3;

    public PlayMusicPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case PageType.LIST:
                return ListTrackFragment.newInstance();
            case PageType.PLAY:
                return PlayAnimationFragment.newInstance();
            case PageType.LYRICS:
                return LyricsFragment.newInstance();
        }
        return null;
    }

    @Override
    public int getCount() {
        return TAB_COUNT;
    }
}
