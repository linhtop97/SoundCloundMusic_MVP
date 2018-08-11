package linhnb.com.soundcloundmusic_mvp.ui.playmusic.playanim;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import de.hdodenhof.circleimageview.CircleImageView;
import linhnb.com.soundcloundmusic_mvp.R;
import linhnb.com.soundcloundmusic_mvp.data.model.Track;
import linhnb.com.soundcloundmusic_mvp.source.local.PreferenceManager;
import linhnb.com.soundcloundmusic_mvp.ui.main.MainActivity;
import linhnb.com.soundcloundmusic_mvp.ui.maincontent.TabType;
import linhnb.com.soundcloundmusic_mvp.utils.ImageUtil;

public class PlayAnimationFragment extends Fragment implements PlayAnimContract.View {

    private Animation mAnimation;
    private CircleImageView mImageView;
    private PlayAnimContract.Presenter mPresenter;
    private MainActivity mMainActivity;
    private Track mTrack;

    public static PlayAnimationFragment newInstance() {
        PlayAnimationFragment playAnimationFragment = new PlayAnimationFragment();
        return playAnimationFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mMainActivity = (MainActivity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup
            container, @Nullable Bundle savedInstanceState) {
        View viewRoot = inflater.inflate(R.layout.fragment_play_animation, container, false);
        init(viewRoot);
        return viewRoot;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        new PlayAnimPresenter(this);
        setImage();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (PreferenceManager.getIsPlaying(getActivity())) {
            mImageView.startAnimation(mAnimation);
        }
    }

    private void init(View view) {
        mImageView = view.findViewById(R.id.img_track);
        mAnimation = AnimationUtils.loadAnimation(mImageView.getContext(), R.anim.rotation);

    }

    @Override
    public void setPresenter(PlayAnimContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void setImage() {
        mImageView.setImageDrawable(mMainActivity.getResources().getDrawable(R.drawable.ic_app_large));
        if (PreferenceManager.getTab(mMainActivity) == TabType.LOCAL_MUSIC) {
            Bitmap bm = ImageUtil.parseAlbum(PreferenceManager.getListTrack(mMainActivity)
                    .get(PreferenceManager.getLastPosition(mMainActivity)));
            if (bm != null) {
                mImageView.setImageBitmap(bm);
            }
        } else {
            Glide.with(getContext())
                    .load(PreferenceManager.getImageUrl(getActivity()))
                    .asBitmap()
                    .placeholder(R.drawable.ic_app_large)
                    .error(R.drawable.ic_app_large)
                    .listener(new RequestListener<String, Bitmap>() {
                        @Override
                        public boolean onException(Exception e, String s, Target<Bitmap> target, boolean b) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Bitmap bitmap, String s, Target<Bitmap> target, boolean b, boolean b1) {
                            mImageView.setImageBitmap(bitmap);
                            return false;
                        }
                    })
                    .preload();
        }
    }

    @Override
    public void startAnimation() {
        mImageView.startAnimation(mAnimation);
    }

    @Override
    public void cancelAnimation() {
        mImageView.clearAnimation();
    }
}