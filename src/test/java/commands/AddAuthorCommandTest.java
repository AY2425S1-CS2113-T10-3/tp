package commands;

import java.io.PrintStream;

import author.AuthorList;
import exceptions.TantouException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ui.Ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

public class AddAuthorCommandTest {
    private final PrintStream standardOut = System.out;
    private AuthorList authorList;
    private Ui ui;
    private AddAuthorCommand commandUnderTest;

    @BeforeEach
    public void setUp() {
        authorList = new AuthorList();
        ui = new Ui();
    }

    @Test
    public void addAuthorCommand_addSingleAuthor_authorCountOne() {
        try {
            commandUnderTest = new AddAuthorCommand("catalog -a \"Kubo Tite\"");
            commandUnderTest.execute(ui, authorList);
            assertEquals(1, authorList.size());
        } catch (TantouException e) {
            // The code should not fail at this point
            fail();
        } finally {
            System.setOut(standardOut);
        }
    }

    @Test
    public void addAuthorCommand_addDuplicateAuthor_authorExistsExceptionThrown() {
        try {
            commandUnderTest = new AddAuthorCommand("catalog -a \"Kubo Tite\"");
            commandUnderTest.execute(ui, authorList);
            // A TantouException should be thrown when a duplicate author tries to be added
            Exception exception = assertThrows(TantouException.class, () -> {
                commandUnderTest.execute(ui, authorList);
            });

            assertEquals("Author exists!", exception.getMessage());
        } catch (TantouException e) {
            // The code should not fail at this point
            fail();
        } finally {
            System.setOut(standardOut);
        }
    }
}
