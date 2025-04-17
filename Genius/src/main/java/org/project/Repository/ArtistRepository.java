package org.project.Repository;

import org.project.Accounts.Artist;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class ArtistRepository {
    private static final String JSON_FILE = "artists_info.json";
    private final ObjectMapper objectMapper;
    private Map<String, ArtistInfo> artistsInfoMap;

    public ArtistRepository() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        this.artistsInfoMap = loadArtistsInfo();
    }

    private Map<String, ArtistInfo> loadArtistsInfo() {
        File file = new File(JSON_FILE);
        if (file.exists()) {
            try {
                return objectMapper.readValue(file,
                        objectMapper.getTypeFactory().constructMapType(HashMap.class, String.class, ArtistInfo.class));
            } catch (IOException e) {
                System.err.println("Error loading artists info: " + e.getMessage());
            }
        }
        return new HashMap<>();
    }

    private void saveArtistsInfo() {
        try {
            objectMapper.writeValue(new File(JSON_FILE), artistsInfoMap);
        } catch (IOException e) {
            System.err.println("Error saving artists info: " + e.getMessage());
        }
    }

    public ArtistInfo getArtistInfo(String username) {
        return artistsInfoMap.get(username);
    }

    public List<ArtistInfo> getAllArtistsInfo() {
        return new ArrayList<>(artistsInfoMap.values());
    }

    public void updateArtistInfo(Artist artist) {
        if (artistsInfoMap.containsKey(artist.getUsername())) {
            saveArtistInfo(artist);
        }
    }

    public void saveArtistInfo(Artist artist) {
        if (artist.getUsername() == null || artist.getName() == null) {
            throw new IllegalArgumentException("Artist name or username cannot be null!");
        }

        ArtistInfo existingInfo = artistsInfoMap.getOrDefault(artist.getUsername(), new ArtistInfo());

        existingInfo.setName(artist.getName());
        existingInfo.setUsername(artist.getUsername());

        ArtistInfo newInfo = ArtistInfo.fromArtist(artist);


        existingInfo.setSingles(newInfo.getSingles() != null ?
                new ArrayList<>(newInfo.getSingles()) : new ArrayList<>());
        existingInfo.setAlbums(newInfo.getAlbums() != null ?
                new ArrayList<>(newInfo.getAlbums()) : new ArrayList<>());
        existingInfo.setAlbumDetails(newInfo.getAlbumDetails() != null ?
                new ArrayList<>(newInfo.getAlbumDetails()) : new ArrayList<>());

        existingInfo.setSongsCount(newInfo.getSongsCount());
        existingInfo.setAlbumsCount(newInfo.getAlbumsCount());
        existingInfo.setFollowersCount(newInfo.getFollowersCount());
        existingInfo.setTotalStreams(newInfo.getTotalStreams());

        existingInfo.setSingleDetails(new ArrayList<>(newInfo.getSingleDetails()));
        existingInfo.setSingles(new ArrayList<>(newInfo.getSingles()));

        artistsInfoMap.put(artist.getUsername(), existingInfo);
        saveArtistsInfo();
    }

    public void updateFollowersCount(String username, int change) {
        ArtistInfo info = artistsInfoMap.get(username);
        if (info != null) {
            info.setFollowersCount(info.getFollowersCount() + change);
            saveArtistsInfo();
        }
    }
    public void saveArtistInfo(ArtistInfo artistInfo) {
        if (artistInfo.getUsername() == null || artistInfo.getName() == null) {
            throw new IllegalArgumentException("Artist name or username cannot be null!");
        }
        artistsInfoMap.put(artistInfo.getUsername(), artistInfo);
        saveArtistsInfo();
    }
    public void updateSongLyrics(String songTitle, String newLyrics) {
        List<ArtistInfo> allArtistInfos = getAllArtistsInfo();

        for (ArtistInfo artistInfo : allArtistInfos) {
            boolean updated = false;

            if (artistInfo.getSingleDetails() != null) {
                for (ArtistInfo.SongDetails single : artistInfo.getSingleDetails()) {
                    if (single.getTitle().equals(songTitle)) {
                        single.setLyrics(newLyrics);
                        updated = true;
                    }
                }
            }

            if (artistInfo.getAlbumDetails() != null) {
                for (ArtistInfo.AlbumDetails album : artistInfo.getAlbumDetails()) {
                    if (album.getTracks() != null) {
                        for (ArtistInfo.SongDetails track : album.getTracks()) {
                            if (track.getTitle().equals(songTitle)) {
                                track.setLyrics(newLyrics);
                                updated = true;
                            }
                        }
                    }
                }
            }

            if (updated) {
                saveArtistInfo(artistInfo);
            }
        }
    }

    }