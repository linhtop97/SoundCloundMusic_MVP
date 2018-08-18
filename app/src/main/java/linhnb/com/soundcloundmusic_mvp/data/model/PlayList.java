package linhnb.com.soundcloundmusic_mvp.data.model;

import android.os.Parcel;
import android.os.Parcelable;

public class PlayList implements Parcelable {


    public static final Creator<PlayList> CREATOR = new Creator<PlayList>() {
        @Override
        public PlayList createFromParcel(Parcel in) {
            return new PlayList(in);
        }

        @Override
        public PlayList[] newArray(int size) {
            return new PlayList[size];
        }
    };
    private int mId;
    private String mName;
    private int mNumberTracks;

    public PlayList() {

    }

    protected PlayList(Parcel in) {
        mId = in.readInt();
        mName = in.readString();
        mNumberTracks = in.readInt();
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public int getNumberTracks() {
        return mNumberTracks;
    }

    public void setNumberTracks(int numberTracks) {
        mNumberTracks = numberTracks;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeString(mName);
        dest.writeInt(mNumberTracks);
    }
}