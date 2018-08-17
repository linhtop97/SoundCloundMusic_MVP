package linhnb.com.soundcloundmusic_mvp.ui.playlist;

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

import linhnb.com.soundcloundmusic_mvp.R;
import linhnb.com.soundcloundmusic_mvp.data.model.PlayList;
import linhnb.com.soundcloundmusic_mvp.data.source.PlayListsRepository;
import linhnb.com.soundcloundmusic_mvp.data.source.local.PlayListLocalDataSource;
import linhnb.com.soundcloundmusic_mvp.ui.base.adapter.OnItemClickListener;
import linhnb.com.soundcloundmusic_mvp.ui.main.MainActivity;


public class PlaylistFragment extends Fragment implements PlayListContract.View, OnItemClickListener {

    private RecyclerView mRecyclerView;
    private PlayListAdapter mPlayListAdapter;
    private LinearLayoutManager mLayoutManager;
    private MainActivity mMainActivity;
    private PlayListContract.Presenter mPresenter;

    public static PlaylistFragment newInstance() {
        return new PlaylistFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View viewRoot = inflater.inflate(R.layout.fragment_playlist, container, false);
        init(viewRoot);
        return viewRoot;
    }

    private void init(View view) {
        mRecyclerView = view.findViewById(R.id.recycler_view);
        mPlayListAdapter = new PlayListAdapter(mMainActivity, null);
        mPlayListAdapter.setOnItemClickListener(this);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mPlayListAdapter);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        new PlayListPresenter(PlayListsRepository.
                getInstance(PlayListLocalDataSource.getInstance(mMainActivity)), this);
        mPresenter.loadPlayLists();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mMainActivity = (MainActivity) context;
    }

    @Override
    public void setPresenter(PlayListContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onPlayListsLoaded(List<PlayList> playLists) {
        mPlayListAdapter.setData(playLists);
    }

    @Override
    public void onPlayListCreated(PlayList playList) {

    }

    @Override
    public void onPlayListEdited(PlayList playList) {

    }

    @Override
    public void onPlayListDeleted(PlayList playList) {

    }

    @Override
    public void onItemClick(int position) {

    }

    @Override
    public void onItemClick(List list, int position) {

    }
}