package ru.mipt1c;

import ru.mipt1c.KeyValueStorage;
import ru.mipt1c.MalformedDataException;

public abstract class KeyValueStorageFactories {
    protected abstract KeyValueStorage<String, String> buildStringsStorage(String path) throws MalformedDataException;

    protected abstract KeyValueStorage<Integer, Double> buildNumbersStorage(String path) throws MalformedDataException;

    protected abstract KeyValueStorage<StudentKey, Student> buildPojoStorage(String path) throws MalformedDataException;
}
