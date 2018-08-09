package linhnb.com.soundcloundmusic_mvp.ui.local;

import linhnb.com.soundcloundmusic_mvp.source.TracksRepository;

public class LocalMusicPresenter implements LocalMusicContract.Presenter {

    private LocalMusicContract.View mView;
    private TracksRepository mTracksRepository;

    public LocalMusicPresenter(TracksRepository tracksRepository, LocalMusicContract.View view) {
        mTracksRepository = tracksRepository;
        mView = view;
        mView.setPresenter(this);
    }

    @Override
    public void loadLocalTracks() {
        mView.showTracks(mTracksRepository.getTracks());
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {

    }
}
