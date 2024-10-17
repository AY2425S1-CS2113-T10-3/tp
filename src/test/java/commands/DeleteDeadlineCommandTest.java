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

public class DeleteDeadlineCommandTest {
    private final PrintStream standardOut = System.out;
    private AuthorList authorList;
    private Ui ui;
    private AddDeadlineCommand commandUnderTest;
    private DeleteDeadlineCommand secondCommandUnderTest;

    @BeforeEach
    public void setUp() {
        authorList = new AuthorList();
        ui = new Ui();
    }

    @Test
    public void deleteDeadlineCommand_deleteSingleDeadline_deadlineMatchAuthorCountOne() {
        try {
            commandUnderTest = new AddDeadlineCommand("add -a \"Gege Akutami\" " +
                    "-m \"Jujutsu Kaisen\" -b \"September 29\"");
            commandUnderTest.execute(ui, authorList);
            secondCommandUnderTest = new DeleteDeadlineCommand("delete -a \"Gege Akutami\"" +
                    " -m \"Jujutsu Kaisen\" -b \"September 29\"");
            secondCommandUnderTest.execute(ui, authorList);
            assertEquals(1, authorList.size());
            assertEquals("None", authorList.getAuthor("Gege Akutami")
                    .getMangaList().get(0).getDeadline());
        } catch (TantouException e) {
            // The code should not fail at this point
            fail();
        } finally {
            System.setOut(standardOut);
        }
    }

    @Test
    public void deleteDeadlineCommand_authorDoesntExist_missingInfoExceptionThrown() {
        try {
            commandUnderTest = new AddDeadlineCommand("add -a \"Gege Akutami\" " +
                    "-m \"Jujutsu Kaisen\" -b \"September 29\"");
            commandUnderTest.execute(ui, authorList);
            secondCommandUnderTest = new DeleteDeadlineCommand("delete -a \"Oda\"" +
                    " -m \"One Piece\" -b \"September 29\"");
            // A TantouException should be thrown when a duplicate author tries to be added
            Exception exception = assertThrows(TantouException.class, () -> {
                secondCommandUnderTest.execute(ui, authorList);
            });

            assertEquals("Author doesn't exist!", exception.getMessage());
        } catch (TantouException e) {
            // The code should not fail at this point
            fail();
        } finally {
            System.setOut(standardOut);
        }
    }
}
