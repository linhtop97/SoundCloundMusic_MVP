package linhnb.com.soundcloundmusic_mvp.ui.playmusic;

import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import linhnb.com.soundcloundmusic_mvp.Injection;
import linhnb.com.soundcloundmusic_mvp.R;
import linhnb.com.soundcloundmusic_mvp.data.model.Track;
import linhnb.com.soundcloundmusic_mvp.source.local.PreferenceManager;
import linhnb.com.soundcloundmusic_mvp.source.remote.TrackDownloadManager;
import linhnb.com.soundcloundmusic_mvp.ui.main.MainActivity;
import linhnb.com.soundcloundmusic_mvp.ui.playmusic.listtrack.ListTrackFragment;
import linhnb.com.soundcloundmusic_mvp.ui.playmusic.playanim.PlayAnimationFragment;
import linhnb.com.soundcloundmusic_mvp.ui.playmusic.service.IPlay;
import linhnb.com.soundcloundmusic_mvp.ui.playmusic.service.MusicService;
import linhnb.com.soundcloundmusic_mvp.ui.playmusic.service.PlayMode;
import linhnb.com.soundcloundmusic_mvp.utils.StringUtil;

public class PlayMusicFragment extends Fragment implements ViewPager.OnPageChangeListener, View.OnClickListener, IPlay.Callback, PlayMusicContract.View, TrackDownloadManager.DownloadListener {

    private static final String TAG = "PlayMusicFragment";
    private static final long UPDATE_PROGRESS_INTERVAL = 1000;
    private List<Track> mTracks;
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
    private ImageButton mImageButtonBack;
    private ImageButton mImageButtonPrevious;
    private ImageButton mImageButtonPlayToggle;
    private ImageButton mImageButtonNext;
    private ImageButton mImageButtonDownload;
    private ImageButton mImageButtonFavorite;
    private ImageButton mImageButtonPlayModeToggle;
    private ImageButton mImageButtonAlarm;
    private SeekBar mSeekBarProgress;
    private PlayMusicContract.Presenter mPresenter;
    private MusicService mPlayer;

    private Handler mHandler = new Handler();

