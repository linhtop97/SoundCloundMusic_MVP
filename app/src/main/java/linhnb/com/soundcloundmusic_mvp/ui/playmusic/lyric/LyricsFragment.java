package linhnb.com.soundcloundmusic_mvp.ui.playmusic.lyric;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import linhnb.com.soundcloundmusic_mvp.R;

public class LyricsFragment extends Fragment {
    public static LyricsFragment newInstance() {
        return new LyricsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View viewRoot = inflater.inflate(R.layout.fragment_lyrics, container, false);
        return viewRoot;
    }
}