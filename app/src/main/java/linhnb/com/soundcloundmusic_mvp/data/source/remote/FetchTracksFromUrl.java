package linhnb.com.soundcloundmusic_mvp.data.source.remote;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import linhnb.com.soundcloundmusic_mvp.data.model.Track;
import linhnb.com.soundcloundmusic_mvp.data.source.TracksDataSource;
import linhnb.com.soundcloundmusic_mvp.utils.Constant;
import linhnb.com.soundcloundmusic_mvp.utils.StringUtil;

public class FetchTracksFromUrl extends AsyncTask<String, Void, List<Track>> {

    private TracksDataSource.LoadTracksCallback mCallback;

    public FetchTracksFromUrl(TracksDataSource.LoadTracksCallback callback) {
        mCallback = callback;
    }

    public static String getJSONStringFromURL(String urlString) throws IOException {
        HttpURLConnection httpURLConnection = null;
        try {
            URL url = new URL(urlString);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod(Constant.REQUEST_METHOD_GET);
            httpURLConnection.setReadTimeout(Constant.READ_TIME_OUT);
            httpURLConnection.setConnectTimeout(Constant.CONNECT_TIME_OUT);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.connect();

            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append(Constant.BREAK_LINE);
            }
            br.close();
            httpURLConnection.disconnect();
            return sb.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected List<Track> doInBackground(String... strings) {
        try {
            JSONObject jsonObject = new JSONObject(getJSONStringFromURL(strings[0]));
            return getTracksFromJsonObject(jsonObject);
        } catch (IOException e) {
            mCallback.onDataNotAvailable(e.getMessage());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(List<Track> tracks) {
        if (tracks == null) {
            mCallback.onDataNotAvailable(Constant.INTERNET_NOT_AVAIABLE);
        }
        if (mCallback != null) {
            mCallback.onTracksLoaded(tracks);
        }
    }

    private List<Track> getTracksFromJsonObject(JSONObject jsonObject) {
        ArrayList<Track> tracks = new ArrayList<>();
        JSONArray jsonCollection = null;
        jsonCollection = jsonObject.optJSONArray(Track.TrackEntity.COLLECTION);
        int lenght = jsonCollection.length();
        for (int i = 0; i < lenght; i++) {

            JSONObject jsonObjectTrack = null;
            jsonObjectTrack = jsonCollection.optJSONObject(i);
            JSONObject trackObj = jsonObjectTrack.optJSONObject(Track.TrackEntity.TRACK);
            Track track = parseJsonObjectToTrackObject(trackObj);
            if (track != null) {
                tracks.add(track);
            }

        }
        return tracks;
    }

    private String parseArtworkUrlToBetter(String artworkUrl) {
        if (artworkUrl != null) {
            return artworkUrl.replace(Track.TrackEntity.LARGE_IMAGE_SIZE,
                    Track.TrackEntity.BETTER_IMAGE_SIZE);
        }
        return null;
    }

    private Track parseJsonObjectToTrackObject(JSONObject jsonTrack) {
        Track track = new Track();
        try {
            JSONObject jsonUser = jsonTrack.getJSONObject(Track.TrackEntity.USER);
            track.setArtworkUrl(parseArtworkUrlToBetter(jsonTrack.optString(Track.TrackEntity.ARTWORK_URL)));
            track.setDownloadable(jsonTrack.optBoolean(Track.TrackEntity.DOWNLOADABLE));
            track.setDownloadUrl(jsonTrack.optString(Track.TrackEntity.DOWNLOAD_URL));
            track.setDuration(jsonTrack.optInt(Track.TrackEntity.DURATION));
            track.setId(jsonTrack.optInt(Track.TrackEntity.ID));
            track.setPlaybackCount(jsonTrack.optInt(Track.TrackEntity.PLAYBACK_COUNT));
            track.setTitle(jsonTrack.optString(Track.TrackEntity.TITLE));
            track.setUri(StringUtil.getUrlStreamTrack(jsonTrack.optString(Track.TrackEntity.URI)));
            track.setLikesCount(jsonTrack.optInt(Track.TrackEntity.LIKES_COUNT));
            track.setUserName(jsonUser.optString(Track.TrackEntity.USERNAME));

        } catch (JSONException e) {
            return null;
        }

        return track;
    }
}
