package linhnb.com.soundcloundmusic_mvp.ui.playmusic.listtrack;

import android.content.Intent;
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

import linhnb.com.soundcloundmusic_mvp.R;
import linhnb.com.soundcloundmusic_mvp.data.model.Track;
import linhnb.com.soundcloundmusic_mvp.data.source.local.PreferenceManager;
import linhnb.com.soundcloundmusic_mvp.ui.base.adapter.OnItemClickListener;
import linhnb.com.soundcloundmusic_mvp.ui.playmusic.service.MusicService;
import linhnb.com.soundcloundmusic_mvp.utils.Constant;

public class ListTrackFragment extends Fragment implements OnItemClickListener {

    private TrackAdapterMini mTrackAdapter;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;

    public static ListTrackFragment newInstance() {
        ListTrackFragment fragment = new ListTrackFragment();
        return fragment;
    }

    public LinearLayoutManager getLayoutManager() {
        return mLayoutManager;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View viewRoot = inflater.inflate(R.layout.fragment_list_track, container, false);
        mRecyclerView = viewRoot.findViewById(R.id.rv_list_track);
        init();
        return viewRoot;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void init() {
        List<Track> tracks = PreferenceManager.getListTrack(getActivity());
        mTrackAdapter = new TrackAdapterMini(getActivity(), tracks);
        mTrackAdapter.setOnItemClickListener(this);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mTrackAdapter);
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(getActivity(), MusicService.class);
        intent.setAction(Constant.ACTION_PLAY_NOW);
        intent.putExtra(Constant.EXTRA_POSITION, position);
        getActivity().startService(intent);
    }

    @Override
    public void onItemClick(List list, int position) {

    }
}