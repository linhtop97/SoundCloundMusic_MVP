package linhnb.com.soundcloundmusic_mvp.source.remote;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import linhnb.com.soundcloundmusic_mvp.ui.playmusic.service.MusicService;


public class FetchBitmapFromUrl extends AsyncTask<String, Void, Bitmap> {

    private MusicService mMusicService;
    private Bitmap bitmap;

    public FetchBitmapFromUrl(MusicService musicService) {
        mMusicService = musicService;
    }

    @Override
    protected Bitmap doInBackground(String... strings) {
        try {
            URL url = new URL(strings[0]);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            // Log exception
            return null;
        }
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (bitmap != null) {
            mMusicService.setBitmap(bitmap);
        }
    }


}
