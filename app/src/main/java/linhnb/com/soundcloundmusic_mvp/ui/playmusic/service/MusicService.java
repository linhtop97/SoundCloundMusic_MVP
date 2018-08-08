package linhnb.com.soundcloundmusic_mvp.ui.playmusic.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import linhnb.com.soundcloundmusic_mvp.R;
import linhnb.com.soundcloundmusic_mvp.data.model.Track;
import linhnb.com.soundcloundmusic_mvp.source.local.PreferenceManager;
import linhnb.com.soundcloundmusic_mvp.source.remote.FetchBitmapFromUrl;
import linhnb.com.soundcloundmusic_mvp.ui.main.MainActivity;
import linhnb.com.soundcloundmusic_mvp.utils.Constant;

public class MusicService extends Service implements IPlay, IPlay.Callback,
        MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener {
    //khởi tạo vị trí bài hát mặc định
    public static final int NO_POSITION = -1;

    private static final int NOTIFICATION_ID = 1;

    private RemoteViews mContentViewBig, mContentViewSmall;
    private Bitmap mBitmap;
    private MediaPlayer mMediaPlayer;
    private List<Track> mTracks;
    private PlayMode mPlayMode;
    private List<Callback> mCallbacks = new ArrayList<>(2);
    private boolean isPaused;
    private int mPlayingindex = -1;

    @Override
    public void onCreate() {
        super.onCreate();
        mPlayMode = PlayMode.LOOP;
        mMediaPlayer = new MediaPlayer();
        mTracks = new ArrayList<>();
        mMediaPlayer.setOnCompletionListener(this);
        registerCallback(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            String action = intent.getAction();
            if (action == null) {
                return START_STICKY;
            }
            switch (action) {
                case Constant.ACTION_MAIN:
                    ArrayList<Track> tracks = intent.getParcelableArrayListExtra(Constant.EXTRA_LIST_TRACK);
                    int position = intent.getIntExtra(Constant.EXTRA_POSITION, 0);
                    if (mMediaPlayer.isPlaying()
                            && checkTrackPlay(mTracks.get(position))) {
                        break;
                    } else {
                        play(tracks, position);
                        showNotification();
                    }
                    break;
                case Constant.ACTION_PLAY_TOGGLE:
                    if (isPlaying()) {
                        pause();
                    } else {
                        play();
                    }
                    break;
                case Constant.ACTION_PLAY_NEXT:
                    playNext();
                    break;
                case Constant.ACTION_PLAY_PREVIOUS:
                    playPrevious();
                    break;
                case Constant.ACTION_PLAY_NOW:
                    position = intent.getIntExtra(Constant.EXTRA_POSITION, 0);
                    if (mMediaPlayer.isPlaying()
                            && checkTrackPlay(mTracks.get(position))) {
                        break;
                    } else {
                        mPlayingindex = position;
                        play(position);
                    }
                    break;
                case Constant.ACTION_STOP_SERVICE:
                    if (isPlaying()) {
                        pause();
                    }
                    stopForeground(true);
                    stopSelf();
                default:
                    break;
            }
            unregisterCallback(this);
        }
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new MusicBinder();
    }

    @Override
    public boolean stopService(Intent name) {
        stopForeground(true);
        unregisterCallback(this);
        return super.stopService(name);
    }

    @Override
    public void onDestroy() {
        releasePlayer();
        stopSelf();
        super.onDestroy();
    }

    @Override
    public void setPlayList(List<Track> tracks) {
        if (tracks == null) {
            tracks = new ArrayList<>();
        }
        mTracks = tracks;
    }

    //chuẩn bị phát nhạc, nếu danh sách rỗng, ko làm gì, trả về flase,
    //ngược lại nếu chưa phát bài nào thì gán bài hát đang chạy từ vị trí 0
    public boolean prepare() {
        if (mTracks.isEmpty()) return false;

        if (mPlayingindex == NO_POSITION) {
            mPlayingindex = 0;
        }
        return true;
    }

    public Track getCurrentTrack() {
        if (mPlayingindex != NO_POSITION) {
            return mTracks.get(mPlayingindex);
        }
        return null;
    }

    @Override
    public boolean play() {
        PreferenceManager.setLastPosition(this, mPlayingindex);
        PreferenceManager.setImageUrl(this, mTracks.get(mPlayingindex).getArtworkUrl());
        if (isPaused) {
            mMediaPlayer.start();
            notifyPlayStatusChanged(true);
            showNotification();
            return true;
        }

        if (prepare()) {
            Track track = getCurrentTrack();
            try {
                mMediaPlayer.reset();
                mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mMediaPlayer.setDataSource(track.getUri());
                mMediaPlayer.setOnPreparedListener(this);
                mMediaPlayer.setOnCompletionListener(this);
                mMediaPlayer.prepareAsync();
                showNotification();
            } catch (IOException e) {
                showNotification();
                if (mPlayingindex < mTracks.size()) next();
                return false;
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean play(List<Track> tracks) {
        if (tracks == null) return false;
        isPaused = false;
        setPlayList(tracks);
        mPlayingindex = 0;
        return play();
    }

    @Override
    public boolean play(List<Track> tracks, int startIndex) {
        if (tracks == null || startIndex < 0 || startIndex >= tracks.size()) return false;
        isPaused = false;
        setPlayList(tracks);
        setPlayingindex(startIndex);
        return play();
    }

    @Override
    public boolean play(Track track) {
        if (track == null) return false;
        isPaused = false;
//        mTracks.clear();
//        mTracks.add(track);
        return play();
    }

    @Override
    public void play(int position) {
        mPlayingindex = position;
        Log.d("size track", mTracks.size() + "");
        play(mTracks.get(mPlayingindex));
        play();
        notifyPlayPrevious(mTracks.get(mPlayingindex));
    }

    @Override
    public void playPrevious() {
        isPaused = false;
        if (hasPrevious()) {
            if (mPlayingindex - 1 < 0) {
                mPlayingindex = mTracks.size() - 1;
            } else {
                mPlayingindex--;
            }
            play();
            notifyPlayPrevious(mTracks.get(mPlayingindex));
        }
    }

    @Override
    public void playNext() {
        isPaused = false;
        boolean hasNext = hasNext(true);
        if (hasNext) {
            Track next = next();
            play();
            notifyPlayNext(next);
        }
    }

    @Override
    public void pause() {
        mMediaPlayer.pause();
        isPaused = true;
        notifyPlayStatusChanged(false);
        showNotification();
    }

    @Override
    public boolean isPlaying() {
        return mMediaPlayer.isPlaying();
    }

    @Override
    public int getProgress() {
        return mMediaPlayer.getCurrentPosition();
    }

    @Override
    public Track getPlayingTrack() {
        return getCurrentTrack();
    }

    @Override
    public void seekTo(int progress) {
        if (mTracks.isEmpty()) return;

        Track currentTrack = getCurrentTrack();
        if (currentTrack != null) {
            if (currentTrack.getDuration() <= progress) {
                onCompletion(mMediaPlayer);
            } else {
                mMediaPlayer.seekTo(progress);
            }
        }
    }

    @Override
    public void registerCallback(Callback callback) {
        mCallbacks.add(callback);
    }

    @Override
    public void unregisterCallback(Callback callback) {
        mCallbacks.remove(callback);
    }

    @Override
    public void removeCallbacks() {
        mCallbacks.clear();
    }

    @Override
    public void releasePlayer() {
        mTracks = null;
        mMediaPlayer.reset();
        mMediaPlayer.release();
        mMediaPlayer = null;
        super.onDestroy();
    }

    @Override
    public void onSwitchPrevious(@Nullable Track previous) {
        showNotification();
    }

    @Override
    public void onSwitchNext(@Nullable Track next) {
        showNotification();
    }


    @Override
    public void onComplete(@Nullable Track next) {
        showNotification();
    }

    @Override
    public void onPlayStatusChanged(boolean isPlaying) {
        showNotification();

    }

//    private void showNotification() {
//        Track track = getPlayingTrack();
//        Intent notIntent = new Intent(this, MainActivity.class);
//        notIntent.setAction(ACTION_MAIN);
//        PreferenceManager.setIsPlaying(this, isPlaying());
//        notIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//        PendingIntent pendInt = PendingIntent.getActivity(getApplicationContext(),
//                0,
//                notIntent, 0);
//
//        Intent previousIntent = new Intent(getApplicationContext(), MusicService.class);
//        previousIntent.setAction(ACTION_PLAY_PREVIOUS);
//        PendingIntent ppreviousIntent = PendingIntent.getService(getApplicationContext(),
//                0,
//                previousIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//        Intent playIntent = new Intent(getApplicationContext(), MusicService.class);
//        playIntent.setAction(ACTION_PLAY_TOGGLE);
//        PendingIntent pplayIntent = PendingIntent.getService(getApplicationContext(),
//                0,
//                playIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//        Intent nextIntent = new Intent(getApplicationContext(), MusicService.class);
//        nextIntent.setAction(ACTION_PLAY_NEXT);
//        PendingIntent pnextIntent = PendingIntent.getService(getApplicationContext(),
//                0,
//                nextIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//        Intent dismissIntent = new Intent(getApplicationContext(), MusicService.class);
//        dismissIntent.setAction(ACTION_STOP_SERVICE);
//        PendingIntent pdismisIntent = PendingIntent.getService(getApplicationContext(),
//                0,
//                dismissIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//        int iconPlayPause;
//        if (isPlaying()) {
//            iconPlayPause = R.drawable.ic_pause;
//        } else {
//            iconPlayPause = R.drawable.ic_play_toogle;
//        }
//        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.remote_view_music_player_small);
//        remoteViews.setImageViewResource(R.id.img_button_close, R.drawable.ic_remote_view_close);
//        remoteViews.setImageViewResource(R.id.img_button_play_toggle, iconPlayPause);
//        remoteViews.setImageViewResource(R.id.img_button_play_previous, R.drawable.ic_play_previous);
//        remoteViews.setImageViewResource(R.id.img_button_play_next, R.drawable.ic_play_next);
//        //choox nay
//        new FetchBitmapFromUrl(this).execute(track.getArtworkUrl());
//        if (mBitmap == null) {
//            remoteViews.setImageViewResource(R.id.image_view_album, R.mipmap.ic_launcher);
//        } else {
//            remoteViews.setImageViewBitmap(R.id.image_view_album, mBitmap);
//        }
//        remoteViews.setTextViewText(R.id.text_view_name, track.getTitle());
//        remoteViews.setTextViewText(R.id.text_view_artist, track.getUserName());
//
//
//        remoteViews.setOnClickPendingIntent(R.id.img_button_close, pdismisIntent);
//        remoteViews.setOnClickPendingIntent(R.id.img_button_play_previous, ppreviousIntent);
//        remoteViews.setOnClickPendingIntent(R.id.img_button_play_next, pnextIntent);
//        remoteViews.setOnClickPendingIntent(R.id.img_button_play_toggle, pplayIntent);
//
//
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(
//                getApplicationContext(), "abc");
//        builder.setSmallIcon(R.mipmap.ic_launcher_round)
//                .setContentTitle(track.getTitle())
//                .setContentText(track.getUserName())
//                .setContentIntent(pendInt)
//                .setContent(remoteViews);
//        Notification not = builder.build();
//        startForeground(NOTIFICATION_ID, not);
//    }
//
//    private void setUpRemoteView(RemoteViews remoteView) {
//        remoteView.setImageViewResource(R.id.image_view_close, R.drawable.ic_remote_view_close);
//        remoteView.setImageViewResource(R.id.image_view_play_last, R.drawable.ic_remote_view_play_last);
//        remoteView.setImageViewResource(R.id.image_view_play_next, R.drawable.ic_remote_view_play_next);
//
//        remoteView.setOnClickPendingIntent(R.id.button_close, getPendingIntent(ACTION_STOP_SERVICE));
//        remoteView.setOnClickPendingIntent(R.id.button_play_last, getPendingIntent(ACTION_PLAY_LAST));
//        remoteView.setOnClickPendingIntent(R.id.button_play_next, getPendingIntent(ACTION_PLAY_NEXT));
//        remoteView.setOnClickPendingIntent(R.id.button_play_toggle, getPendingIntent(ACTION_PLAY_TOGGLE));
//    }
//
//    private void updateRemoteViews(RemoteViews remoteView) {
//        Track track = getPlayingTrack();
//        if (track != null) {
//            remoteView.setTextViewText(R.id.text_view_name, track.getTitle());
//            remoteView.setTextViewText(R.id.text_view_artist, track.getUserName());
//        }
//        remoteView.setImageViewResource(R.id.image_view_play_toggle, isPlaying()
//                ? R.drawable.ic_pause : R.drawable.ic_play_toogle);
//        Bitmap album = AlbumUtils.parseAlbum(getPlayingSong());
//        if (album == null) {
//            remoteView.setImageViewResource(R.id.image_view_album, R.mipmap.ic_launcher);
//        } else {
//            remoteView.setImageViewBitmap(R.id.image_view_album, album);
//
//        }
//    }
//    private RemoteViews getSmallContentView() {
//        if (mContentViewSmall == null) {
//            mContentViewSmall = new RemoteViews(getPackageName(), R.layout.remote_view_music_player_small);
//        }
//        return mContentViewSmall;
//    }
//
//    private RemoteViews getBigContentView() {
//        if (mContentViewBig == null) {
//            mContentViewBig = new RemoteViews(getPackageName(), R.layout.remote_view_music_player);
//        }
//        return mContentViewBig;
//    }
    // Notification

    /**
     * Show a notification while this service is running.
     */
    private void showNotification() {
        Intent notIntent = new Intent(this, MainActivity.class);
        notIntent.setAction(Constant.ACTION_MAIN);
        PreferenceManager.setIsPlaying(this, isPlaying());
        notIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        // The PendingIntent to launch our activity if the user selects this notification
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notIntent, 0);

        // Set the info for the views that show in the notification panel.
        Notification notification = new NotificationCompat.Builder(this, "abc")
                .setSmallIcon(R.drawable.ic_launcher_foreground)  // the status icon
                .setWhen(System.currentTimeMillis())  // the time stamp
                .setContentIntent(contentIntent)  // The intent to send when the entry is clicked
                .setCustomContentView(getSmallContentView())
                .setCustomBigContentView(getBigContentView())
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setOngoing(true)
                .build();
        // Send the notification.
        startForeground(NOTIFICATION_ID, notification);
    }

    private RemoteViews getSmallContentView() {
        if (mContentViewSmall == null) {
            mContentViewSmall = new RemoteViews(getPackageName(), R.layout.remote_view_music_player_small);
            setUpRemoteView(mContentViewSmall);
        }
        updateRemoteViews(mContentViewSmall);
        return mContentViewSmall;
    }

    private RemoteViews getBigContentView() {
        if (mContentViewBig == null) {
            mContentViewBig = new RemoteViews(getPackageName(), R.layout.remote_view_music_player);
            setUpRemoteView(mContentViewBig);
        }
        updateRemoteViews(mContentViewBig);
        return mContentViewBig;
    }

    private void setUpRemoteView(RemoteViews remoteView) {
        remoteView.setImageViewResource(R.id.image_view_close, R.drawable.ic_remote_view_close);
        remoteView.setImageViewResource(R.id.image_view_play_previous, R.drawable.ic_play_previous);
        remoteView.setImageViewResource(R.id.image_view_play_next, R.drawable.ic_play_next);

        Intent previousIntent = new Intent(getApplicationContext(), MusicService.class);
        previousIntent.setAction(Constant.ACTION_PLAY_PREVIOUS);
        PendingIntent ppreviousIntent = PendingIntent.getService(getApplicationContext(),
                0,
                previousIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent playIntent = new Intent(getApplicationContext(), MusicService.class);
        playIntent.setAction(Constant.ACTION_PLAY_TOGGLE);
        PendingIntent pplayIntent = PendingIntent.getService(getApplicationContext(),
                0,
                playIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent nextIntent = new Intent(getApplicationContext(), MusicService.class);
        nextIntent.setAction(Constant.ACTION_PLAY_NEXT);
        PendingIntent pnextIntent = PendingIntent.getService(getApplicationContext(),
                0,
                nextIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent dismissIntent = new Intent(getApplicationContext(), MusicService.class);
        dismissIntent.setAction(Constant.ACTION_STOP_SERVICE);
        PendingIntent pdismisIntent = PendingIntent.getService(getApplicationContext(),
                0,
                dismissIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteView.setOnClickPendingIntent(R.id.button_close, pdismisIntent);
        remoteView.setOnClickPendingIntent(R.id.button_play_previous, ppreviousIntent);
        remoteView.setOnClickPendingIntent(R.id.button_play_next, pnextIntent);
        remoteView.setOnClickPendingIntent(R.id.button_play_toggle, pplayIntent);
    }

    private void updateRemoteViews(RemoteViews remoteView) {
        Track currentTrack = getPlayingTrack();
        if (currentTrack != null) {
            remoteView.setTextViewText(R.id.text_view_name, currentTrack.getTitle());
            remoteView.setTextViewText(R.id.text_view_artist, currentTrack.getUserName());
        }
        remoteView.setImageViewResource(R.id.image_view_play_toggle, isPlaying()
                ? R.drawable.ic_pause : R.drawable.ic_play_toogle);
        new FetchBitmapFromUrl(this).execute(currentTrack.getArtworkUrl());
        if (mBitmap == null) {
            remoteView.setImageViewResource(R.id.image_view_album, R.mipmap.ic_launcher);
        } else {
            remoteView.setImageViewBitmap(R.id.image_view_album, mBitmap);
        }
    }

    // PendingIntent

    private PendingIntent getPendingIntent(String action) {
        return PendingIntent.getService(this, 0, new Intent(action), 0);
    }

    public void setBitmap(Bitmap bm) {
        mBitmap = bm;
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        Track next = null;
        //ở đây giới hạn duy nhất kiểu hcơi nhạc là 1 list, chơi nhạc nên dừng khi danh sách hết
        if (getPlayMode() == PlayMode.LIST && getPlayingIndex() == mTracks.size() - 1) {
            // In the end of the list
            // Do nothing, just deliver the callback
        } else if (getPlayMode() == PlayMode.SINGLE) {
            next = getCurrentTrack();
            play(next);
        } else {
            boolean hasNext = hasNext(true);
            if (hasNext) {
                next = next();
                play();
            }
        }
        notifyComplete(next);
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        mMediaPlayer.start();
        notifyPlayStatusChanged(true);
        showNotification();
    }

    public boolean hasPrevious() {
        return mTracks != null && mTracks.size() != 0;
    }

    public boolean hasNext(boolean fromComplete) {
        if (mTracks.isEmpty()) return false;
        if (fromComplete) {
            if (mPlayMode == PlayMode.LIST && mPlayingindex + 1 >= mTracks.size()) return false;
        }
        return true;
    }

    public Track next() {
        switch (mPlayMode) {
            case LOOP:
            case LIST:
            case SINGLE:
                int newIndex = mPlayingindex + 1;
                if (newIndex >= mTracks.size()) {
                    newIndex = 0;
                }
                mPlayingindex = newIndex;
                break;
            case SHUFFLE:
                mPlayingindex = randomPlayIndex();
                break;
        }
        return mTracks.get(mPlayingindex);
    }

    private int randomPlayIndex() {
        int randomIndex = new Random().nextInt(mTracks.size());
        if (mTracks.size() > 1 && randomIndex == mPlayingindex) {
            randomPlayIndex();
        }
        return randomIndex;
    }

    public PlayMode getPlayMode() {
        return mPlayMode;
    }

    @Override
    public void setPlayMode(PlayMode playMode) {
        mPlayMode = playMode;
    }

    public int getPlayingIndex() {
        return mPlayingindex;
    }

    public void setPlayingindex(int playingindex) {
        this.mPlayingindex = playingindex;
    }

    private void notifyPlayStatusChanged(boolean isPlaying) {
        for (Callback callback : mCallbacks) {
            callback.onPlayStatusChanged(isPlaying);
        }
    }

    private void notifyPlayPrevious(Track track) {
        for (Callback callback : mCallbacks) {
            callback.onSwitchPrevious(track);
        }
    }

    private void notifyPlayNext(Track track) {
        for (Callback callback : mCallbacks) {
            callback.onSwitchNext(track);
        }
    }

    private void notifyComplete(Track track) {
        for (Callback callback : mCallbacks) {
            callback.onComplete(track);
        }
    }

    public boolean checkTrackPlay(Track track) {
        Track trackCurent = mTracks.get(mPlayingindex);
        return track.getUri().equals(trackCurent.getUri());
    }

    public class MusicBinder extends Binder {
        public MusicService getService() {
            return MusicService.this;
        }
    }
}
