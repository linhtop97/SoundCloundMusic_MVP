package linhnb.com.soundcloundmusic_mvp.ui.splash;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import linhnb.com.soundcloundmusic_mvp.R;
import linhnb.com.soundcloundmusic_mvp.ui.main.MainActivity;
import linhnb.com.soundcloundmusic_mvp.ui.maincontent.MainFragment;
import linhnb.com.soundcloundmusic_mvp.utils.Constant;
import linhnb.com.soundcloundmusic_mvp.utils.FragmentManagerUtils;

public class SplashFragment extends Fragment implements SplashContract.View {

    private SplashContract.Presenter mPresenter;
    private MainActivity mMainActivity;

    public static SplashFragment newInstance(int timeDelay) {
        SplashFragment fragment = new SplashFragment();
        Bundle args = new Bundle();
        args.putInt(Constant.ARGUMENT_TIME_DELAY, timeDelay);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View viewRoot = inflater.inflate(R.layout.fragment_splash, container, false);
        initView(viewRoot);
        return viewRoot;
    }

    private void initView(View viewRoot) {
        int timeDelay = getArguments().getInt(Constant.ARGUMENT_TIME_DELAY);
        final Animation animationIcon = AnimationUtils.loadAnimation(mMainActivity, R.anim.show_app_icon);
        viewRoot.findViewById(R.id.img_app_icon).startAnimation(animationIcon);
        final Animation animationName = AnimationUtils.loadAnimation(mMainActivity, R.anim.show_app_name);
        viewRoot.findViewById(R.id.textview_app_name).startAnimation(animationName);
        mPresenter = new SplashPresenter(this);
        mPresenter.startingDelay(timeDelay);
    }

    @Override
    public void showMainApp() {
        FragmentManager manager = mMainActivity.getSupportFragmentManager();
        Fragment fragment = manager.findFragmentByTag(this.getClass().getSimpleName());
        if (fragment != null)
            manager.beginTransaction().remove(fragment).commit();
        MainFragment mainFragment = MainFragment.newInstance(false);
        FragmentManagerUtils.addFragment(manager, mainFragment, R.id.app_content,
                mainFragment.getClass().getSimpleName(), false);

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
    }

    @Override
    public void setPresenter(SplashContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mMainActivity = (MainActivity) context;
    }
}
