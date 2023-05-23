package com.example.listen;

public class UploadSong {
    public String songsCategory;
    public String songTitle;
    public String artist;
    public String album_art;
    public String songDuration;
    public String songLink;
    public String albumId;
    private String url;
    private String name;


    public UploadSong(String url, String name) {
        this.url = url;
        this.name = name;
    }

    public UploadSong(String songsCategory, String songTitle, String artist, String album_art, String songDuration, String songLink) {
       if(songTitle.trim().equals("")){
           songTitle = "No title";
       }


        this.songsCategory = songsCategory;
        this.songTitle = songTitle;
        this.artist = artist;
        this.album_art = album_art;
        this.songDuration = songDuration;
        this.songLink = songLink;


    }
    public UploadSong(){

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

    public String getAlbumId() {
        return albumId;
    }

    public void setAlbumId(String albumId) {
        this.albumId = albumId;
    }

    public String getSongsCategory() {
        return songsCategory;
    }

    public void setSongsCategory(String songsCategory) {
        this.songsCategory = songsCategory;
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

    public String getAlbum_art() {
        return album_art;
    }

    public void setAlbum_art(String album_art) {
        this.album_art = album_art;
    }

    public String getSongDuration() {
        return songDuration;
    }

    public void setSongDuration(String songDuration) {
        this.songDuration = songDuration;
    }

    public String getSongLink() {
        return songLink;
    }

    public void setSongLink(String songLink) {
        this.songLink = songLink;
    }

    public UploadSong(String songsCategory) {
        this.songsCategory = songsCategory;
    }


    }
