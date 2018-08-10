package linhnb.com.soundcloundmusic_mvp.ui.local;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import linhnb.com.soundcloundmusic_mvp.Injection;
import linhnb.com.soundcloundmusic_mvp.R;
import linhnb.com.soundcloundmusic_mvp.data.model.Track;
import linhnb.com.soundcloundmusic_mvp.source.TracksRepository;
import linhnb.com.soundcloundmusic_mvp.source.local.PreferenceManager;
import linhnb.com.soundcloundmusic_mvp.source.local.TracksLocalDataSource;
import linhnb.com.soundcloundmusic_mvp.source.remote.TracksRemoteDataSource;
import linhnb.com.soundcloundmusic_mvp.ui.base.adapter.OnItemClickListener;
import linhnb.com.soundcloundmusic_mvp.ui.home.HomeFragment;
import linhnb.com.soundcloundmusic_mvp.ui.main.MainActivity;
import linhnb.com.soundcloundmusic_mvp.utils.PermissionCheck;


public class LocalMusicFragment extends Fragment implements LocalMusicContract.View, OnItemClickListener {

    private RecyclerView mRecyclerView;
    private TrackAdapter mTrackAdapter;
    private LinearLayoutManager mLayoutManager;
    private LocalMusicContract.Presenter mPresenter;
    private MainActivity mMainActivity;


    public static LocalMusicFragment newInstance() {
        return new LocalMusicFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View viewRoot = inflater.inflate(R.layout.fragment_local_music, container, false);
        init(viewRoot);
        return viewRoot;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mMainActivity = (MainActivity) context;
    }

    private void init(View view) {
        mRecyclerView = view.findViewById(R.id.recycler_view);
        mTrackAdapter = new TrackAdapter(mMainActivity, null);
        mTrackAdapter.setOnItemClickListener(this);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mTrackAdapter);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        new LocalMusicPresenter(TracksRepository.getInstance(TracksRemoteDataSource.getInstance(),
                TracksLocalDataSource.getInstance()), this);

        if (PermissionCheck.readAndWriteExternalStorage(mMainActivity)) {
            mPresenter.loadLocalTracks();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void setPresenter(LocalMusicContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showTracks(List<Track> tracks) {
        PreferenceManager.putListTrack(Injection.provideContext(), tracks);
        mTrackAdapter.setData(tracks);
    }

    @Override
    public void onItemClick(int position) {
    }

    @Override
    public void onItemClick(List list, int position) {
        if (getActivity() instanceof HomeFragment.IPlayTrack) {
            ((HomeFragment.IPlayTrack) getActivity()).playTrack(list, position);
        }
    }
}