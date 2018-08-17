package linhnb.com.soundcloundmusic_mvp.ui.home;

import java.util.List;

import linhnb.com.soundcloundmusic_mvp.data.model.Track;
import linhnb.com.soundcloundmusic_mvp.data.source.TracksDataSource;
import linhnb.com.soundcloundmusic_mvp.data.source.TracksRepository;

public class HomePresenter implements HomeContract.Presenter,
        TracksDataSource.LoadTracksCallback {

    private final HomeContract.View mView;
    private TracksRepository mTracksRepository;

    public HomePresenter(TracksRepository tracksRepository, HomeContract.View view) {
        mView = view;
        mView.setPresenter(this);
        mTracksRepository = tracksRepository;
    }

    @Override
    public void loadTracks(String genre, int limit, int offSet) {
        mView.showLoading();
        mTracksRepository.getTracks(genre, limit, offSet, this);
    }

    @Override
    public void onTracksLoaded(List<Track> tracks) {
        mView.hideLoading();
        mView.showTracks(tracks);
    }

    @Override
    public void onDataNotAvailable(String msg) {
        mView.handleError(msg);
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {

    }
}
