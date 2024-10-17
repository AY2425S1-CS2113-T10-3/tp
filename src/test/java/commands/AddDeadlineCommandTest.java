package commands;

import author.AuthorList;
import exceptions.TantouException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ui.Ui;

import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class AddDeadlineCommandTest {
    private final PrintStream standardOut = System.out;
    private AuthorList authorList;
    private Ui ui;
    private AddDeadlineCommand commandUnderTest;

    @BeforeEach
    public void setUp() {
        authorList = new AuthorList();
        ui = new Ui();
    }

    @Test
    public void addDeadlineCommand_addSingleDeadline_deadlineMatchAuthorCountOne() {
        try {
            commandUnderTest = new AddDeadlineCommand("add -a \"Gege Akutami\" " +
                    "-m \"Jujutsu Kaisen\" -b \"September 29\"");
            commandUnderTest.execute(ui, authorList);
            assertEquals(1, authorList.size());
            assertEquals("September 29", authorList.getAuthor("Gege Akutami")
                    .getMangaList().get(0).getDeadline());
        } catch (TantouException e) {
            // The code should not fail at this point
            fail();
        } finally {
            System.setOut(standardOut);
        }
    }
}