    private Runnable mProgressCallback = new Runnable() {
        @Override
        public void run() {
            if (isDetached()) return;
            if (mPlayer.isPlaying()) {
                int progress = (int) (mSeekBarProgress.getMax()
                        * ((float) mPlayer.getProgress() / (float) getCurrentSongDuration()));
                updateProgressTextWithDuration(mPlayer.getProgress());
                if (progress >= 0 && progress <= mSeekBarProgress.getMax()) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        mSeekBarProgress.setProgress(progress, true);
                    } else {
                        mSeekBarProgress.setProgress(progress);
                    }
                    mHandler.postDelayed(this, UPDATE_PROGRESS_INTERVAL);
                }
            }
        }
    };

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            MusicService.MusicBinder musicBinder = (MusicService.MusicBinder) iBinder;
            mPlayer = musicBinder.getService();
            mPlayer.registerCallback(PlayMusicFragment.this);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mPlayer = null;
            mPlayer.unregisterCallback(PlayMusicFragment.this);
        }
    };

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
        mTracks = PreferenceManager.getListTrack(mMainActivity);
        mCurrentPosition = PreferenceManager.getLastPosition(getActivity());
        mTextTrackTitle = mViewRoot.findViewById(R.id.text_track_title);
        mTextArtist = mViewRoot.findViewById(R.id.text_artist);
        mTextTotalTime = mViewRoot.findViewById(R.id.textview_total_time);
        mTextCurrentTime = mViewRoot.findViewById(R.id.textview_current_time);
        mSeekBarProgress = mViewRoot.findViewById(R.id.seekbar_song);
        mImageButtonBack = mViewRoot.findViewById(R.id.button_back);
        mImageButtonBack.setOnClickListener(this);
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
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mSeekBarProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    updateProgressTextWithProgress(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                mHandler.removeCallbacks(mProgressCallback);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                seekTo(getDuration(seekBar.getProgress()));
                if (mPlayer.isPlaying()) {
                    mHandler.removeCallbacks(mProgressCallback);
                    mHandler.post(mProgressCallback);
                }
            }
        });

        new PlayMusicPresenter(getActivity(), mTracks, this).subscribe();
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
        if (position != PageType.LIST) {
            Fragment fragment = getChildFragmentManager().findFragmentByTag("android:switcher:" + R.id.vp_music + ":" + mViewPager.getCurrentItem());
            if (fragment != null && PageType.LIST == mViewPager.getCurrentItem()) {
                PreferenceManager.setImageUrl(getActivity(), PreferenceManager.getImageUrl(mMainActivity));
                ListTrackFragment listTrackFragment = (ListTrackFragment) fragment;
                LinearLayoutManager linearLayoutManager = listTrackFragment.getLayoutManager();
                linearLayoutManager.scrollToPosition(PreferenceManager.getLastPosition(mMainActivity));
            }
        }
        if (position != PageType.PLAY) {
            mImageButtonDownload.setVisibility(View.GONE);
            mImageButtonAlarm.setVisibility(View.GONE);

        } else {
            Fragment fragment = getChildFragmentManager().findFragmentByTag("android:switcher:" + R.id.vp_music + ":" + mViewPager.getCurrentItem());
            if (fragment != null && PageType.PLAY == mViewPager.getCurrentItem()) {
                PreferenceManager.setImageUrl(getActivity(), PreferenceManager.getImageUrl(mMainActivity));
                PlayAnimationFragment playAnimationFragment = (PlayAnimationFragment) fragment;
                playAnimationFragment.setImage();
                if (!mPlayer.isPlaying()) {
                    playAnimationFragment.cancelAnimation();
                } else {
                    playAnimationFragment.startAnimation();
                }

            }
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
        switch (view.getId()) {
            case R.id.button_back:
                getActivity().getSupportFragmentManager().popBackStackImmediate();
                break;
            case R.id.button_play_toggle:
                if (mPlayer.isPlaying()) {
                    mPlayer.pause();
                    mImageButtonPlayToggle.setImageResource(R.drawable.ic_play_toogle);
                } else {
                    mPlayer.play();
                    mImageButtonPlayToggle.setImageResource(R.drawable.ic_pause);
                }
                break;
            case R.id.button_play_mode_toggle:
                if (mPlayer == null) return;
                PlayMode current = PreferenceManager.lastPlayMode(getActivity());
                PlayMode newMode = PlayMode.switchNextMode(current);
                PreferenceManager.setPlayMode(getActivity(), newMode);
                mPlayer.setPlayMode(newMode);
                updatePlayMode(newMode);
                break;
            case R.id.button_next:
                if (mPlayer == null) return;
                mPlayer.playNext();
                break;
            case R.id.button_previous:
                if (mPlayer == null) return;
                mPlayer.playPrevious();
                break;
            case R.id.button_download:
                new TrackDownloadManager(getActivity(), PlayMusicFragment.this)
                        .download(PreferenceManager.getListTrack(getActivity()).get(PreferenceManager.getLastPosition(getActivity())));
                break;
            default:
                break;
        }
    }

    private int getCurrentSongDuration() {
        Track currentTrack = mPlayer.getPlayingTrack();
        int duration = 0;
        if (currentTrack != null) {
            duration = currentTrack.getDuration();
        }
        return duration;
    }

    private void updateProgressTextWithProgress(int progress) {
        int targetDuration = getDuration(progress);
        mTextCurrentTime.setText(StringUtil.formatDuration(targetDuration));
    }

    private void updateProgressTextWithDuration(int duration) {
        mTextCurrentTime.setText(StringUtil.formatDuration(duration));
    }

    private int getDuration(int progress) {
        return (int) (getCurrentSongDuration() * ((float) progress / mSeekBarProgress.getMax()));
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mPlayer != null && mPlayer.isPlaying()) {
            mHandler.removeCallbacks(mProgressCallback);
            mHandler.post(mProgressCallback);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mPlayer != null) {
            onTrackUpdated(mPlayer.getPlayingTrack());
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        mHandler.removeCallbacks(mProgressCallback);
    }

    @Override
    public void onDestroyView() {
        mPresenter.unsubscribe();
        super.onDestroyView();
    }

    @Override
    public void updatePlayMode(PlayMode playMode) {
        if (playMode == null) {
            playMode = PlayMode.getDefault();
        }
        switch (playMode) {
            case LIST:
                mImageButtonPlayModeToggle.setImageResource(R.drawable.ic_play_mode_list);
                break;
            case LOOP:
                mImageButtonPlayModeToggle.setImageResource(R.drawable.ic_play_mode_loop);
                break;
            case SHUFFLE:
                mImageButtonPlayModeToggle.setImageResource(R.drawable.ic_play_mode_shuffle);
                break;
            case SINGLE:
                mImageButtonPlayModeToggle.setImageResource(R.drawable.ic_play_mode_single);
                break;
        }
    }

    public void updatePlayToggle(boolean play) {
        mImageButtonPlayToggle.setImageResource(play ? R.drawable.ic_pause : R.drawable.ic_play_toogle);
    }

    @Override
    public void onSwitchPrevious(@Nullable Track previous) {
        onTrackUpdated(previous);
    }

    @Override
    public void onSwitchNext(@Nullable Track next) {
        onTrackUpdated(next);
    }


    @Override
    public void onComplete(@Nullable Track next) {
        onTrackUpdated(next);
    }

    @Override
    public void onPlayStatusChanged(boolean isPlaying) {
        onTrackUpdated(mPlayer.getPlayingTrack());
        updatePlayToggle(isPlaying);
        if (isPlaying) {
            mHandler.removeCallbacks(mProgressCallback);
            mHandler.post(mProgressCallback);
        } else {
            mHandler.removeCallbacks(mProgressCallback);
        }
    }

    private void seekTo(int duration) {
        mPlayer.seekTo(duration);
    }

    @Override
    public void onServiceBound(MusicService service) {
        mPlayer = service;
        mPlayer.registerCallback(this);
    }

    @Override
    public void onServiceUnbound() {
        mPlayer.unregisterCallback(this);
        mPlayer = null;
    }

    @Override
    public void onTrackUpdated(Track track) {
        if (track == null) {
            seekTo(0);
            mImageButtonPlayToggle.setImageResource(R.drawable.ic_play_toogle);
            mSeekBarProgress.setProgress(0);
            updateProgressTextWithProgress(0);
            mHandler.removeCallbacks(mProgressCallback);
            return;
        }
        mTextTrackTitle.setText(track.getTitle());
        mTextArtist.setText(track.getUserName());
        if (track.isDownloadable()) {
            mImageButtonDownload.setImageResource(R.drawable.ic_download);
            mImageButtonDownload.setClickable(true);
        } else {
            mImageButtonDownload.setImageResource(R.drawable.ic_download_grey);
            mImageButtonDownload.setClickable(false);
        }
        // Step 3: Duration
        mTextTotalTime.setText(StringUtil.formatDuration(track.getDuration()));
        mTextCurrentTime.setText(StringUtil.formatDuration(mPlayer.getProgress()));
        // set ảnh cho cái đĩa, chạy animation
        Fragment fragment = null;

        if (this.isAdded()) {
            fragment = getChildFragmentManager().findFragmentByTag("android:switcher:" + R.id.vp_music + ":" + mViewPager.getCurrentItem());
            if (fragment != null && PageType.PLAY == mViewPager.getCurrentItem()) {
                PreferenceManager.setImageUrl(getActivity(), track.getArtworkUrl());
                PlayAnimationFragment playAnimationFragment = (PlayAnimationFragment) fragment;
                playAnimationFragment.setImage();
                playAnimationFragment.cancelAnimation();
            }
        }
        mImageButtonPlayToggle.setImageResource(R.drawable.ic_play_toogle);
        mHandler.removeCallbacks(mProgressCallback);
        if (mPlayer.isPlaying()) {
            if (fragment != null && PageType.PLAY == mViewPager.getCurrentItem()) {
                PlayAnimationFragment playAnimationFragment = (PlayAnimationFragment) fragment;
                playAnimationFragment.setImage();
                playAnimationFragment.startAnimation();
            }
            mHandler.post(mProgressCallback);
            mImageButtonPlayToggle.setImageResource(R.drawable.ic_pause);
        }
    }

    @Override
    public void setPresenter(PlayMusicContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onDownloadError(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDownloading() {
        Toast.makeText(getActivity(), getActivity().getString(R.string.msg_downloading),
                Toast.LENGTH_SHORT).show();
    }
}