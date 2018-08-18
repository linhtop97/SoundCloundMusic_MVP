package linhnb.com.soundcloundmusic_mvp.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Track implements Parcelable {

    public static final Creator<Track> CREATOR = new Creator<Track>() {
        @Override
        public Track createFromParcel(Parcel in) {
            return new Track(in);
        }

        @Override
        public Track[] newArray(int size) {
            return new Track[size];
        }
    };
    @SerializedName("artwork_url")
    @Expose
    private String mArtworkUrl;
    @SerializedName("downloadable")
    @Expose
    private boolean mDownloadable;
    @SerializedName("download_url")
    @Expose
    private String mDownloadUrl;
    @SerializedName("duration")
    @Expose
    private int mDuration;
    @SerializedName("id")
    @Expose
    private int mId;
    @SerializedName("likes_count")
    @Expose
    private int mLikesCount;
    @SerializedName("title")
    @Expose
    private String mTitle;
    @SerializedName("uri")
    @Expose
    private String mUri;
    @SerializedName("username")
    @Expose
    private String mUserName;
    @SerializedName("playback_count")
    @Expose
    private int mPlaybackCount;

    public Track(Builder builder) {
        this.mArtworkUrl = builder.mArtworkUrl;
        this.mDownloadable = builder.mDownloadable;
        this.mDuration = builder.mDuration;
        this.mId = builder.mId;
        this.mLikesCount = builder.mLikesCount;
        this.mTitle = builder.mTitle;
        this.mUri = builder.mUri;
        this.mUserName = builder.mUserName;
        this.mPlaybackCount = builder.mPlaybackCount;
        this.mDownloadUrl = builder.mDownloadUrl;
    }

    protected Track(Parcel in) {
        mArtworkUrl = in.readString();
        mDownloadable = in.readByte() != 0;
        mDownloadUrl = in.readString();
        mDuration = in.readInt();
        mId = in.readInt();
        mLikesCount = in.readInt();
        mTitle = in.readString();
        mUri = in.readString();
        mUserName = in.readString();
        mPlaybackCount = in.readInt();
    }

    public String getArtworkUrl() {
        return mArtworkUrl;
    }

    public void setArtworkUrl(String artworkUrl) {
        mArtworkUrl = artworkUrl;
    }

    public boolean isDownloadable() {
        return mDownloadable;
    }

    public void setDownloadable(boolean downloadable) {
        mDownloadable = downloadable;
    }

    public String getDownloadUrl() {
        return mDownloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        mDownloadUrl = downloadUrl;
    }

    public int getDuration() {
        return mDuration;
    }

    public void setDuration(int duration) {
        mDuration = duration;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public int getLikesCount() {
        return mLikesCount;
    }

    public void setLikesCount(int likesCount) {
        mLikesCount = likesCount;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getUri() {
        return mUri;
    }

    public void setUri(String uri) {
        mUri = uri;
    }

    public String getUserName() {
        return mUserName;
    }

    public void setUserName(String userName) {
        mUserName = userName;
    }

    public int getPlaybackCount() {
        return mPlaybackCount;
    }

    public void setPlaybackCount(int playbackCount) {
        mPlaybackCount = playbackCount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mArtworkUrl);
        parcel.writeByte((byte) (mDownloadable ? 1 : 0));
        parcel.writeString(mDownloadUrl);
        parcel.writeInt(mDuration);
        parcel.writeInt(mId);
        parcel.writeInt(mLikesCount);
        parcel.writeString(mTitle);
        parcel.writeString(mUri);
        parcel.writeString(mUserName);
        parcel.writeInt(mPlaybackCount);
    }

    public static class TrackEntity {
        public static final String COLLECTION = "collection";
        public static final String ARTWORK_URL = "artwork_url";
        public static final String DOWNLOADABLE = "downloadable";
        public static final String DOWNLOAD_URL = "download_url";
        public static final String DURATION = "duration";
        public static final String ID = "id";
        public static final String PLAYBACK_COUNT = "playback_count";
        public static final String TITLE = "title";
        public static final String URI = "uri";
        public static final String USER = "user";
        public static final String TRACK = "track";
        public static final String USERNAME = "username";
        public static final String LIKES_COUNT = "likes_count";
        public static final String LARGE_IMAGE_SIZE = "large";
        public static final String BETTER_IMAGE_SIZE = "original";
    }

    public static class Builder {
        private String mArtworkUrl;
        private boolean mDownloadable;
        private String mDownloadUrl;
        private int mDuration;
        private int mId;
        private int mLikesCount;
        private String mTitle;
        private String mUri;
        private String mUserName;
        private int mPlaybackCount;

        public Builder setArtworkUrl(String artworkUrl) {
            mArtworkUrl = artworkUrl;
            return this;
        }

        public Builder setDownloadable(boolean downloadable) {
            mDownloadable = downloadable;
            return this;
        }

        public Builder setDownloadUrl(String downloadUrl) {
            mDownloadUrl = downloadUrl;
            return this;
        }

        public Builder setDuration(int duration) {
            mDuration = duration;
            return this;
        }

        public Builder setId(int id) {
            mId = id;
            return this;
        }

        public Builder setLikesCount(int likesCount) {
            mLikesCount = likesCount;
            return this;
        }

        public Builder setTitle(String title) {
            mTitle = title;
            return this;
        }

        public Builder setUri(String uri) {
            mUri = uri;
            return this;
        }

        public Builder setUserName(String userName) {
            mUserName = userName;
            return this;
        }

        public Builder setPlaybackCount(int playbackCount) {
            mPlaybackCount = playbackCount;
            return this;
        }

        public Track build() {
            return new Track(this);
        }
    }
}
