package com.example.spotifysdk;

import java.util.List;

public class SpotifyWrapped {
    private List<String> topArtists;
    private List<String> topTracks;
    private List<String> topGenres;

    public SpotifyWrapped(List<String> topArtists, List<String> topTracks, List<String> topGenres) {
        this.topArtists = topArtists;
        this.topTracks = topTracks;
        this.topGenres = topGenres;
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
