package org.project.Repository;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.project.SongRelated.LyricEditRequest;

import java.util.*;

public class RegularInfo {
    @JsonProperty("username")
    private String username;

    @JsonProperty("followedArtists")
    private List<String> followedArtists;

    @JsonProperty("lyricEditRequests")
    private List<LyricEditRequest> lyricEditRequests;


    @JsonProperty("recentlyViewedSongs")
    private Queue<String> recentlyViewedSongs;
    private static final int MAX_RECENT_SONGS = 10;

    public RegularInfo() {
        this.followedArtists = new ArrayList<>();
        this.lyricEditRequests = new ArrayList<>();
        this.recentlyViewedSongs = new LinkedList<>();
    }

    public RegularInfo(String username) {
        this();
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<String> getFollowedArtists() {
        return new ArrayList<>(followedArtists);
    }

    public void setFollowedArtists(List<String> followedArtists) {
        this.followedArtists = new ArrayList<>(followedArtists);
    }

    public void followArtist(String artistUsername) {
        if (!followedArtists.contains(artistUsername)) {
            followedArtists.add(artistUsername);
        }
    }

    public void unfollowArtist(String artistUsername) {
        followedArtists.remove(artistUsername);
    }

    public boolean isFollowing(String artistUsername) {
        return followedArtists.contains(artistUsername);
    }

    public List<String> getRecentlyViewedSongs() {
        return new ArrayList<>(recentlyViewedSongs);
    }


    @Override
    public String toString() {
        return "RegularInfo{" +
                "username='" + username + '\'' +
                ", followedArtists=" + followedArtists +
                ", lyricEditRequests=" + lyricEditRequests +
                '}';
    }

}
