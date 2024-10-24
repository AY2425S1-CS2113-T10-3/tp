package commands;

import author.AuthorList;
import exceptions.TantouException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ui.Ui;

import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

//@@author averageandyyy
public class AddMangaCommandTest {
    private final PrintStream standardOut = System.out;
    private AuthorList authorList;
    private Ui ui;
    private AddMangaCommand commandUnderTest;

    @BeforeEach
    public void setUp() {
        authorList = new AuthorList();
        ui = new Ui();
    }

    @Test
    public void addMangaCommand_addOneManga_authorCountOneMangaNameMatch() {
        try {
            String[] userInputList = {"Kubo Tite", "Bleach"};
            commandUnderTest = new AddMangaCommand(userInputList);
            commandUnderTest.execute(ui, authorList);
            assertEquals(1, authorList.size());
            assertEquals("Bleach", authorList.getAuthor("Kubo Tite").getMangaList().get(0).getMangaName());
        } catch (TantouException e) {
            fail();
        } finally {
            System.setOut(standardOut);
        }
    }

    @Test
    public void addMangaCommand_addDuplicateManga_mangaExistsExceptionThrown() {
        try {
            String[] userInputList = {"Kubo Tite", "Bleach"};
            commandUnderTest = new AddMangaCommand(userInputList);
            commandUnderTest.execute(ui, authorList);
            Exception exception = assertThrows(TantouException.class, () -> {
                commandUnderTest.execute(ui, authorList);
            });

            assertEquals("Manga already exists!", exception.getMessage());
        } catch (TantouException e) {
            fail();
        } finally {
            System.setOut(standardOut);
        }
    }

    @Test
    public void addMangaCommand_addMangaToExistingAuthor_mangaAddedSuccess() {
        try {
            String authorName = "Kubo Tite";
            Command addAuthor = new AddAuthorCommand(authorName);
            addAuthor.execute(ui, authorList);
            String[] mangaInputList = {"Kubo Tite", "Bleach"};
            commandUnderTest = new AddMangaCommand(mangaInputList);
            commandUnderTest.execute(ui, authorList);

            assertEquals(1, authorList.size());
            assertEquals("Bleach", authorList.getAuthor("Kubo Tite").getMangaList().get(0).getMangaName());
        } catch (TantouException e) {
            fail();
        } finally {
            System.setOut(standardOut);
        }
    }

    @Test
    public void addMangaCommand_emptyAuthorName_noAuthorOrMangaProvidedExceptionThrown() {
        try {
            // Simulate no author provided
            // Missing arguments should have been caught at the Parser level
            String[] userInputList = {"", "Bleach"};
            commandUnderTest = new AddMangaCommand(userInputList);
            // A TantouException should be thrown as no author is provided
            Exception exception = assertThrows(TantouException.class, () -> {
                commandUnderTest.execute(ui, authorList);
            });

            assertEquals("No author or manga provided!", exception.getMessage());
        } finally {
            System.setOut(standardOut);
        }
    }

    @Test
    public void addMangaCommand_emptyMangaName_noAuthorOrMangaProvidedExceptionThrown() {
        try {
            // Simulate no author provided
            String[] userInputList = {"Kubo Tite", ""};
            commandUnderTest = new AddMangaCommand(userInputList);
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
