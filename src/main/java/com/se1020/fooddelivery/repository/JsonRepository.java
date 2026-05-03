package com.se1020.fooddelivery.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.DeserializationFeature;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

// Abstract generic repository — subclasses inherit all CRUD file operations
// T is the model type, concrete repos just supply the file name and class type
public abstract class JsonRepository<T> {

    private static final ConcurrentHashMap<String, Object> FILE_LOCKS = new ConcurrentHashMap<>();

    protected final ObjectMapper mapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    @Value("${data.path}")
    private String dataPath;

    private Object fileLock() {
        return FILE_LOCKS.computeIfAbsent(getFileName(), fn -> new Object());
    }

    // Subclasses must declare what file and what type they work with
    protected abstract String getFileName();
    protected abstract Class<T> getType();
    protected abstract String getId(T entity);
    protected abstract void setId(T entity, String id);

    protected File getFile() {
        File dir = new File(dataPath);
        if (!dir.exists()) dir.mkdirs();
        return new File(dataPath + "/" + getFileName());
    }

    public List<T> findAll() {
        synchronized (fileLock()) {
            File file = getFile();
            if (!file.exists()) return new ArrayList<>();
            try {
                return mapper.readValue(file,
                        mapper.getTypeFactory().constructCollectionType(List.class, getType()));
            } catch (Exception e) {
                return new ArrayList<>();
            }
        }
    }

    public Optional<T> findById(String id) {
        return findAll().stream()
                .filter(entity -> getId(entity).equals(id))
                .findFirst();
    }

    public List<T> findWhere(Predicate<T> predicate) {
        return findAll().stream().filter(predicate).toList();
    }

    public T save(T entity) {
        synchronized (fileLock()) {
            List<T> all = readAllFromDisk();
            if (getId(entity) == null || getId(entity).isEmpty()) {
                setId(entity, java.util.UUID.randomUUID().toString());
            }
            all.add(entity);
            writeAllUnsafe(all);
            return entity;
        }
    }

    public T update(T updated) {
        synchronized (fileLock()) {
            List<T> all = readAllFromDisk();
            for (int i = 0; i < all.size(); i++) {
                if (getId(all.get(i)).equals(getId(updated))) {
                    all.set(i, updated);
                    writeAllUnsafe(all);
                    return updated;
                }
            }
            return null;
        }
    }

    public boolean delete(String id) {
        synchronized (fileLock()) {
            List<T> all = readAllFromDisk();
            boolean removed = all.removeIf(entity -> getId(entity).equals(id));
            if (removed) writeAllUnsafe(all);
            return removed;
        }
    }

    private List<T> readAllFromDisk() {
        File file = getFile();
        if (!file.exists()) return new ArrayList<>();
        try {
            return mapper.readValue(file,
                    mapper.getTypeFactory().constructCollectionType(List.class, getType()));
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    private void writeAllUnsafe(List<T> data) {
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(getFile(), data);
        } catch (Exception e) {
            throw new RuntimeException("Failed to write to " + getFileName(), e);
        }
    }
}