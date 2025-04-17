package org.project.Accounts;

import org.project.SongRelated.Album;
import org.project.SongRelated.Comment;
import org.project.SongRelated.Song;
import org.project.Repository.ArtistInfo;

import java.util.ArrayList;
import java.util.List;

public class Artist extends Accounts {

    private List<Song> singles;
    private List<Album> albums;
    private int followersCount;
    private int AlbumCount;

    public Artist(String name, int age, String username, String email, String password) {
        super(name, age, username, email, password);
        this.followersCount = 0;
        this.singles = new ArrayList<>();
        this.albums = new ArrayList<>();
    }

    public int getFollowersCount() {
        return followersCount;
    }

    public void incrementFollowers() {
        followersCount++;
    }

    public void decrementFollowers() {
        if (followersCount > 0) {
            followersCount--;
        }
    }

    public Song createSingle(String title, String lyrics, String genre, String releaseDate) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be empty!");
        }

        if (singles.stream().anyMatch(s -> s.getTitle().equalsIgnoreCase(title))) {
            System.out.println("Single '" + title + "' already exists!");
            return null;
        }

        Song single = new Song(title, this.getName(),
                lyrics != null ? lyrics : "",
                genre != null ? genre : "Unknown",
                releaseDate != null ? releaseDate : "Unknown");
        singles.add(single);
        return single;
    }

    public Album createAlbum(String title, String releaseDate, String genre) {
        if (albums.stream().anyMatch(a -> a.getTitle().equals(title))) {
            System.out.println("Album already exists!");
            return null;
        }
        Album album = new Album(title, this.getName(), releaseDate, genre);
        albums.add(album);
        return album;
    }

    public void addSongToAlbum(Album album, Song song) {
        if (album != null && song != null && song.getArtist().equals(this.getName())) {
            album.addSong(song);
            singles.remove(song);
        }
    }

    public List<Song> getAllSongs() {
        List<Song> allSongs = new ArrayList<>(singles);
        albums.forEach(album -> allSongs.addAll(album.getTrackList()));
        return allSongs;
    }

    public List<Song> getSingles() {
        return new ArrayList<>(singles);
    }

    public List<Album> getAlbums() {
        return new ArrayList<>(albums);
    }

    public int getTotalStreams() {
        return getAllSongs().stream().mapToInt(Song::getViews).sum();
    }

    public boolean removeSong(Song song) {
        boolean removedFromSingles = singles.remove(song);
        boolean removedFromAlbums = albums.stream()
                .anyMatch(album -> album.removeSong(song));
        return removedFromSingles || removedFromAlbums;
    }

    public void addSongToAlbum(String albumName, Song song) {
        Album album = albums.stream()
                .filter(a -> a.getTitle().equals(albumName))
                .findFirst()
                .orElse(null);

        if (album != null) {
            album.addSong(song);
            singles.remove(song);
            System.out.println("Song added to album successfully!");
        } else {
            System.out.println("Album not found!");
        }
    }

    public void updateAlbum(String albumName, String newTitle, String newGenre) {
        Album album = getAlbumByName(albumName);
        if (album != null) {
            album.setTitle(newTitle);
            album.setGenre(newGenre);
            System.out.println("Album updated successfully!");
        } else {
            System.out.println("Album not found!");
        }
    }

    private Album getAlbumByName(String name) {
        return albums.stream()
                .filter(a -> a.getTitle().equals(name))
                .findFirst()
                .orElse(null);
    }

    private Song getSingleByName(String name) {
        return singles.stream()
                .filter(s -> s.getTitle().equals(name))
                .findFirst()
                .orElse(null);
    }

    public void setFollowersCount(int followersCount) {
        if (followersCount >= 0) {
            this.followersCount = followersCount;
        } else {
            throw new IllegalArgumentException("Followers count cannot be negative");
        }
    }

    public int getAlbumCount() {
        return albums.size();
    }

    public void loadFromArtistInfo(ArtistInfo info) {
        if (info == null) return;

        this.followersCount = info.getFollowersCount();

        this.albums.clear();
        this.singles.clear();

        if (info.getAlbumDetails() != null) {
            for (ArtistInfo.AlbumDetails albumDetail : info.getAlbumDetails()) {
                Album album = this.createAlbum(albumDetail.getTitle(), "Unknown Date", albumDetail.getGenre());
                if (album != null && albumDetail.getTracks() != null) {
                    for (ArtistInfo.SongDetails track : albumDetail.getTracks()) {
                        Song song = new Song(
                                track.getTitle(),
                                this.getName(),
                                track.getLyrics(),
                                track.getGenre(),
                                track.getReleaseDate()
                        );

                        if (track.getComments() != null) {
                            for (Comment comment : track.getComments()) {
                                song.addComment(comment.getUsername(), comment.getCommentText());
                            }
                        }
                        album.addSong(song);
                    }
                }
            }
        }

        if (info.getSingleDetails() != null) {
            for (ArtistInfo.SongDetails singleDetail : info.getSingleDetails()) {
                this.createSingle(
                        singleDetail.getTitle(),
                        singleDetail.getLyrics(),
                        singleDetail.getGenre(),
                        singleDetail.getReleaseDate()
                );
            }
        }
    }

    @Override
    public String toString() {
        return "Artist{" +
                "name='" + getName() + '\'' +
                ", username='" + getUsername() + '\'' +
                ", singlesCount=" + singles.size() +
                ", albumsCount=" + albums.size() +
                ", totalSongs=" + getAllSongs().size() +
                ", totalStreams=" + getTotalStreams() +
                '}';
    }

}
