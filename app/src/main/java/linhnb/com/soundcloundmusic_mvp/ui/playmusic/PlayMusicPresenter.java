package linhnb.com.soundcloundmusic_mvp.ui.playmusic;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import linhnb.com.soundcloundmusic_mvp.data.model.Track;
import linhnb.com.soundcloundmusic_mvp.source.local.PreferenceManager;
import linhnb.com.soundcloundmusic_mvp.ui.playmusic.service.IPlay;
import linhnb.com.soundcloundmusic_mvp.ui.playmusic.service.MusicService;
import linhnb.com.soundcloundmusic_mvp.ui.playmusic.service.PlayMode;
import linhnb.com.soundcloundmusic_mvp.utils.Constant;

import static android.content.Context.BIND_AUTO_CREATE;

public class PlayMusicPresenter implements PlayMusicContract.Presenter {

    private Context mContext;
    private PlayMusicContract.View mView;
    private List<Track> mTracks;
    private int mPosition;

    private IPlay mPlayer;
    private boolean mIsServiceBound;

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            MusicService.MusicBinder musicBinder = (MusicService.MusicBinder) iBinder;
            mPlayer = musicBinder.getService();
            mView.onServiceBound((MusicService) mPlayer);
            mView.onTrackUpdated(mPlayer.getPlayingTrack());
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mPlayer = null;
            mView.onServiceUnbound();
        }
    };

    public PlayMusicPresenter(Context context, List<Track> tracks, PlayMusicContract.View view) {
        mContext = context;
        mView = view;
        mTracks = tracks;
        mPosition = PreferenceManager.getLastPosition(mContext);
        mView.setPresenter(this);
    }

    @Override
    public void retrieveLastPlayMode() {
        PlayMode lastPlayMode = PreferenceManager.lastPlayMode(mContext);
        mView.updatePlayMode(lastPlayMode);
    }

    @Override
    public void bindMusicService() {
        Intent intent = new Intent(mContext, MusicService.class);
        intent.setAction(Constant.ACTION_MAIN);
        mContext.bindService(intent, mConnection, BIND_AUTO_CREATE);
        mContext.startService(intent);
        mIsServiceBound = true;
    }

    @Override
    public void unbindMusicService() {
        if (mIsServiceBound) {
            // Detach our existing connection.
            mContext.unbindService(mConnection);
            mIsServiceBound = false;
        }
    }

    @Override
    public void subscribe() {
        bindMusicService();
        retrieveLastPlayMode();
        if (mPlayer != null && mPlayer.isPlaying()) {
            mView.onTrackUpdated(mPlayer.getPlayingTrack());
        } else {
            //play another resource
        }
    }

    @Override
    public void unsubscribe() {
        unbindMusicService();
        // Release context reference
        mContext = null;
        mView = null;
    }
}
