package linhnb.com.soundcloundmusic_mvp.data.model;

public class PlayList {
    private String mName;
    private int mNumberTracks;
    private String mImage;

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

    public String getImage() {
        return mImage;
    }

    public void setImage(String image) {
        mImage = image;
    }

}