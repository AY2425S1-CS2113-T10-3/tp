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
            commandUnderTest = new AddMangaCommand("catalog -a \"Kubo Tite\" -m \"Bleach\"");
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
            commandUnderTest = new AddMangaCommand("catalog -a \"Kubo Tite\" -m \"Bleach\"");
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
}
