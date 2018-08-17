package linhnb.com.soundcloundmusic_mvp.ui.playmusic.listtrack;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import linhnb.com.soundcloundmusic_mvp.R;
import linhnb.com.soundcloundmusic_mvp.data.model.Track;
import linhnb.com.soundcloundmusic_mvp.data.source.remote.TrackDownloadManager;
import linhnb.com.soundcloundmusic_mvp.ui.base.adapter.IAdapterView;
import linhnb.com.soundcloundmusic_mvp.ui.base.adapter.ListAdapter;
import linhnb.com.soundcloundmusic_mvp.utils.StringUtil;

public class TrackAdapterMini extends ListAdapter<Track> implements TrackDownloadManager.DownloadListener {

    public TrackAdapterMini(Context context, List<Track> data) {
        super(context, data);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_track_mini, parent,
                false);
        return new TrackViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        TrackViewHolder trackViewHolder = (TrackViewHolder) holder;
        trackViewHolder.bind(mData.get(position));
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

    private class TrackViewHolder extends RecyclerView.ViewHolder implements IAdapterView<Track> {

        private Track mTrack;
        private TextView mTextTitle;
        private TextView mTextArtist;
        private TextView mTextDuration;
        private ImageButton mImageButtonOption;
        private ImageButton mImageButtonPlay;

        public TrackViewHolder(View itemView) {
            super(itemView);
            mTextTitle = itemView.findViewById(R.id.text_title);
            mTextArtist = itemView.findViewById(R.id.text_artist);
            mTextDuration = itemView.findViewById(R.id.text_duration);
            mImageButtonOption = itemView.findViewById(R.id.img_button_options);
            mImageButtonPlay = itemView.findViewById(R.id.img_button_play);
            setupOptionsMenu();
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItemClickListener.onItemClick(getAdapterPosition());
                }
            });
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
                                                TrackAdapterMini.this)
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

        @Override
        public void bind(Track item) {
            if (item == null) {
                return;
            }
            mTrack = item;
            mTextTitle.setText(mTrack.getTitle());
            mTextArtist.setText(mTrack.getUserName());
            mTextDuration.setText(StringUtil.formatDuration(mTrack.getDuration()));
//            if (item.getId() == PreferenceManager.getListTrack(mContext)
//                    .get(PreferenceManager.getLastPosition(mContext)).getId()) {
//                mImageButtonPlay.setVisibility(View.VISIBLE);
//            }
        }
    }

}
