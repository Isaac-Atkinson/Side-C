package com.isaaca.musictracker.requests;

public class AlbumRequest {

    private String title;
    private int year;
    private int rating;
    private String artist;

    public String getTitle() {
        return title;
    }

    public int getYear() {
        return year;
    }

    public int getRating() {
        return rating;
    }

    public String getArtist() {
        return artist;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }
}
