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

//@@author sarahchow03
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
        authorList.add(author);
        author.addManga(manga);
    }

    @Test
    public void deleteMangaCommand_deleteSingleManga_mangaCountZero() {
        try {
            String[] userInputList = {"test1", "test1"};
            commandUnderTest = new DeleteMangaCommand(userInputList);
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
            // Simulate no relevant manga
            String[] userInputList = {"test1", "test1"};
            commandUnderTest = new DeleteMangaCommand(userInputList);
            commandUnderTest.execute(ui, authorList);
            // Delete non-existing manga
            // A TantouException should be thrown when an author that does not exist is deleted
            Exception exception = assertThrows(TantouException.class, () -> commandUnderTest.execute(ui, authorList));

            assertEquals("Manga does not exist!", exception.getMessage());
        } catch (TantouException e) {
            // The code should not fail at this point
            fail();
        } finally {
            System.setOut(standardOut);
        }
    }

    //@@author averageandyyy
    @Test
    public void deleteMangaCommand_deleteFromNonExistingAuthor_mangaDoesNotExistsExceptionThrown() {
        try {
            // Simulate no relevant author
            String authorName = "test1";
            Command deleteAuthor = new DeleteAuthorCommand(authorName);
            deleteAuthor.execute(ui, authorList);
            // Delete manga from non-existing author
            String[] mangaInputList = {"test1", "test1"};
            commandUnderTest = new DeleteMangaCommand(mangaInputList);
            // A TantouException should be thrown when an author that does not exist is deleted
            Exception exception = assertThrows(TantouException.class, () -> commandUnderTest.execute(ui, authorList));

            assertEquals("Author does not exist!", exception.getMessage());
        } catch (TantouException e) {
            // The code should not fail at this point
            fail();
        } finally {
            System.setOut(standardOut);
        }
    }

    //@@author averageandyyy
    @Test
    public void deleteMangaCommand_emptyAuthorName_noAuthorOrMangaProvidedExceptionThrown() {
        try {
            // Simulate no author provided
            String[] userInputList = {"", "Bleach"};
            commandUnderTest = new DeleteMangaCommand(userInputList);
            // A TantouException should be thrown as no author is provided
            Exception exception = assertThrows(TantouException.class, () -> {
                commandUnderTest.execute(ui, authorList);
            });

            assertEquals("No author or manga provided!", exception.getMessage());
        } finally {
            System.setOut(standardOut);
        }
    }

    //@@author averageandyyy
    @Test
    public void deleteMangaCommand_emptyMangaName_noAuthorOrMangaProvidedExceptionThrown() {
        try {
            // Simulate no author provided
            String[] userInputList = {"Kubo Tite", ""};
            commandUnderTest = new DeleteMangaCommand(userInputList);
            // A TantouException should be thrown as no author is provided
            Exception exception = assertThrows(TantouException.class, () -> {
                commandUnderTest.execute(ui, authorList);
            });

            assertEquals("No author or manga provided!", exception.getMessage());
        } finally {
            System.setOut(standardOut);
        }
    }
}
