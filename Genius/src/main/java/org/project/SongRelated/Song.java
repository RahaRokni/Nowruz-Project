package org.project.SongRelated;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Song {
    private String title;
    private String artist;
    private String lyrics;
    private String genre;
    private int views;
    private String releaseDate;
    private List<Comment> comments;

    public Song(String title, String artist, String lyrics, String genre, String releaseDate) {
        this.title = title;
        this.artist = artist;
        this.lyrics = lyrics;
        this.genre = genre;
        this.releaseDate = releaseDate;
        this.comments = new ArrayList<>();
    }

    public void incrementViews() {
        this.views++;
    }

    public int getViews() {
        return this.views;
    }

    public String getLyrics() {
        return this.lyrics;
    }

    public void updateLyrics(String newLyrics) {
        if (newLyrics != null && !newLyrics.trim().isEmpty()) {
            this.lyrics = newLyrics;
        }
    }

    public void appendLyrics(String additionalLyrics) {
        if (additionalLyrics != null && !additionalLyrics.trim().isEmpty()) {
            if (this.lyrics == null) {
                this.lyrics = additionalLyrics;
            } else {
                this.lyrics += "\n" + additionalLyrics;
            }
        }
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public String getGenre() {
        return genre;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void addComment(String username, String commentText) {
        Comment newComment = new Comment(username, commentText);
        comments.add(newComment);
    }

    public List<Comment> getComments() {
        return comments.stream()
                .sorted(Comparator.comparing(Comment::getDate).reversed())
                .collect(Collectors.toList());
    }

    public void displayComments() {
        if (comments.isEmpty()) {
            System.out.println("No comments yet.");
            return;
        }

        System.out.println("\n=== Comments for '" + title + "' ===");
        getComments().forEach(System.out::println);
    }

    @Override
    public String toString() {
        return "Song{" +
                "title='" + title + '\'' +
                ", artist='" + artist + '\'' +
                ", genre='" + genre + '\'' +
                ", views=" + views +
                ", releaseDate='" + releaseDate + '\'' +
                '}';
    }
}
