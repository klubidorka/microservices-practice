package ru.mipt1c;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.Iterator;

@Component
public class KeyValueStorageImplV2<K, V> implements KeyValueStorage<K, V> {

    @Autowired
    Repository repository;

    private enum State {
        ACTIVE,
        CLOSED,
    }

    private State storageState = State.ACTIVE;


    @Override
    public V read(K key) {
        checkIfStateAppropriate();
        Wrapper wrapper = repository.findByMyKey(toByteArray(key));
        if (wrapper == null)
            return null;
        return (V) fromByteArray(wrapper.myValue);
    }

    @Override
    public boolean exists(K key) {
        checkIfStateAppropriate();
        return repository.existsByMyKey(toByteArray(key));
    }

    @Override
    public void write(K key, V value) {
        checkIfStateAppropriate();
        repository.save(new Wrapper(toByteArray(key), toByteArray(value)));
    }

    @Override
    public void delete(K key) {
        checkIfStateAppropriate();
        Wrapper wrapper = repository.findByMyKey(toByteArray(key));
        if (wrapper != null)
            repository.delete(wrapper);
    }

    @Override
    public Iterator<K> readKeys() {
        checkIfStateAppropriate();
        Iterator<Wrapper> iterator = repository.findAll().iterator();

        return new Iterator<>() {
            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public K next() {
                if (!iterator.hasNext())
                    return null;
                return (K) fromByteArray(iterator.next().myKey);
            }
        };
    }

    @Override
    public int size() {
        checkIfStateAppropriate();
        return (int) repository.count();
    }

    @Override
    public void flush() {
        checkIfStateAppropriate();
        repository.flush();
        storageState = State.CLOSED;
    }

    @Override
    public void close() {
        flush();
    }

    private void checkIfStateAppropriate() {
        if (storageState != State.ACTIVE)
            throw new IllegalStateException("Storage is closed");
        if (repository == null)
            throw new IllegalStateException("Repository is null");
    }

    private byte[] toByteArray(Object obj) {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(obj);
            oos.flush();
            return bos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Object fromByteArray(byte[] bytes) {
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(bis);
            return ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
