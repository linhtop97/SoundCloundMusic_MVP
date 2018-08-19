package linhnb.com.soundcloundmusic_mvp.ui.playlist;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import linhnb.com.soundcloundmusic_mvp.R;
import linhnb.com.soundcloundmusic_mvp.data.model.PlayList;
import linhnb.com.soundcloundmusic_mvp.data.model.PlayListDataBase;
import linhnb.com.soundcloundmusic_mvp.data.source.PlayListsRepository;
import linhnb.com.soundcloundmusic_mvp.data.source.local.PlayListLocalDataSource;
import linhnb.com.soundcloundmusic_mvp.data.source.local.PreferenceManager;
import linhnb.com.soundcloundmusic_mvp.ui.base.adapter.OnItemClickListener;
import linhnb.com.soundcloundmusic_mvp.ui.main.MainActivity;


public class PlayListFragment extends Fragment implements PlayListContract.View, OnItemClickListener,
        PlayListAdapter.AddPlayListCallback, EditPlayListDialogFragment.Callback {

    private RecyclerView mRecyclerView;
    private PlayListAdapter mPlayListAdapter;
    private LinearLayoutManager mLayoutManager;
    private MainActivity mMainActivity;
    private ProgressDialog mDialog;
    private PlayListContract.Presenter mPresenter;
    private int mEditIndex;
    private int mDeleteIndex;

    public static PlayListFragment newInstance() {
        return new PlayListFragment();
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
        mPlayListAdapter.setAddPlayListCallback(this);
        mDialog = new ProgressDialog(getActivity());
        mDialog.setMessage(getString(R.string.loading));
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

    // MVP View
    @Override
    public void showLoading() {
        mDialog.show();
    }

    @Override
    public void hideLoading() {
        mDialog.dismiss();
    }

    @Override
    public void handleError(Throwable error) {
        Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPlayListsLoaded(List<PlayList> playLists) {
        mPlayListAdapter.setData(playLists);
    }

    @Override
    public void onPlayListAdded(PlayList playList) {
        int lastIndex = mPlayListAdapter.getData().size() - 1;
        mPlayListAdapter.getData().remove(lastIndex);
        mPlayListAdapter.getData().add(playList);
        mPlayListAdapter.notifyItemInserted(lastIndex);
        mPlayListAdapter.getData().add(null);
        mPlayListAdapter.updateFooterView();

    }

    @Override
    public void onPlayListEdited(PlayList playList) {
        mPlayListAdapter.getData().set(mEditIndex, playList);
        mPlayListAdapter.notifyItemChanged(mEditIndex);
    }

    @Override
    public void onPlayListDeleted(PlayList playList) {
        mPlayListAdapter.getData().remove(mDeleteIndex);
        mPlayListAdapter.notifyItemRemoved(mDeleteIndex);
        mPlayListAdapter.updateFooterView();
    }

    @Override
    public void onItemClick(int position) {

    }

    @Override
    public void onItemClick(List list, int position) {

    }

    @Override
    public void onAction(View actionView, final int position) {
        final PlayList playList = mPlayListAdapter.getItem(position);
        PopupMenu actionMenu = new PopupMenu(getActivity(), actionView, Gravity.END | Gravity.BOTTOM);
        actionMenu.inflate(R.menu.options_menu_item_playlist);
        if (playList.getName().equals(PlayListDataBase.FAVORITE_PLAYLIST)) {
            actionMenu.getMenu().findItem(R.id.action_rename).setVisible(false);
            actionMenu.getMenu().findItem(R.id.action_delete).setVisible(false);
        }
        actionMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.action_play_now) {
                } else if (item.getItemId() == R.id.action_rename) {
                    mEditIndex = position;
                    putPlayList();
                    EditPlayListDialogFragment.editPlayList(playList)
                            .setCallback(PlayListFragment.this)
                            .show(getFragmentManager().beginTransaction(), "EditPlayList");
                } else if (item.getItemId() == R.id.action_delete) {
                    mDeleteIndex = position;
                    showAlertDialog(playList);
                }
                return true;
            }
        });
        actionMenu.show();
    }

    @SuppressLint("CommitTransaction")
    @Override
    public void onAddPlayList() {
        putPlayList();
        EditPlayListDialogFragment.createPlayList()
                .setCallback(PlayListFragment.this)
                .show(getFragmentManager().beginTransaction(), "CreatePlayList");
    }

    // Create or Edit Play List Callbacks
    @Override
    public void onCreated(PlayList playList) {
        mPresenter.addPlayList(playList);
    }

    @Override
    public void onEdited(PlayList playList) {
        mPresenter.editPlayList(playList);
    }

    private void putPlayList() {
        String playListString = "";
        for (PlayList pl : mPlayListAdapter.getData()) {
            if (pl != null) {
                playListString = playListString.concat(" ").concat(pl.getName());
            }
        }
        PreferenceManager.setPlayLists(mMainActivity, playListString.trim());
    }

    public void showAlertDialog(final PlayList playList) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mMainActivity);
        builder.setMessage("Do you want delete?");
        builder.setCancelable(false);
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mPresenter.deletePlayList(playList);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }
}