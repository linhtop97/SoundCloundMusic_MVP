package linhnb.com.soundcloundmusic_mvp.data.source.remote;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;

import linhnb.com.soundcloundmusic_mvp.R;
import linhnb.com.soundcloundmusic_mvp.data.model.Track;
import linhnb.com.soundcloundmusic_mvp.utils.Constant;
import linhnb.com.soundcloundmusic_mvp.utils.PermissionCheck;
import linhnb.com.soundcloundmusic_mvp.utils.StringUtil;

public class TrackDownloadManager {
    private Track mTrack;
    private Context mContext;
    private DownloadListener mListener;

    public TrackDownloadManager(Context context, DownloadListener downloadListener) {
        mContext = context;
        mListener = downloadListener;
    }

    public void download(Track track) {
        mTrack = track;
        if (track == null || mContext == null) {
            notifyCantDownload();
            return;
        }

        if (PermissionCheck.readAndWriteExternalStorage(mContext)) {
            DownloadManager downloadManager = (DownloadManager)
                    mContext.getSystemService(Context.DOWNLOAD_SERVICE);
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(
                    StringUtil.convertUrlDownloadTrack(mTrack.getDownloadUrl())));
            request.setNotificationVisibility(DownloadManager.Request
                    .VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            request.setTitle(mTrack.getTitle());
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,
                    mTrack.getTitle() + Constant.FILE_EXTENTION);
            downloadManager.enqueue(request);

            notifyDownloading();
        }

    }

    private void notifyCantDownload() {
        if (mListener == null) return;
        mListener.onDownloadError(mContext.getResources().getString(R.string.msg_cant_download));
    }

    private void notifyDownloading() {
        if (mListener == null) return;
        mListener.onDownloading();
    }

    public interface DownloadListener {

        void onDownloadError(String msg);

        void onDownloading();
    }
}
