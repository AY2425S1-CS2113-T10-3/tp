package storage;

import author.Author;
import author.AuthorList;
import manga.Manga;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertNull;

//@@author xenthm
class StorageTest {
    private static final String TEST_DATA_FILE_NAME = "testCatalog.json";
    private static URI testDataFile;
    private static final Storage STORAGE_TEST_INSTANCE = Storage.getInstance();
    private static final AuthorList AUTHOR_LIST_TEST = new AuthorList();

    private static void setupAuthorListTest() {
        Author author1 = new Author("author1");
        author1.addManga(new Manga("manga1", author1, "deadline1"));
        author1.addManga(new Manga("manga2", author1, "deadline2"));
        Author author2 = new Author("author2");
        author2.addManga(new Manga("manga3", author2, "deadline3"));
        author2.addManga(new Manga("manga4", author2, "deadline4"));
        Author author3 = new Author("author3");
        author3.addManga(new Manga("manga5", author3, "deadline5"));
        author3.addManga(new Manga("manga6", author3, "deadline6"));

        AUTHOR_LIST_TEST.add(author1);
        AUTHOR_LIST_TEST.add(author2);
        AUTHOR_LIST_TEST.add(author3);
    }

    @BeforeAll
    public static void setup() throws URISyntaxException {
        testDataFile = Objects.requireNonNull(
                StorageTest.class
                        .getClassLoader()
                        .getResource(TEST_DATA_FILE_NAME)
        ).toURI();
        STORAGE_TEST_INSTANCE.setDataFile(Paths.get(testDataFile).toFile());
        setupAuthorListTest();
    }

    @Test
    void getInstance_returnsSameSingletonInstanceAcrossMultipleCalls() {
        Storage storage1 = Storage.getInstance();
        Storage storage2 = Storage.getInstance();
        assertSame(storage1, storage2);
    }

    @Test
    void readAuthorListFromDataFile_returnsNullWhenDataFileIsInvalid() {
        File temp = STORAGE_TEST_INSTANCE.getDataFile();
        STORAGE_TEST_INSTANCE.setDataFile(new File("invalid"));
        assertNull(STORAGE_TEST_INSTANCE.readAuthorListFromDataFile());
        STORAGE_TEST_INSTANCE.setDataFile(temp);
    }

    @Test
    void readAuthorListFromDataFile_matchesTestCase() {
        assertEquals(AUTHOR_LIST_TEST, STORAGE_TEST_INSTANCE.readAuthorListFromDataFile());
    }

    @Test
    void toJson_serializesCorrectly() throws IOException {
        StringWriter stringWriter = new StringWriter();
        STORAGE_TEST_INSTANCE
                .getGson()
                .toJson(AUTHOR_LIST_TEST, stringWriter);
        String actualJson = stringWriter.toString()
                .replaceAll("\\r\\n", "\n");
        String expectedJson = new String(Files.readAllBytes(Paths.get(testDataFile)))
                .replaceAll("\\r\\n", "\n");
        assertEquals(expectedJson, actualJson);
    }
}
