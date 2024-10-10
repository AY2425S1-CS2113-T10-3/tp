package manga;

import author.Author;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MangaListTest {
    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    @Test
    public void printMangaListFromMangaList_twoMangas() {
        System.setOut(new PrintStream(outputStreamCaptor));
        try {
            Author placeholderAuthor = new Author("placeholderAuthor");
            MangaList mangaList = new MangaList();
            mangaList.add(new Manga("manga1", placeholderAuthor));
            mangaList.add(new Manga("manga2", placeholderAuthor));
            assertEquals(2, mangaList.size());
            mangaList.print();
            assertEquals("1. manga1 | Deadline: None" + System.lineSeparator() + "2. manga2 | Deadline: None" + System.lineSeparator(),
                    outputStreamCaptor.toString());
        } finally {
            System.setOut(standardOut);
        }
    }
}
