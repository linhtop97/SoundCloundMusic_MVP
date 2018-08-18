package linhnb.com.soundcloundmusic_mvp.ui.playlist;

import java.util.List;

import linhnb.com.soundcloundmusic_mvp.data.model.PlayList;
import linhnb.com.soundcloundmusic_mvp.data.source.PlayListsRepository;

public class PlayListPresenter implements PlayListContract.Presenter {

    private PlayListsRepository mRepository;
    private PlayListContract.View mView;

    public PlayListPresenter(PlayListsRepository repository, PlayListContract.View view) {
        mRepository = repository;
        mView = view;
        mView.setPresenter(this);
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {

    }

    @Override
    public void loadPlayLists() {
        mView.showLoading();
        List<PlayList> playLists = mRepository.getAllPlayList();
        playLists.add(null);
        mView.hideLoading();
        mView.onPlayListsLoaded(playLists);

    }

    @Override
    public void addPlayList(PlayList playList) {
        if (mRepository.addPlayList(playList)) {
            mView.onPlayListAdded(playList);
        }
    }

    @Override
    public void editPlayList(PlayList playList) {
        if (mRepository.updatePlayList(playList)) {
            mView.onPlayListEdited(playList);
        }
    }

    @Override
    public void deletePlayList(PlayList playList) {

    }
}
