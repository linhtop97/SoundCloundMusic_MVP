package linhnb.com.soundcloundmusic_mvp.ui.local;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import linhnb.com.soundcloundmusic_mvp.R;
import linhnb.com.soundcloundmusic_mvp.data.model.Track;
import linhnb.com.soundcloundmusic_mvp.data.source.local.PreferenceManager;
import linhnb.com.soundcloundmusic_mvp.ui.base.adapter.ListAdapter;
import linhnb.com.soundcloundmusic_mvp.ui.maincontent.TabType;
import linhnb.com.soundcloundmusic_mvp.utils.StringUtil;

public class TrackAdapter extends ListAdapter<Track> {
    public TrackAdapter(Context context, List<Track> tracks) {
        super(context, tracks);
        mData = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_track_local, parent,
                false);
        return new TrackViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        TrackViewHolder trackViewHolder = (TrackViewHolder) holder;
        trackViewHolder.bind(mData.get(position), position);
    }

    private class TrackViewHolder extends RecyclerView.ViewHolder {
        private TextView mTextTitle;
        private TextView mTextInfor;
        private TextView mTextViewIndex;
        private ImageButton mImageButtonOption;
        private Track mTrack;

        public TrackViewHolder(View itemView) {
            super(itemView);
            mTextTitle = itemView.findViewById(R.id.text_title);
            mTextInfor = itemView.findViewById(R.id.text_info);
            mTextViewIndex = itemView.findViewById(R.id.text_view_index);
            mImageButtonOption = itemView.findViewById(R.id.img_button_options);
            setupOptionsMenu();
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PreferenceManager.setImageUrl(mContext, null);
                    PreferenceManager.setTab(mContext, TabType.LOCAL_MUSIC);
                    mItemClickListener.onItemClick(mData, getAdapterPosition());
                }
            });
        }

        public void bind(Track item, int position) {
            if (item == null) {
                return;
            }
            mTrack = item;
            mTextTitle.setText(mTrack.getTitle());
            mTextInfor.setText(String.format("%s | %s", StringUtil.formatDuration(mTrack.getDuration()),
                    mTrack.getUserName()));
            mTextViewIndex.setText(String.valueOf(position + 1));
        }

        private void setupOptionsMenu() {
            mImageButtonOption.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final PopupMenu popupMenu = new PopupMenu(mContext, mImageButtonOption);
                    popupMenu.inflate(R.menu.options_menu_item_track);
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
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