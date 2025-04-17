package org.project.Repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AdminRepository {
    private static final String REQUESTS_FILE = "artist_requests.json";
    private final ObjectMapper objectMapper;
    private List<ArtistRequest> pendingRequests;

    public AdminRepository() {
        this.objectMapper = new ObjectMapper();
        this.pendingRequests = loadRequests();
    }

    private List<ArtistRequest> loadRequests() {
        File file = new File(REQUESTS_FILE);
        if (file.exists()) {
            try {
                return objectMapper.readValue(file,
                        objectMapper.getTypeFactory().constructCollectionType(List.class, ArtistRequest.class));
            } catch (IOException e) {
                System.err.println("Error loading artist requests: " + e.getMessage());
            }
        }
        return new ArrayList<>();
    }

    private void saveRequests() {
        try {
            objectMapper.writeValue(new File(REQUESTS_FILE), pendingRequests);
        } catch (IOException e) {
            System.err.println("Error saving artist requests: " + e.getMessage());
        }
    }

    public void addArtistRequest(ArtistRequest request) {
        pendingRequests.add(request);
        saveRequests();
    }

    public List<ArtistRequest> getPendingRequests() {
        return new ArrayList<>(pendingRequests);
    }

    public void removeRequest(String username) {
        pendingRequests.removeIf(req -> req.getUsername().equals(username));
        saveRequests();
    }
}