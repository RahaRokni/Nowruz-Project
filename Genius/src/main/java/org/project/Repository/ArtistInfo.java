package org.project.Repository;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.project.SongRelated.Comment;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ArtistInfo {
    @JsonProperty("username")
    private String username;
    @JsonProperty("name")
    private String name;
    @JsonProperty("songsCount")
    private int songsCount;
    @JsonProperty("albumsCount")
    private int albumsCount;
    @JsonProperty("followersCount")
    private int followersCount;
    @JsonProperty("singles")
    private List<String> singles;
    @JsonProperty("albums")
    private List<String> albums;
    @JsonProperty("totalStreams")
    private int totalStreams;
    @JsonProperty("albumDetails")
    private List<AlbumDetails> albumDetails;
    @JsonProperty("singleDetails")
    private List<SongDetails> singleDetails;

    private int age = 12;
    private String password = "1234!Asdf";
    private String email = "test@test.com";

    public ArtistInfo() {
        this.singles = new ArrayList<>();
        this.albums = new ArrayList<>();
        this.albumDetails = new ArrayList<>();
        this.singleDetails = new ArrayList<>();
    }

    public static ArtistInfo fromArtist(org.project.Accounts.Artist artist) {
        ArtistInfo info = new ArtistInfo();
        info.setUsername(artist.getUsername());
        info.setName(artist.getName());
        info.setSongsCount(artist.getAllSongs().size());
        info.setAlbumsCount(artist.getAlbums().size());
        info.setFollowersCount(artist.getFollowersCount());



        info.setSingleDetails(artist.getSingles().stream()
                .map(song -> new SongDetails(
                        song.getTitle(),
                        song.getLyrics(),
                        song.getGenre(),
                        song.getReleaseDate(),
                        song.getComments()
                ))
                .collect(Collectors.toList()));

        info.setSingles(info.getSingleDetails().stream()
                .map(song -> song.getTitle() + " (" + song.getGenre() + ")")
                .collect(Collectors.toList()));

        List<AlbumDetails> albumDetailsList = artist.getAlbums() != null ?
                artist.getAlbums().stream()
                        .map(album -> new AlbumDetails(
                                album.getTitle(),
                                album.getGenre(),
                                album.getTrackList() != null ?
                                        album.getTrackList().stream()
                                                .map(song -> new SongDetails(
                                                        song.getTitle(),
                                                        song.getLyrics(),
                                                        song.getGenre(),
                                                        song.getReleaseDate(),
                                                        song.getComments()
                                                ))
                                                .collect(Collectors.toList()) :
                                        new ArrayList<>()
                        ))
                        .collect(Collectors.toList()) :
                new ArrayList<>();
        info.setAlbumDetails(albumDetailsList);

        info.setTotalStreams(artist.getTotalStreams());
        return info;
    }

    public List<SongDetails> getSingleDetails() {
        return singleDetails;
    }

    void setSingleDetails(List<SongDetails> singleDetails) {
        this.singleDetails = singleDetails;
    }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getSongsCount() { return songsCount; }
    public void setSongsCount(int songsCount) { this.songsCount = songsCount; }

    public int getAlbumsCount() {
        return this.albumsCount;
    }
    public void setAlbumsCount(int albumsCount) {
        this.albumsCount = albumsCount; }

    public int getFollowersCount() { return followersCount; }
    public void setFollowersCount(int followersCount) { this.followersCount = followersCount; }

    public List<String> getSingles() { return singles; }
    public void setSingles(List<String> singles) { this.singles = singles; }

    public List<String> getAlbums() { return albums; }
    public void setAlbums(List<String> albums) { this.albums = albums; }

    public int getTotalStreams() { return totalStreams; }
    public void setTotalStreams(int totalStreams) { this.totalStreams = totalStreams; }

    public List<AlbumDetails> getAlbumDetails() {
        return albumDetails;
    }

    public void setAlbumDetails(List<AlbumDetails> albumDetails) {
        this.albumDetails = albumDetails;
    }

    public int getAge() {
        return age;

    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public static class AlbumDetails {
        private String title;
        private String genre;
        private List<SongDetails> tracks;

        public AlbumDetails() {
        }

        public AlbumDetails(String title, String genre, List<SongDetails> tracks) {
            this.title = title;
            this.genre = genre;
            this.tracks = tracks;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getGenre() {
            return genre;
        }

        public void setGenre(String genre) {
            this.genre = genre;
        }

        public List<SongDetails> getTracks() {
            return tracks;
        }

        public void setTracks(List<SongDetails> tracks) {
            this.tracks = tracks;
        }
    }

    public static class SongDetails {
        private String title;
        private String lyrics;
        private String genre;
        private String releaseDate;
        private List<Comment> comments;

        public SongDetails() {}

        public SongDetails(String title, String lyrics, String genre, String releaseDate, List<Comment> comments) {
            this.title = title;
            this.lyrics = lyrics;
            this.genre = genre;
            this.releaseDate = releaseDate;
            this.comments = comments != null ? comments : new ArrayList<>();
        }

        public List<Comment> getComments() { return comments; }
        public void setComments(List<Comment> comments) { this.comments = comments; }
        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        public String getLyrics() { return lyrics; }
        public void setLyrics(String lyrics) { this.lyrics = lyrics; }
        public String getGenre() { return genre; }
        public void setGenre(String genre) { this.genre = genre; }
        public String getReleaseDate() { return releaseDate; }
        public void setReleaseDate(String releaseDate) { this.releaseDate = releaseDate; }
    }

    @Override
    public String toString() {
        return "ArtistInfo{" +
                "username='" + username + '\'' +
                ", name='" + name + '\'' +
                ", songsCount=" + songsCount +
                ", albumsCount=" + albumsCount +
                ", followersCount=" + followersCount +
                ", totalStreams=" + totalStreams +
                '}';
    }
}