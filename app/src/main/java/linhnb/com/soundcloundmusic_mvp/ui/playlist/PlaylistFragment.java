package linhnb.com.soundcloundmusic_mvp.ui.playlist;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import linhnb.com.soundcloundmusic_mvp.R;
import linhnb.com.soundcloundmusic_mvp.data.model.PlayList;
import linhnb.com.soundcloundmusic_mvp.data.source.local.PlayListLocalDataSource;
import linhnb.com.soundcloundmusic_mvp.ui.main.MainActivity;


public class PlaylistFragment extends Fragment {

    private MainActivity mMainActivity;

    public static PlaylistFragment newInstance() {
        return new PlaylistFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View viewRoot = inflater.inflate(R.layout.fragment_playlist, container, false);
        return viewRoot;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        PlayListLocalDataSource playListLocalDataSource = PlayListLocalDataSource.getInstance(mMainActivity);
        List<PlayList> playLists = playListLocalDataSource.getAllPlayList();
        for (PlayList p : playLists) {
            Log.d("list", p.getName() + " " + p.getNumberTracks());
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mMainActivity = (MainActivity) context;
    }
}