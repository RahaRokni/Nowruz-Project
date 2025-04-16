package org.project.SongRelated;

import java.util.Objects;

public class LyricEditRequest {
    private String songTitle;
    private String originalLyrics;
    private String suggestedLyrics;
    private String requesterUsername;
    private boolean approved;

    public LyricEditRequest(String songTitle, String originalLyrics,
                            String suggestedLyrics, String requesterUsername) {
        this.songTitle = songTitle;
        this.originalLyrics = originalLyrics;
        this.suggestedLyrics = suggestedLyrics;
        this.requesterUsername = requesterUsername;
        this.approved = false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LyricEditRequest that = (LyricEditRequest) o;
        return Objects.equals(songTitle, that.songTitle) &&
                Objects.equals(requesterUsername, that.requesterUsername);
    }

    @Override
    public int hashCode() {
        return Objects.hash(songTitle, requesterUsername);
    }

    public String getSongTitle() { return songTitle; }
    public String getOriginalLyrics() { return originalLyrics; }
    public String getSuggestedLyrics() { return suggestedLyrics; }
    public String getRequesterUsername() { return requesterUsername; }
    public boolean isApproved() { return approved; }
    public void setApproved(boolean approved) { this.approved = approved; }

    public String getSongArtist() {
        return songTitle;
    }
}