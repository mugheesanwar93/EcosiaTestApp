package org.ecosia.ecosiatestapp.pojo;

public class AudioMetaData {
    private String title;
    private String artist;
    private String album;
    private String duration;
    private String data;
    private String albumId;

    public AudioMetaData(String title, String artist, String album, String duration, String data, String albumId) {
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.duration = duration;
        this.data = data;
        this.albumId = albumId;
    }

    public String getTitle() {

        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getalbumId() {
        return albumId;
    }

    public void setalbumId(String albumId) {
        this.albumId = albumId;
    }
}
