package linhnb.com.soundcloundmusic_mvp.ui.home;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;

import linhnb.com.soundcloundmusic_mvp.R;
import linhnb.com.soundcloundmusic_mvp.data.model.Track;
import linhnb.com.soundcloundmusic_mvp.source.remote.TrackDownloadManager;
import linhnb.com.soundcloundmusic_mvp.ui.base.adapter.IAdapterView;
import linhnb.com.soundcloundmusic_mvp.ui.base.adapter.ListAdapter;
import linhnb.com.soundcloundmusic_mvp.ui.base.adapter.OnLoadMoreListener;
import linhnb.com.soundcloundmusic_mvp.utils.StringUtil;

public class TrackAdapter extends ListAdapter<Track> implements TrackDownloadManager.DownloadListener {

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private OnLoadMoreListener onLoadMoreListener;
    private boolean isLoading;
    private int visibleThreshold = 5;
    private int mLastItemClickPosition = RecyclerView.NO_POSITION;


    public TrackAdapter(Context context, RecyclerView recyclerView, List<Track> data) {
        super(context, data);

        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int totalItemCount = linearLayoutManager.getItemCount();
                int lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    if (onLoadMoreListener != null) {
                        onLoadMoreListener.onLoadMore();
                    }
                    isLoading = true;
                }
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        return mData.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_track, parent, false);
            return new TrackViewHolder(view);
        } else if (viewType == VIEW_TYPE_LOADING) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false);
            return new LoadingViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof TrackViewHolder) {
            TrackViewHolder trackViewHolder = (TrackViewHolder) holder;
            trackViewHolder.bind(mData.get(position));
        } else if (holder instanceof LoadingViewHolder) {
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            loadingViewHolder.progressBar.setIndeterminate(true);
        }
    }

    public void setOnLoadMoreListener(OnLoadMoreListener mOnLoadMoreListener) {
        this.onLoadMoreListener = mOnLoadMoreListener;
    }

    public int getLastItemClickPosition() {
        return mLastItemClickPosition;
    }

    public void setLoaded() {
        isLoading = false;
    }

    @Override
    public void onDownloadError(String msg) {
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDownloading() {
        Toast.makeText(mContext, mContext.getString(R.string.msg_downloading),
                Toast.LENGTH_SHORT).show();
    }

    private class LoadingViewHolder extends RecyclerView.ViewHolder {

        private ProgressBar progressBar;

        private LoadingViewHolder(View view) {
            super(view);
            progressBar = view.findViewById(R.id.progressBar);
        }
    }

    // "Normal item" ViewHolder
    private class TrackViewHolder extends RecyclerView.ViewHolder implements IAdapterView<Track> {
        private TextView mTextTitle;
        private TextView mTextArtist;
        private ImageView mImageTrack;
        private TextView mTextDuration;
        private TextView mTextLikeCount;
        private TextView mTextPlaybackCount;
        private ImageButton mImageButtonOption;
        private Track mTrack;

        public TrackViewHolder(View itemView) {
            super(itemView);
            mImageTrack = itemView.findViewById(R.id.image_song);
            mTextTitle = itemView.findViewById(R.id.text_title);
            mTextArtist = itemView.findViewById(R.id.text_artist);
            mTextDuration = itemView.findViewById(R.id.text_duration);
            mTextLikeCount = itemView.findViewById(R.id.text_number_favorite);
            mTextPlaybackCount = itemView.findViewById(R.id.text_number_play);
            mImageButtonOption = itemView.findViewById(R.id.img_button_options);
            setupOptionsMenu();
            mLastItemClickPosition = getAdapterPosition();
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItemClickListener.onItemClick(mData, mLastItemClickPosition);
                }
            });
        }

        @Override
        public void bind(Track item) {
            if (item == null) {
                return;
            }
            mTrack = item;
            mTextTitle.setText(mTrack.getTitle());
            mTextPlaybackCount.setText(StringUtil.formatCount(mTrack.getPlaybackCount()));
            mTextArtist.setText(item.getUserName());
            mTextLikeCount.setText(StringUtil.formatCount(mTrack.getLikesCount()));
            mTextDuration.setText(StringUtil.formatDuration(mTrack.getDuration()));
            Glide.with(mContext).load(mTrack.getArtworkUrl()).into(mImageTrack);
        }

        private void setupOptionsMenu() {
            mImageButtonOption.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final PopupMenu popupMenu = new PopupMenu(mContext, mImageButtonOption);
                    popupMenu.inflate(R.menu.options_menu_item_track);
                    if (mTrack.isDownloadable()) {
                        popupMenu.getMenu().getItem(0).setTitle(R.string.action_download);
                    } else {
                        popupMenu.getMenu().getItem(0).setTitle(R.string.track_copyrighted);
                    }

                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.action_download:
                                    if (popupMenu.getMenu().getItem(0).getTitle().equals(mContext.getString(R.string.action_download))) {
                                        new TrackDownloadManager(mContext,
                                                TrackAdapter.this)
                                                .download(mTrack);
                                    }

                                    return true;
                                default:
                                    return false;
                            }
                        }
                    });
                    popupMenu.show();
                }
            });
        }
    }
}
