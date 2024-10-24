package commands;

import author.Author;
import author.AuthorList;
import exceptions.TantouException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ui.Ui;

import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

//@@author sarahchow03
class DeleteAuthorCommandTest {
    private final PrintStream standardOut = System.out;
    private AuthorList authorList;
    private DeleteAuthorCommand commandUnderTest;
    private Ui ui;

    @BeforeEach
    public void setUp() {
        authorList = new AuthorList();
        ui = new Ui();
        Author author = new Author("test1");
        authorList.add(author);
    }

    @Test
    public void deleteAuthorCommand_deleteSingleAuthor_authorCountZero() {
        try {
            String authorName = "test1";
            commandUnderTest = new DeleteAuthorCommand(authorName);
            commandUnderTest.execute(ui, authorList);
            assertEquals(0, authorList.size());
        } catch (TantouException e) {
            // The code should not fail at this point
            fail();
        } finally {
            System.setOut(standardOut);
        }
    }

    @Test
    public void deleteAuthorCommand_deleteNonExistingAuthor_authorDoesNotExistsExceptionThrown() {
        try {
            String authorName = "test1";
            commandUnderTest = new DeleteAuthorCommand(authorName);
            commandUnderTest.execute(ui, authorList);
            // A TantouException should be thrown when an author that does not exist is deleted
            Exception exception = assertThrows(TantouException.class, () -> commandUnderTest.execute(ui, authorList));
            System.out.println(exception.getMessage());

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
    public void deleteAuthorCommand_emptyAuthorName_noAuthorProvidedExceptionThrown() {
        try {
            // Simulate no author provided
            // Empty arguments should have been caught at the Parser level
            String authorName = "";
            commandUnderTest = new DeleteAuthorCommand(authorName);
            // A TantouException should be thrown as no author is provided
            Exception exception = assertThrows(TantouException.class, () -> {
                commandUnderTest.execute(ui, authorList);
            });

            assertEquals("No author provided!", exception.getMessage());
        } finally {
            System.setOut(standardOut);
        }
    }
}
