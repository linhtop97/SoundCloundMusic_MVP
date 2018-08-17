package linhnb.com.soundcloundmusic_mvp.ui.playlist;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import java.util.List;

import linhnb.com.soundcloundmusic_mvp.R;
import linhnb.com.soundcloundmusic_mvp.data.model.PlayList;
import linhnb.com.soundcloundmusic_mvp.ui.base.adapter.IAdapterView;
import linhnb.com.soundcloundmusic_mvp.ui.base.adapter.ListAdapter;

public class PlayListAdapter extends ListAdapter<PlayList> {

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_ADD = 1;

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

    private class AddViewHolder extends RecyclerView.ViewHolder {

        private TextView mTextNumberPlayLists;

        private AddViewHolder(View view) {
            super(view);
            mTextNumberPlayLists = view.findViewById(R.id.text_number_playlist);
            mTextNumberPlayLists.setText(String.valueOf((mData.size() - 1)).concat(" ").concat(
                    mContext.getResources().getString(R.string.msg_1_playlists_in_total)));
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //add play list at here
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
            setupOptionsMenu();
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItemClickListener.onItemClick(mData, getAdapterPosition());
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
            if (mPlaylistName.equals("Favorite")) {
                mImageFavorite.setVisibility(View.VISIBLE);
                mTextPlayList.setVisibility(View.GONE);
            } else {
                mTextPlayList.setText(String.valueOf(item.getName().toLowerCase().charAt(0)));
            }
        }

        private void setupOptionsMenu() {
            mImageButtonOption.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final PopupMenu popupMenu = new PopupMenu(mContext, mImageButtonOption);
                    popupMenu.inflate(R.menu.options_menu_item_playlist);
                    if (mPlaylistName.equals("Favorite")) {
                        popupMenu.getMenu().getItem(1).setVisible(false);
                        popupMenu.getMenu().getItem(2).setVisible(false);
                    }
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.action_play_now:
                                    return true;
                                case R.id.action_rename:
                                    return true;
                                case R.id.action_delete:
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
