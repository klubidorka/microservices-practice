package ru.mipt1c;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class KeyValueStorageImpl<K, V> implements KeyValueStorage<K, V> {

    private final static String fileOnDiscName = "keyValueStorageBin";
    private final String fullPath;

    private enum State {
        ACTIVE,
        CLOSED,
    }

    private State storageState;

    private final Map<K, V> internalStorage;

    public KeyValueStorageImpl(File dir) throws MalformedDataException {
        File file = Paths.get(dir.getPath(), fileOnDiscName).toFile();
        fullPath = file.getPath();
        internalStorage = file.exists() ? storageFromExistingFile(file) : storageFromNewFile(file);
        storageState = State.ACTIVE;
    }

    public KeyValueStorageImpl(String pathToDir) throws MalformedDataException {
        this(new File(pathToDir));
    }

    @Override
    public V read(K key) {
        checkIfStateAppropriate();
        return internalStorage.get(key);
    }

    @Override
    public boolean exists(K key) {
        checkIfStateAppropriate();
        return internalStorage.containsKey(key);
    }

    @Override
    public void write(K key, V value) {
        checkIfStateAppropriate();
        internalStorage.put(key, value);
    }

    @Override
    public void delete(K key) {
        checkIfStateAppropriate();
        internalStorage.remove(key);
    }

    @Override
    public Iterator<K> readKeys() {
        checkIfStateAppropriate();
        return internalStorage.keySet().iterator();
    }

    @Override
    public int size() {
        checkIfStateAppropriate();
        return internalStorage.size();
    }

    @Override
    public void flush() {
        checkIfStateAppropriate();
        try (FileOutputStream out = new FileOutputStream(fullPath); ObjectOutputStream objOut = new ObjectOutputStream(out)) {
            objOut.writeObject(internalStorage);
        } catch (IOException e) {
            e.printStackTrace();
        }
        storageState = State.CLOSED;
    }

    @Override
    public void close() {
        flush();
    }

    private Map<K, V> storageFromNewFile(File file) throws MalformedDataException {
        try {
            boolean created = file.createNewFile();
            if (!created)
                throw new MalformedDataException("Failed to create file");
            return new HashMap<>();
        } catch (IOException e) {
            throw new MalformedDataException(e);
        }
    }

    private Map<K, V> storageFromExistingFile(File file) throws MalformedDataException {
        try (InputStream in = Files.newInputStream(file.toPath()); ObjectInputStream objectInput = new ObjectInputStream(in)) {
            Object fromFile = objectInput.readObject();
            if (fromFile instanceof HashMap)
                return (HashMap<K, V>) fromFile;
            else
                throw new MalformedDataException();
        } catch (ClassNotFoundException | IOException e) {
            throw new MalformedDataException(e);
        }
    }

    private void checkIfStateAppropriate() {
        if (storageState != State.ACTIVE)
            throw new IllegalStateException("Storage is closed");
    }
}
