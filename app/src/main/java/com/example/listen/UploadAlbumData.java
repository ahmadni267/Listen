package com.example.listen;

public class UploadAlbumData {
    private String albumName;
    private String albumId;
    private String imageUrl;
    private int totalMusic;

    public UploadAlbumData() {
        // Default constructor needed for Firebase
    }



    public UploadAlbumData(String albumName, String fileUrl, String imageUrl, String songCategory) {
        this.albumName = albumName;
        this.albumId = fileUrl;
        this.imageUrl = imageUrl;
        this.totalMusic = totalMusic;

    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public String getAlbumId() {
        return albumId;
    }

    public void setAlbumId(String albumId) {
        this.albumId = albumId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getTotalMusic() {
        return totalMusic;
    }

    public void setTotalMusic(int totalMusic) {
        this.totalMusic = totalMusic;
    }
}