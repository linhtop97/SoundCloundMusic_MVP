package linhnb.com.soundcloundmusic_mvp.ui.playmusic;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import linhnb.com.soundcloundmusic_mvp.Injection;
import linhnb.com.soundcloundmusic_mvp.R;
import linhnb.com.soundcloundmusic_mvp.ui.main.MainActivity;

public class PlayMusicFragment extends Fragment implements ViewPager.OnPageChangeListener, View.OnClickListener {

    private static final String TAG = "PlayMusicFragment";
    private int mCurrentPosition;
    private MainActivity mMainActivity;
    private ViewPager mViewPager;
    private PlayMusicPagerAdapter mPlayMusicPagerAdapter;
    private LinearLayout mSliderDots;
    private int mDotsCount;
    private ImageView[] mDotsView;
    private View mViewRoot;
    private TextView mTextTrackTitle;
    private TextView mTextArtist;
    private TextView mTextTotalTime;
    private TextView mTextCurrentTime;
    private ImageButton mImageButtonDown;
    private ImageButton mImageButtonPrevious;
    private ImageButton mImageButtonPlayToggle;
    private ImageButton mImageButtonNext;
    private ImageButton mImageButtonDownload;
    private ImageButton mImageButtonFavorite;
    private ImageButton mImageButtonPlayModeToggle;
    private ImageButton mImageButtonAlarm;
    private SeekBar mSeekBarProgress;


    public static PlayMusicFragment newInstance() {
        return new PlayMusicFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mViewRoot = inflater.inflate(R.layout.fragment_play_music, container, false);
        initView();
        return mViewRoot;
    }

    private void initView() {
        mTextTrackTitle = mViewRoot.findViewById(R.id.text_track_title);
        mTextArtist = mViewRoot.findViewById(R.id.text_artist);
        mTextTotalTime = mViewRoot.findViewById(R.id.textview_total_time);
        mTextCurrentTime = mViewRoot.findViewById(R.id.textview_current_time);
        mSeekBarProgress = mViewRoot.findViewById(R.id.seekbar_song);
        mImageButtonDown = mViewRoot.findViewById(R.id.button_down);
        mImageButtonDown.setOnClickListener(this);
        mImageButtonPrevious = mViewRoot.findViewById(R.id.button_previous);
        mImageButtonPrevious.setOnClickListener(this);
        mImageButtonPlayToggle = mViewRoot.findViewById(R.id.button_play_toggle);
        mImageButtonPlayToggle.setOnClickListener(this);
        mImageButtonNext = mViewRoot.findViewById(R.id.button_next);
        mImageButtonNext.setOnClickListener(this);
        mImageButtonDownload = mViewRoot.findViewById(R.id.button_download);
        mImageButtonDownload.setOnClickListener(this);
        mImageButtonPlayModeToggle = mViewRoot.findViewById(R.id.button_play_mode_toggle);
        mImageButtonPlayModeToggle.setOnClickListener(this);
        mImageButtonAlarm = mViewRoot.findViewById(R.id.button_alarm);
        mImageButtonAlarm.setOnClickListener(this);
        mImageButtonFavorite = mViewRoot.findViewById(R.id.button_favorite);
        mImageButtonFavorite.setOnClickListener(this);
        mViewPager = mViewRoot.findViewById(R.id.vp_music);
        mPlayMusicPagerAdapter = new PlayMusicPagerAdapter(getChildFragmentManager());
        mViewPager.setAdapter(mPlayMusicPagerAdapter);
        mViewPager.setCurrentItem(PageType.PLAY);
        setUpSliderDot();
        mViewPager.addOnPageChangeListener(this);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mMainActivity = (MainActivity) context;
    }

    private void setUpSliderDot() {
        mSliderDots = mViewRoot.findViewById(R.id.slider_dots);
        mDotsCount = mPlayMusicPagerAdapter.getCount();
        mDotsView = new ImageView[mDotsCount];
        for (int i = 0; i < mDotsCount; i++) {
            mDotsView[i] = new ImageView(mMainActivity);
            mDotsView[i].setImageDrawable(ContextCompat.getDrawable(Injection.provideContext(),
                    R.drawable.inactive_dot));
            LinearLayout.LayoutParams params =
                    new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(8, 0, 8, 0);
            mSliderDots.addView(mDotsView[i], params);
        }
        mDotsView[PageType.PLAY].setImageDrawable(ContextCompat.getDrawable(Injection.provideContext(),
                R.drawable.active_dot));
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (position != PageType.PLAY) {
            mImageButtonDownload.setVisibility(View.GONE);
            mImageButtonAlarm.setVisibility(View.GONE);

        } else {
            mImageButtonDownload.setVisibility(View.VISIBLE);
            mImageButtonAlarm.setVisibility(View.VISIBLE);
        }
        for (int i = 0; i < mDotsCount; i++) {
            mDotsView[i].setImageDrawable(ContextCompat.getDrawable(Injection.provideContext(),
                    R.drawable.inactive_dot));
        }
        mDotsView[position].setImageDrawable(ContextCompat.getDrawable(Injection.provideContext(),
                R.drawable.active_dot));
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View view) {

    }
}