package parser;

import commands.AddAuthorCommand;
import commands.AddMangaCommand;
import commands.Command;
import commands.DeleteAuthorCommand;
import commands.DeleteMangaCommand;
import commands.ViewAuthorsCommand;
import commands.ViewMangasCommand;
import exceptions.TantouException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class ParserTest {
    private Parser parser;

    @BeforeEach
    public void setUp() {
        parser = new Parser();
    }

    @Test
    public void getUserCommand_viewAuthors_parsedCorrectly() {
        parseInputAssertCommandType("view", ViewAuthorsCommand.class);
    }

    @Test
    public void getUserCommand_viewMangas_parsedCorrectly() {
        parseInputAssertCommandType("view -a \"test\"", ViewMangasCommand.class);
    }

    @Test
    public void getUserCommand_deleteAuthor_parsedCorrectly() {
        parseInputAssertCommandType("catalog -d -a \"test\"", DeleteAuthorCommand.class);
    }

    @Test
    public void getUserCommand_deleteManga_parsedCorrectly() {
        parseInputAssertCommandType("catalog -d -a \"test\" -m \"test\"", DeleteMangaCommand.class);
    }

    @Test
    public void getUserCommand_addAuthor_parsedCorrectly() {
        parseInputAssertCommandType("catalog -a \"test\"", AddAuthorCommand.class);
    }

    @Test
    public void getUserCommand_addManga_parsedCorrectly() {
        parseInputAssertCommandType("catalog -a \"test\" -m \"test\"", AddMangaCommand.class);
    }

    private void parseInputAssertCommandType(String input, Class<? extends Command> expectedClass) {
        try {
            Command actual = parser.getUserCommand(input);
            assertEquals(actual.getClass(), expectedClass);
        } catch (TantouException e) {
            // Should not reach here as the input should be valid for testing
            fail();
        }
    }
}
