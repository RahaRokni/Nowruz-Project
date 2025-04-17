package org.project.Accounts;

import org.project.Repository.RegularInfo;
import org.project.SongRelated.Song;
import org.project.SongRelated.Album;
import org.project.SongRelated.LyricEditRequest;

import java.util.*;
import java.util.stream.Collectors;

public class Regular extends Accounts {
    private RegularInfo regularInfo;
    private List<Song> allSongs;
    private List<Album> allAlbums;
    private List<Artist> allArtists;

    public Regular(String name, int age, String username, String email, String password) {
        super(name, age, username, email, password);
        this.regularInfo = new RegularInfo(username);
        this.allSongs = new ArrayList<>();
        this.allAlbums = new ArrayList<>();
        this.allArtists = new ArrayList<>();
    }

    public void followArtist(Artist artist) {
        if (artist != null && !regularInfo.isFollowing(artist.getUsername())) {
            regularInfo.followArtist(artist.getUsername());
            System.out.println("You are now following " + artist.getName());
        }
    }

    public void unfollowArtist(Artist artist) {
        if (artist != null) {
            regularInfo.unfollowArtist(artist.getUsername());
            System.out.println("You have unfollowed " + artist.getName());
        }
    }

    public List<String> getFollowedArtists() {
        return regularInfo.getFollowedArtists();
    }

    public boolean isFollowing(Artist artist) {
        return artist != null && regularInfo.isFollowing(artist.getUsername());
    }

    public void viewSongLyrics(Song song) {
        if (song != null) {
            System.out.println("\n=== " + song.getTitle() + " Lyrics ===");
            System.out.println(song.getLyrics());
            song.incrementViews();
        }
    }

    public void viewAlbumDetails(Album album) {
        if (album != null) {
            System.out.println("\n=== " + album.getTitle() + " ===");
            System.out.println("Artist: " + album.getArtist());
            System.out.println("Release Date: " + album.getReleaseDate());
            System.out.println("Genre: " + album.getGenre());
            System.out.println("Tracks (" + album.getTrackCount() + "):");

            album.getTrackList().forEach(track ->
                    System.out.println(" - " + track.getTitle() +
                            " (" + track.getViews() + " views)"));
        }
    }

    public void suggestLyricEdit(Song song, String suggestedLyrics) {
        if (song != null && suggestedLyrics != null && !suggestedLyrics.trim().isEmpty()) {
            regularInfo.addLyricEditRequest(new LyricEditRequest(
                    song.getTitle(),
                    song.getLyrics(),
                    suggestedLyrics,
                    this.getUsername()
            ));
            System.out.println("Your lyric suggestion has been submitted for approval.");
        }
    }

    public List<LyricEditRequest> getLyricEditRequests() {
        return regularInfo.getLyricEditRequests();
    }

    public List<Song> searchSongs(String query) {
        return allSongs.stream()
                .filter(song -> song.getTitle().toLowerCase().contains(query.toLowerCase()) ||
                        song.getArtist().toLowerCase().contains(query.toLowerCase()) ||
                        song.getLyrics().toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<Album> searchAlbums(String query) {
        return allAlbums.stream()
                .filter(album -> album.getTitle().toLowerCase().contains(query.toLowerCase()) ||
                        album.getArtist().toLowerCase().contains(query.toLowerCase()) ||
                        album.getGenre().toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<Artist> searchArtists(String query) {
        return allArtists.stream()
                .filter(artist -> artist.getName().toLowerCase().contains(query.toLowerCase()) ||
                        artist.getUsername().toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toList());
    }

    public void searchAll(String query) {
        System.out.println("\n=== Search Results for '" + query + "' ===");

        List<Song> songs = searchSongs(query);
        List<Album> albums = searchAlbums(query);
        List<Artist> artists = searchArtists(query);

        System.out.println("\nArtists (" + artists.size() + "):");
        artists.forEach(artist -> System.out.println(" - " + artist.getName() +
                (isFollowing(artist) ? " (Following)" : "")));

        System.out.println("\nAlbums (" + albums.size() + "):");
        albums.forEach(album -> System.out.println(" - " + album.getTitle() + " by " + album.getArtist()));

        System.out.println("\nSongs (" + songs.size() + "):");
        songs.forEach(song -> System.out.println(" - " + song.getTitle() + " by " + song.getArtist()));
    }


    public void viewTopSongs(int count) {
        if (allSongs.isEmpty()) {
            System.out.println("No songs available.");
            return;
        }

        List<Song> sortedSongs = new ArrayList<>(allSongs);
        sortedSongs.sort(Comparator.comparingInt(Song::getViews).reversed());

        System.out.println("\n=== Top " + Math.min(count, sortedSongs.size()) + " Songs ===");
        for (int i = 0; i < Math.min(count, sortedSongs.size()); i++) {
            Song song = sortedSongs.get(i);
            System.out.printf("%d. %s by %s (%d views)%n",
                    i+1, song.getTitle(), song.getArtist(), song.getViews());
        }
    }

    public void commentOnSong(Song song, String commentText) {
        if (song != null && commentText != null && !commentText.trim().isEmpty()) {
            song.addComment(this.getUsername(), commentText);
            System.out.println("Comment added successfully!");
        } else {
            System.out.println("Invalid song or comment.");
        }
    }

    public RegularInfo getRegularInfo() {
        return regularInfo;
    }

    @Override
    public String toString() {
        return "RegularUser{" +
                "name='" + getName() + '\'' +
                ", username='" + getUsername() + '\'' +
                ", following=" + regularInfo.getFollowedArtists().size() + " artists" +
                ", pendingLyricEdits=" + regularInfo.getLyricEditRequests().size() +
                '}';
    }
}