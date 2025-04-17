package org.project.Repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.project.SongRelated.LyricEditRequest;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class RegularInfoRepository {
    private static final String JSON_FILE = "regular_users_info.json";
    private final ObjectMapper objectMapper;
    private Map<String, RegularInfo> regularInfoMap;

    public RegularInfoRepository() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        this.regularInfoMap = loadRegularInfo();
    }

    private Map<String, RegularInfo> loadRegularInfo() {
        File file = new File(JSON_FILE);
        if (file.exists()) {
            try {
                return objectMapper.readValue(file,
                        objectMapper.getTypeFactory().constructMapType(
                                HashMap.class, String.class, RegularInfo.class));
            } catch (IOException e) {
                System.err.println("Error loading regular users info: " + e.getMessage());
            }
        }
        return new HashMap<>();
    }

    private void saveRegularInfo() {
        try {
            objectMapper.writeValue(new File(JSON_FILE), regularInfoMap);
        } catch (IOException e) {
            System.err.println("Error saving regular users info: " + e.getMessage());
        }
    }

    public RegularInfo getRegularInfo(String username) {
        return regularInfoMap.get(username);
    }

    public void saveRegularInfo(RegularInfo info) {
        if (info == null || info.getUsername() == null) {
            throw new IllegalArgumentException("RegularInfo or username cannot be null");
        }
        regularInfoMap.put(info.getUsername(), info);
        saveRegularInfo();
    }

    public void updateFollowedArtists(String username, String artistUsername, boolean follow) {
        RegularInfo info = regularInfoMap.getOrDefault(username, new RegularInfo(username));
        if (follow) {
            info.followArtist(artistUsername);
        } else {
            info.unfollowArtist(artistUsername);
        }
        regularInfoMap.put(username, info);
        saveRegularInfo();
    }

    public void addLyricEditRequest(String username, LyricEditRequest request) {
        RegularInfo info = regularInfoMap.getOrDefault(username, new RegularInfo(username));
        info.addLyricEditRequest(request);
        regularInfoMap.put(username, info);
        saveRegularInfo();
    }

    public List<LyricEditRequest> getPendingLyricEditsForArtist(String artistUsername) {
        return regularInfoMap.values().stream()
                .flatMap(info -> info.getLyricEditRequests().stream())
                .filter(req -> req.getSongTitle().startsWith(artistUsername + "/"))
                .collect(Collectors.toList());
    }


    public void removeLyricEditRequest(String requesterUsername, LyricEditRequest request) {

        RegularInfo regularInfo = getRegularInfo(requesterUsername);

        if (regularInfo != null) {

            List<LyricEditRequest> requests = regularInfo.getLyricEditRequests();

            if (requests != null) {

                requests.removeIf(req ->
                        req.getSongTitle().equals(request.getSongTitle()) &&
                                req.getRequesterUsername().equals(request.getRequesterUsername()) &&
                                req.getOriginalLyrics().equals(request.getOriginalLyrics())
                );


                saveRegularInfo(regularInfo);
            }
        }
    }

    public List<LyricEditRequest> getAllPendingLyricEdits() {
        List<LyricEditRequest> allPendingEdits = new ArrayList<>();


        List<RegularInfo> allRegularInfos = getAllRegularInfos();


        for (RegularInfo regularInfo : allRegularInfos) {
            if (regularInfo.getLyricEditRequests() != null) {
                allPendingEdits.addAll(regularInfo.getLyricEditRequests());
            }
        }

        return allPendingEdits;
    }

    private List<RegularInfo> getAllRegularInfos() {
        File file = new File(JSON_FILE);
        if (file.exists()) {
            try {
                return objectMapper.readValue(file,
                        objectMapper.getTypeFactory().constructCollectionType(List.class, RegularInfo.class));
            } catch (IOException e) {
                System.err.println("Error loading regular infos: " + e.getMessage());
            }
        }
        return new ArrayList<>();
    }
}