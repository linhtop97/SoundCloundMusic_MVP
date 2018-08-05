package linhnb.com.soundcloundmusic_mvp.ui.base.adapter;

import java.util.List;

public interface OnItemClickListener<T> {

    void onItemClick(int position);

    void onItemClick(List<T> list, int position);
}
