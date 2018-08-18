package linhnb.com.soundcloundmusic_mvp.ui.playlist;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import linhnb.com.soundcloundmusic_mvp.R;
import linhnb.com.soundcloundmusic_mvp.data.model.PlayList;
import linhnb.com.soundcloundmusic_mvp.data.model.PlayListDataBase;
import linhnb.com.soundcloundmusic_mvp.ui.base.adapter.IAdapterView;
import linhnb.com.soundcloundmusic_mvp.ui.base.adapter.ListAdapter;

public class PlayListAdapter extends ListAdapter<PlayList> {

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_ADD = 1;
    private AddPlayListCallback mAddPlayListCallback;

    public PlayListAdapter(Context context, List<PlayList> data) {
        super(context, data);
    }

    @Override
    public int getItemViewType(int position) {
        return mData.get(position) == null ? VIEW_TYPE_ADD : VIEW_TYPE_ITEM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_playlist, parent, false);
            return new PlayListViewHolder(view);
        } else if (viewType == VIEW_TYPE_ADD) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_play_list_footer, parent, false);
            return new AddViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof PlayListViewHolder) {
            PlayListViewHolder trackViewHolder = (PlayListViewHolder) holder;
            trackViewHolder.bind(mData.get(position));
        } else if (holder instanceof AddViewHolder) {
            AddViewHolder addViewHolder = (AddViewHolder) holder;
        }
    }

    public void setAddPlayListCallback(AddPlayListCallback callback) {
        mAddPlayListCallback = callback;
    }

    public interface AddPlayListCallback {

        void onAction(View actionView, int position);

        void onAddPlayList();
    }

    private class AddViewHolder extends RecyclerView.ViewHolder {

        private TextView mTextNumberPlayLists;

        @SuppressLint("StringFormatInvalid")
        private AddViewHolder(View view) {
            super(view);
            mTextNumberPlayLists = view.findViewById(R.id.text_number_playlist);
            if (mTextNumberPlayLists == null) {
                return;
            }
            int itemCount = getItemCount() - 1;
            if (itemCount > 1) {
                mTextNumberPlayLists.setVisibility(View.VISIBLE);
                mTextNumberPlayLists.setText(mContext.getString(R.string.msg_1_playlists_in_total, itemCount));
            } else {
                mTextNumberPlayLists.setVisibility(View.GONE);
            }
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mAddPlayListCallback != null) {
                        mAddPlayListCallback.onAddPlayList();
                    }
                }
            });
        }
    }

    // "Normal item" ViewHolder
    private class PlayListViewHolder extends RecyclerView.ViewHolder implements IAdapterView<PlayList> {
        private TextView mTextPlayListName;
        private TextView mTextNumberTracks;
        private TextView mTextPlayList;
        private ImageView mImageFavorite;
        private ImageButton mImageButtonOption;
        private String mPlaylistName;

        public PlayListViewHolder(View itemView) {
            super(itemView);
            mTextPlayListName = itemView.findViewById(R.id.text_playlist_name);
            mTextNumberTracks = itemView.findViewById(R.id.text_number_tracks);
            mTextPlayList = itemView.findViewById(R.id.image_view_name);
            mTextNumberTracks = itemView.findViewById(R.id.text_number_tracks);
            mImageFavorite = itemView.findViewById(R.id.image_view_favorite);
            mImageButtonOption = itemView.findViewById(R.id.img_button_options);
            mImageButtonOption.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (mAddPlayListCallback != null) {
                        mAddPlayListCallback.onAction(mImageButtonOption, position);
                    }
                }
            });
        }

        @Override
        public void bind(PlayList item) {
            if (item == null) {
                return;
            }
            mPlaylistName = item.getName();
            mTextPlayListName.setText(mPlaylistName);
            mTextNumberTracks.setText(String.valueOf(item.getNumberTracks()).concat(" ")
                    .concat(mContext.getResources().getString(R.string.number_tracks)));
            if (mPlaylistName.equals(PlayListDataBase.FAVORITE_PLAYLIST)) {
                mImageFavorite.setVisibility(View.VISIBLE);
                mTextPlayList.setVisibility(View.GONE);
            } else {
                mTextPlayList.setVisibility(View.VISIBLE);
                mTextPlayList.setText(String.valueOf(item.getName().toLowerCase().charAt(0)));
            }
        }
    }
}
