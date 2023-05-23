package com.example.listen;
import android.os.Parcel;
import android.os.Parcelable;

public class Song implements Parcelable {
    private String songTitle;
    private String artist;
    private String songDuration;

    public Song(String songTitle, String artist, String songDuration) {
        this.songTitle = songTitle;
        this.artist = artist;
        this.songDuration = songDuration;
    }

    protected Song(Parcel in) {
        songTitle = in.readString();
        artist = in.readString();
        songDuration = in.readString();
    }

    public static final Creator<Song> CREATOR = new Creator<Song>() {
        @Override
        public Song createFromParcel(Parcel in) {
            return new Song(in);
        }

        @Override
        public Song[] newArray(int size) {
            return new Song[size];
        }
    };

    public String getSongTitle() {
        return songTitle;
    }

    public String getArtist() {
        return artist;
    }

    public String getSongDuration() {
        return songDuration;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(songTitle);
        dest.writeString(artist);
        dest.writeString(songDuration);
    }


}
