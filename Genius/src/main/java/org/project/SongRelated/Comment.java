package org.project.SongRelated;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Comment {
    private final String username;
    private final String commentText;
    private final LocalDateTime date;

    @JsonCreator
    public Comment(@JsonProperty("username") String username,
                   @JsonProperty("commentText") String commentText,
                   @JsonProperty("date") LocalDateTime date) {
        this.username = username;
        this.commentText = commentText;
        this.date = date != null ? date : LocalDateTime.now();
    }

    public Comment(String username, String commentText) {
        this(username, commentText, LocalDateTime.now());
    }

    public String getUsername() { return username; }
    public String getCommentText() { return commentText; }
    public LocalDateTime getDate() { return date; }

    public String getFormattedDate() {
        return date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }

    @Override
    public String toString() {
        return String.format("[%s] %s: %s", getFormattedDate(), username, commentText);
    }
}