package linhnb.com.soundcloundmusic_mvp.ui.maincontent;

import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import linhnb.com.soundcloundmusic_mvp.R;
import linhnb.com.soundcloundmusic_mvp.source.local.PreferenceManager;
import linhnb.com.soundcloundmusic_mvp.ui.main.MainActivity;
import linhnb.com.soundcloundmusic_mvp.ui.playmusic.PlayMusicFragment;
import linhnb.com.soundcloundmusic_mvp.utils.Constant;
import linhnb.com.soundcloundmusic_mvp.utils.FragmentManagerUtils;

public class MainFragment extends Fragment implements TabLayout.OnTabSelectedListener {

    private static final String TAG = "MainFragment";
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private MainActivity mMainActivity;
    private int mCurrentTab;

    public static MainFragment newInstance(Boolean isOpenPlayFragment) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putBoolean(Constant.ARGUMENT_IS_OPEN, isOpenPlayFragment);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View viewRoot = inflater.inflate(R.layout.fragment_main, container, false);
        initView(viewRoot);
        setupTabIcons();
        return viewRoot;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onresum");
        String action = mMainActivity.getIntent().getAction();
        Log.d(TAG, action);
        if (action != null && action.equals(Constant.ACTION_MAIN)) {
            FragmentManager manager = mMainActivity.getSupportFragmentManager();
            PlayMusicFragment musicFragment = PlayMusicFragment.newInstance();
            FragmentManagerUtils.addFragment(manager, musicFragment, R.id.main_content,
                    musicFragment.getClass().getName(), true);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    private void initView(View view) {
        mViewPager = view.findViewById(R.id.view_pager);
        mTabLayout = view.findViewById(R.id.tablayout);
        MainPagerAdapter adapter = new MainPagerAdapter(getChildFragmentManager());
        mViewPager.setAdapter(adapter);
        mViewPager.setCurrentItem(mCurrentTab);
        mTabLayout.addOnTabSelectedListener(this);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    private void setupTabIcons() {
        switch (mCurrentTab) {
            case TabType.HOME:
                mTabLayout.getTabAt(TabType.HOME).setIcon(R.drawable.ic_home_selected);
                mTabLayout.getTabAt(TabType.LOCAL_MUSIC).setIcon(R.drawable.ic_local_music_unselected);
                mTabLayout.getTabAt(TabType.PLAYLIST).setIcon(R.drawable.ic_playlist_unselected);
                break;
            case TabType.LOCAL_MUSIC:
                mTabLayout.getTabAt(TabType.HOME).setIcon(R.drawable.ic_home_unselected);
                mTabLayout.getTabAt(TabType.LOCAL_MUSIC).setIcon(R.drawable.ic_local_music_selected);
                mTabLayout.getTabAt(TabType.PLAYLIST).setIcon(R.drawable.ic_playlist_unselected);
                break;
            case TabType.PLAYLIST:
                mTabLayout.getTabAt(TabType.HOME).setIcon(R.drawable.ic_home_unselected);
                mTabLayout.getTabAt(TabType.LOCAL_MUSIC).setIcon(R.drawable.ic_local_music_unselected);
                mTabLayout.getTabAt(TabType.PLAYLIST).setIcon(R.drawable.ic_playlist_selected);
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mMainActivity = (MainActivity) context;
        mCurrentTab = PreferenceManager.getTab(mMainActivity);
        Boolean isOpen = getArguments().getBoolean(Constant.ARGUMENT_IS_OPEN);
        if (isOpen) {
            FragmentManager manager = mMainActivity.getSupportFragmentManager();
            PlayMusicFragment musicFragment = PlayMusicFragment.newInstance();
            FragmentManagerUtils.addFragment(manager, musicFragment, R.id.main_content,
                    musicFragment.getClass().getName(), true);
        }
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        switch (tab.getPosition()) {
            case TabType.HOME:
                mTabLayout.getTabAt(TabType.HOME).setIcon(R.drawable.ic_home_selected);
                mViewPager.setCurrentItem(0);
                break;
            case TabType.LOCAL_MUSIC:
                mTabLayout.getTabAt(TabType.LOCAL_MUSIC).setIcon(R.drawable.ic_local_music_selected);
                mViewPager.setCurrentItem(1);
                break;
            case TabType.PLAYLIST:
                mTabLayout.getTabAt(TabType.PLAYLIST).setIcon(R.drawable.ic_playlist_selected);
                mViewPager.setCurrentItem(2);
                break;
        }

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
        int tabIconColor = ContextCompat.getColor(mMainActivity, R.color.sc_theme_colorTabIconUnselected);
        tab.getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
    }
}
