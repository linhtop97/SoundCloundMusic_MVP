package linhnb.com.soundcloundmusic_mvp.ui.playmusic;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import linhnb.com.soundcloundmusic_mvp.R;

public class PlayMusicFragment extends Fragment {

    private static final String TAG = "PlayMusicFragment";

    private int mCurrentPosition;

    private ViewPager mViewPager;

    public static PlayMusicFragment newInstance() {
        return new PlayMusicFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View viewRoot = inflater.inflate(R.layout.fragment_play_music, container, false);
        initView(viewRoot);
        return viewRoot;
    }

    private void initView(View viewRoot) {
        mViewPager = viewRoot.findViewById(R.id.vp_music);
        PlayMusicPagerAdapter musicPagerAdapter = new PlayMusicPagerAdapter(getChildFragmentManager());
        mViewPager.setAdapter(musicPagerAdapter);
        mViewPager.setCurrentItem(PageType.PLAY);
    }
}