package org.project.Repository;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import com.fasterxml.jackson.databind.ObjectMapper;


public abstract class BaseRepository<T, ID> {
    private String fileName;
    private Class<T> type;
    private Map<ID, T> storage;
    private ObjectMapper objectMapper;

    public BaseRepository(String fileName, Class<T> type) {
        this.fileName = fileName;
        this.type = type;
        this.storage = new HashMap<>();
        this.objectMapper = new ObjectMapper();
        loadFromFile();
    }

    protected abstract ID getId(T entity);

    private void loadFromFile() {
        try {
            File file = new File(fileName);
            if (file.exists() && file.length() > 0) {
                List<T> list = objectMapper.readValue(file,
                        objectMapper.getTypeFactory().constructCollectionType(List.class, type));
                storage.clear();
                for (T entity : list) {
                    storage.put(getId(entity), entity);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading from file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void save(T entity) {
        storage.put(getId(entity), entity);
        saveToFile();
    }

    private void saveToFile() {
        try {
            objectMapper.writeValue(new File(fileName), new ArrayList<>(storage.values()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<T> findAll() {
        return new ArrayList<>(storage.values());
    }

    public T findById(ID id) {
        return storage.get(id);
    }

    public void delete(ID id) {
        storage.remove(id);
        saveToFile();
    }

    public void update(T entity) {
        storage.put(getId(entity), entity);
        saveToFile();
    }

}
