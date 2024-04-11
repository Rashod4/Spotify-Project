package com.example.spotifysdk;

import java.time.LocalDateTime;
import java.util.List;

public class SpotifyWrapped {
    private List<String> topArtists;
    private List<String> topTracks;
    private List<String> topGenres;
    private List<String> previewUrls;
    private List<String> imageUrls;
    private List<String> artistImageUrls;
    private List<String> topAlbums;
    private LocalDateTime dateTime;


    public SpotifyWrapped(List<String> topArtists, List<String> topTracks, List<String> topGenres,
                          List<String> previewUrls, List<String> imageUrls, List<String> artistImageUrls,
                          List<String> topAlbums) {
        this.topArtists = topArtists;
        this.topTracks = topTracks;
        this.topGenres = topGenres;
        this.previewUrls = previewUrls;
        this.imageUrls = imageUrls;
        this.artistImageUrls = artistImageUrls;
        this.topAlbums = topAlbums;
    }

    public SpotifyWrapped(List<String> topArtists, List<String> topTracks, List<String> topGenres,
                          List<String> previewUrls, List<String> imageUrls, List<String> artistImageUrls,
                          List<String> topAlbums, LocalDateTime dateTime) {
        this.topArtists = topArtists;
        this.topTracks = topTracks;
        this.topGenres = topGenres;
        this.previewUrls = previewUrls;
        this.imageUrls = imageUrls;
        this.artistImageUrls = artistImageUrls;
        this.topAlbums = topAlbums;
        this.dateTime = dateTime;
    }


    public LocalDateTime getDateTime(){ return  dateTime; };
    public List<String> getPreviewUrls() {
        return previewUrls;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public List<String> getArtistImageUrls() {
        return artistImageUrls;
    }

    public List<String> getTopAlbums() {
        return topAlbums;
    }

    public List<String> getTopArtists() {
        return topArtists;
    }

    public List<String> getTopTracks() {
        return topTracks;
    }

    public List<String> getTopGenres() {
        return topGenres;
    }

}
