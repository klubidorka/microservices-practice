package ru.mipt1c;

public class SingleFileStorageTest extends AbstractSingleFileStorageTest{
    @Override
    protected KeyValueStorage<String, String> buildStringsStorage(String path) throws MalformedDataException {
        return new KeyValueStorageImpl<>(path);
    }

    @Override
    protected KeyValueStorage<Integer, Double> buildNumbersStorage(String path) throws MalformedDataException {
        return new KeyValueStorageImpl<>(path);
    }

    @Override
    protected KeyValueStorage<StudentKey, Student> buildPojoStorage(String path) throws MalformedDataException {
        return new KeyValueStorageImpl<>(path);
    }
}
