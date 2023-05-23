package com.example.listen.Model;

public class UploadAlbum2 {
    String url;
    String name;
    String albumId;
    private String songTitle;
    private String artist;
    private String songDuration;

    public UploadAlbum2(String url, String name, String songCategory) {
        this.url = url;
        this.name = name;
        this.albumId = songCategory;
    }
    public UploadAlbum2() {
        // Diperlukan konstruktor kosong untuk Firebase
    }

    public String getAlbumId() {
        return albumId;
    }

    public void setAlbumId(String albumId) {
        this.albumId = albumId;
    }

    public String getSongTitle() {
        return songTitle;
    }

    public void setSongTitle(String songTitle) {
        this.songTitle = songTitle;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getSongDuration() {
        return songDuration;
    }

    public void setSongDuration(String songDuration) {
        this.songDuration = songDuration;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
