package ru.mipt1c;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
@EnableJpaRepositories
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class SingleFileStorageTest extends AbstractSingleFileStorageTest {
    @Autowired
    KeyValueStorage<String, String> stringsStorage;

    @Autowired
    KeyValueStorage<Integer, Double> numbersStorage;

    @Autowired
    KeyValueStorage<StudentKey, Student> pojoStorage;

    @Override
    protected KeyValueStorage<String, String> buildStringsStorage(String path) throws MalformedDataException {
        return stringsStorage;
    }

    @Override
    protected KeyValueStorage<Integer, Double> buildNumbersStorage(String path) throws MalformedDataException {
        return numbersStorage;
    }

    @Override
    protected KeyValueStorage<StudentKey, Student> buildPojoStorage(String path) throws MalformedDataException {
        return pojoStorage;
    }
}
