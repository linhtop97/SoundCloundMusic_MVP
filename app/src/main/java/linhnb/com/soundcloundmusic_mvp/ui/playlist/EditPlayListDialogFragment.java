package linhnb.com.soundcloundmusic_mvp.ui.playlist;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import linhnb.com.soundcloundmusic_mvp.R;
import linhnb.com.soundcloundmusic_mvp.data.model.PlayList;
import linhnb.com.soundcloundmusic_mvp.ui.main.MainActivity;

public class EditPlayListDialogFragment extends DialogFragment implements Dialog.OnShowListener {

    private static final String ARGUMENT_PLAY_LIST = "playList";

    private EditText mEditTextName;
    private PlayList mPlayList;
    private MainActivity mMainActivity;

    private Callback mCallback;

    public static EditPlayListDialogFragment newInstance() {
        return newInstance(null);
    }

    public static EditPlayListDialogFragment newInstance(@Nullable PlayList playList) {
        EditPlayListDialogFragment fragment = new EditPlayListDialogFragment();
        if (playList != null) {
            Bundle arguments = new Bundle();
            arguments.putParcelable(ARGUMENT_PLAY_LIST, playList);
            fragment.setArguments(arguments);
        }
        return fragment;
    }

    public static EditPlayListDialogFragment createPlayList() {
        return newInstance();
    }

    public static EditPlayListDialogFragment editPlayList(PlayList playList) {
        return newInstance(playList);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null) {
            mPlayList = arguments.getParcelable(ARGUMENT_PLAY_LIST);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mMainActivity = (MainActivity) context;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Dialog dialog = new AlertDialog.Builder(mMainActivity)
                .setTitle(getTitle())
                .setView(R.layout.dialog_create_or_edit_play_list)
                .setNegativeButton(R.string.msg_cancel, null)
                .setPositiveButton(R.string.msg_Confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onConfirm();
                    }
                })
                .create();
        dialog.setOnShowListener(this);
        return dialog;
    }

    @Override
    public void onShow(DialogInterface dialog) {
        if (mEditTextName == null) {
            mEditTextName = (EditText) getDialog().findViewById(R.id.edit_text);
            mEditTextName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        if (mEditTextName.length() > 0) {
                            onConfirm();
                        }
                        return true;
                    }
                    return false;
                }
            });
        }
        if (isEditMode()) {
            mEditTextName.setText(mPlayList.getName());
        }
        mEditTextName.requestFocus();
        mEditTextName.setSelection(mEditTextName.length());
    }

    public EditPlayListDialogFragment setCallback(Callback callback) {
        mCallback = callback;
        return this;
    }

    private void onConfirm() {
        if (mCallback == null) return;

        PlayList playList = mPlayList;
        if (playList == null) {
            playList = new PlayList();
        }
        playList.setName(mEditTextName.getText().toString());
        if ((isEditMode())) {
            mCallback.onEdited(playList);
        } else {
            mCallback.onCreated(playList);
        }
    }

    private boolean isEditMode() {
        return mPlayList != null;
    }

    private String getTitle() {
        return mMainActivity.getString(isEditMode() ?
                R.string.play_list_edit : R.string.play_list_create);
    }

    public interface Callback {

        void onCreated(PlayList playList);

        void onEdited(PlayList playList);
    }
}
