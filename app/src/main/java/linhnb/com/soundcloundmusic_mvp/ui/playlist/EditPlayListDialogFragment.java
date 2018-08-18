package linhnb.com.soundcloundmusic_mvp.ui.playlist;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import linhnb.com.soundcloundmusic_mvp.R;
import linhnb.com.soundcloundmusic_mvp.data.model.PlayList;
import linhnb.com.soundcloundmusic_mvp.data.source.local.PreferenceManager;
import linhnb.com.soundcloundmusic_mvp.ui.main.MainActivity;
import linhnb.com.soundcloundmusic_mvp.utils.StringUtil;

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
        final AlertDialog dialog = new AlertDialog.Builder(mMainActivity)
                .setTitle(getTitle())
                .setView(R.layout.dialog_create_or_edit_play_list)
                .setNegativeButton(R.string.msg_cancel, null)
                .setPositiveButton(R.string.msg_Confirm, null)
                .create();
        dialog.setOnShowListener(this);
        return dialog;
    }

    @Override
    public void onResume() {
        super.onResume();
        AlertDialog alertDialog = (AlertDialog) getDialog();
        Button confirmButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onConfirm()) {
                    dismiss();
                }
            }
        });
        Button cancelButton = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    @Override
    public void onShow(final DialogInterface dialog) {
        if (mEditTextName == null) {
            mEditTextName = getDialog().findViewById(R.id.edit_text);
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

    private boolean onConfirm() {
        String[] playlists = StringUtil.getPlayLists(PreferenceManager.getPlayLists(mMainActivity).toLowerCase());

        if (mCallback == null) return true;

        PlayList playList = mPlayList;
        if (playList == null) {
            playList = new PlayList();
        }
        String playListName = mEditTextName.getText().toString();
        if (playListName.length() < 0 || playListName.isEmpty()) {
            Toast.makeText(mMainActivity, mMainActivity.getText(R.string.playlist_name_cannot_empty), Toast.LENGTH_SHORT).show();
            return false;
        }
        playList.setName(playListName);
        for (String s : playlists) {
            if (s.equals(playListName.toLowerCase())) {
                if (!isEditMode()) {
                    Toast.makeText(mMainActivity, mMainActivity.getText(R.string.playlist_name_is_exists), Toast.LENGTH_SHORT).show();
                    return false;
                }
                return true;
            }
        }
        if ((isEditMode())) {
            mCallback.onEdited(playList);
        } else {
            mCallback.onCreated(playList);
        }
        return true;
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
