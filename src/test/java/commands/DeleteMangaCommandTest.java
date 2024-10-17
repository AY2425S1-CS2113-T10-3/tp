package commands;

import author.Author;
import author.AuthorList;
import exceptions.TantouException;
import manga.Manga;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ui.Ui;

import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

class DeleteMangaCommandTest {
    private final PrintStream standardOut = System.out;
    private AuthorList authorList;
    private DeleteMangaCommand commandUnderTest;
    private Ui ui;
    private Author author;

    @BeforeEach
    public void setUp() {
        authorList = new AuthorList();
        ui = new Ui();
        author = new Author("test1");
        Manga manga = new Manga("test1", author);
        authorList.addAuthor(author);
        author.addManga(manga);
    }

    @Test
    public void deleteMangaCommand_deleteSingleManga_mangaCountZero() {
        try {
            commandUnderTest = new DeleteMangaCommand("catalog -d -a \"test1\" -m \"test1\"");
            commandUnderTest.execute(ui, authorList);
            assertEquals(0, author.getMangaList().size());
        } catch (TantouException e) {
            // The code should not fail at this point
            fail();
        } finally {
            System.setOut(standardOut);
        }
    }

    @Test
    public void deleteMangaCommand_deleteNonExistingManga_mangaDoesNotExistsExceptionThrown() {
        try {
            commandUnderTest = new DeleteMangaCommand("catalog -d -a \"test1\" -m \"test1\"");
            commandUnderTest.execute(ui, authorList);
            // A TantouException should be thrown when an author that does not exist is deleted
            Exception exception = assertThrows(TantouException.class, () -> commandUnderTest.execute(ui, authorList));
            System.out.println(exception.getMessage());

            assertEquals("Manga does not exist!", exception.getMessage());
        } catch (TantouException e) {
            // The code should not fail at this point
            fail();
        } finally {
            System.setOut(standardOut);
        }
    }

}
