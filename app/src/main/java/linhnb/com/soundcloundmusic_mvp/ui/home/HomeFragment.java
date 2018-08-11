package linhnb.com.soundcloundmusic_mvp.ui.home;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import linhnb.com.soundcloundmusic_mvp.R;
import linhnb.com.soundcloundmusic_mvp.data.model.Track;
import linhnb.com.soundcloundmusic_mvp.source.TracksRepository;
import linhnb.com.soundcloundmusic_mvp.source.local.TracksLocalDataSource;
import linhnb.com.soundcloundmusic_mvp.source.remote.TracksRemoteDataSource;
import linhnb.com.soundcloundmusic_mvp.ui.base.adapter.OnItemClickListener;
import linhnb.com.soundcloundmusic_mvp.ui.base.adapter.OnLoadMoreListener;
import linhnb.com.soundcloundmusic_mvp.utils.ArrayListUtil;
import linhnb.com.soundcloundmusic_mvp.utils.Constant;

public class HomeFragment extends Fragment implements HomeContract.View, AdapterView.OnItemSelectedListener, OnItemClickListener, OnLoadMoreListener {

    private HomePresenter mPresenter;
    private Spinner mSpinnerGenres;
    private RecyclerView mRecyclerView;
    private TrackAdapter mTrackAdapter;
    private ProgressDialog mDialog;
    private LinearLayoutManager mLayoutManager;
    private TextView mTextViewNoInternet;
    private List<Track> mTracks;
    private List<Track> mTrackList;
    private int mSize;
    private int mCurrentListItem = 0;
    private List<ArrayList<Track>> mContactMember;

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View viewRoot = inflater.inflate(R.layout.fragment_home, container, false);
        init(viewRoot);
        return viewRoot;
    }

    void init(View view) {
        mRecyclerView = view.findViewById(R.id.recycler_view);
        mTextViewNoInternet = view.findViewById(R.id.text_no_internet);
        ArrayAdapter<CharSequence> genresAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.music_genres, android.R.layout.simple_spinner_item);
        genresAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerGenres = view.findViewById(R.id.spinner_genres);
        mSpinnerGenres.setAdapter(genresAdapter);
        mSpinnerGenres.setOnItemSelectedListener(this);
        new HomePresenter(TracksRepository.getInstance(TracksRemoteDataSource.getInstance()
                , TracksLocalDataSource.getInstance()), this);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mTrackAdapter = new TrackAdapter(getActivity(), mRecyclerView, null);
        mTrackAdapter.setOnItemClickListener(this);
        mRecyclerView.setAdapter(mTrackAdapter);
        mTrackAdapter.setOnLoadMoreListener(this);
        mDialog = new ProgressDialog(getActivity());
        mDialog.setMessage(getString(R.string.loading));
    }

    private List<Track> moreData() {
        mCurrentListItem++;
        return mContactMember.get(mCurrentListItem);
    }

    @Override
    public void setPresenter(HomeContract.Presenter presenter) {
        mPresenter = (HomePresenter) presenter;
    }

    @Override
    public void showTracks(List<Track> tracks) {
        mTextViewNoInternet.setVisibility(View.GONE);
        mTracks = tracks;
        mSize = tracks.size();
        mContactMember = new ArrayListUtil<Track>().chiaContactVaoTungarrray(mTracks, 20);
        if (mTrackList != null) {
            if (mTrackList.size() != 0) {
                mTrackList.clear();
            }
        } else {
            mTrackList = new ArrayList<>();
        }
        mTrackList.addAll(mContactMember.get(0));
        mTrackAdapter.setData(mTrackList);
        mRecyclerView.setVisibility(View.VISIBLE);
        mLayoutManager.scrollToPositionWithOffset(0, 0);
    }

    @Override
    public void showLoading() {
        mDialog.show();
    }

    @Override
    public void hideLoading() {
        mDialog.dismiss();
    }

    @Override
    public void handleError(String msg) {
        mTextViewNoInternet.setVisibility(View.VISIBLE);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()) {
            case R.id.spinner_genres:
                mPresenter.loadTracks(Constant.MUSIC_GENRES[i],
                        Constant.LIMIT_DEFAULT, Constant.OFFSET_DEFAULT);
                mTextViewNoInternet.setVisibility(View.GONE);
                break;
            default:
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onItemClick(int position) {
    }

    @Override
    public void onItemClick(List list, int position) {
        if (getActivity() instanceof IPlayTrack) {
            ((IPlayTrack) getActivity()).playTrack(list, position);
        }
    }

    @Override
    public void onLoadMore() {
        if (mTrackList.size() < mSize) {
            mTrackList.add(null);
            mTrackAdapter.notifyItemInserted(mTrackList.size() - 1);
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    mTrackList.remove(mTrackList.size() - 1);
                    mTrackAdapter.notifyItemRemoved(mTrackList.size());
                    //Generating more data
                    mTrackList.addAll(moreData());
                    mTrackAdapter.notifyDataSetChanged();
                    mTrackAdapter.setLoaded();
                }
            }, 1500);
        }
    }

    public interface IPlayTrack {

        void playTrack(int position);

        void playTrack(List<Track> tracks, int position);
    }
}