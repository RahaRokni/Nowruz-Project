package org.project.SongRelated;

import java.util.ArrayList;
import java.util.List;

public class Album {
    private String title;
    private final String artist;
    private String releaseDate;
    private List<Song> trackList;
    private String genre;

    public Album(String title, String artist, String releaseDate, String genre) {
        this.title = title;
        this.artist = artist;
        this.releaseDate = releaseDate;
        this.genre = genre;
        this.trackList = new ArrayList<>();
    }

    public void addSong(Song song) {
        if (song != null && song.getArtist().equals(this.artist)) {
            trackList.add(song);
        } else {
            throw new IllegalArgumentException("Song must belong to the album's artist");
        }
    }

    public boolean removeSong(Song song) {
        return trackList.remove(song);
    }


    public int getTotalViews() {
        return trackList.stream().mapToInt(Song::getViews).sum();
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public List<Song> getTrackList() {
        return new ArrayList<>(trackList);
    }

    public String getGenre() {
        return genre;
    }

    public void setTitle(String title) {
        if (title != null && !title.trim().isEmpty()) {
            this.title = title;
        }
    }

    public void setReleaseDate(String releaseDate) {
        if (releaseDate != null && !releaseDate.trim().isEmpty()) {
            this.releaseDate = releaseDate;
        }
    }

    public void setGenre(String genre) {
        if (genre != null && !genre.trim().isEmpty()) {
            this.genre = genre;
        }
    }

    @Override
    public String toString() {
        return "Album{" +
                "title='" + title + '\'' +
                ", artist='" + artist + '\'' +
                ", releaseDate='" + releaseDate + '\'' +
                ", genre='" + genre + '\'' +
                ", trackCount=" + trackList.size() +
                '}';
    }

    public boolean containsSong(Song song) {
        return trackList.contains(song);
    }

    public int getTrackCount() {
        return trackList.size();
    }
}