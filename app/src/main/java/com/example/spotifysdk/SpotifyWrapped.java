package com.example.spotifysdk;

import java.util.List;

public class SpotifyWrapped {
    private List<String> topArtists;
    private List<String> topTracks;

    public SpotifyWrapped(List<String> topArtists, List<String> topTracks) {
        this.topArtists = topArtists;
        this.topTracks = topTracks;
    }

    public List<String> getTopArtists() {
        return topArtists;
    }

    public List<String> getTopTracks() {
        return topTracks;
    }
}
