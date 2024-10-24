package parser;

import commands.AddAuthorCommand;
import commands.AddMangaCommand;
import commands.ByeCommand;
import commands.Command;
import commands.DeleteAuthorCommand;
import commands.DeleteMangaCommand;
import commands.ViewAuthorsCommand;
import commands.ViewMangasCommand;
import exceptions.TantouException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

//@@author xenthm
public class ParserTest {
    private Parser parser;

    @BeforeEach
    public void setUp() {
        parser = new Parser();
    }

    //@@author averageandyyy
    @Test
    public void getUserCommand_byeTantou_parsedCorrectly() {
        parseInputAssertCommandType("bye", ByeCommand.class);
    }

    //@@author xenthm
    @Test
    public void getUserCommand_viewAuthors_parsedCorrectly() {
        parseInputAssertCommandType("view", ViewAuthorsCommand.class);
    }

    @Test
    public void getUserCommand_viewMangas_parsedCorrectly() {
        parseInputAssertCommandType("view -a \"test\"", ViewMangasCommand.class);
    }

    //@@author sarahchow03
    @Test
    public void getUserCommand_deleteAuthor_parsedCorrectly() {
        parseInputAssertCommandType("catalog -d -a \"test\"", DeleteAuthorCommand.class);
    }

    @Test
    public void getUserCommand_deleteManga_parsedCorrectly() {
        parseInputAssertCommandType("catalog -d -a \"test\" -m \"test\"", DeleteMangaCommand.class);
    }

    //@@author averageandyyy
    @Test
    public void getUserCommand_addAuthor_parsedCorrectly() {
        parseInputAssertCommandType("catalog -a \"test\"", AddAuthorCommand.class);
    }

    //@@author averageandyyy
    @Test
    public void getUserCommand_addManga_parsedCorrectly() {
        parseInputAssertCommandType("catalog -a \"test\" -m \"test\"", AddMangaCommand.class);
    }

    //@@author xenthm
    private void parseInputAssertCommandType(String input, Class<? extends Command> expectedClass) {
        try {
            Command actual = parser.getUserCommand(input);
            assertEquals(actual.getClass(), expectedClass);
        } catch (TantouException e) {
            // Should not reach here as the input should be valid for testing
            fail();
        }
    }

    //@@author averageandyyy
    @Test
    public void getAuthorNameFromInput_validName_nameMatch() {
        try {
            // At this point, the string would have had the catalog prefix removed
            String processedUserInput = " -a Kubo Tite";
            String authorName = parser.getAuthorNameFromInput(processedUserInput);
            assertEquals(authorName, "Kubo Tite");
        } catch (TantouException e) {
            // The code should not fail at this point
            fail();
        }
    }

    //@@author averageandyyy
//    @Test
//    public void getAuthorNameFromInput_invalidOption_parseExceptionThrown() {
//        // Simulate a ParseException with an invalid option that leads to a TantouException
//        String[] userInputList = {"-x", ""};
//        assertThrows(TantouException.class, () -> {
//            parser.getAuthorNameFromInput(userInputList);
//        });
//    }
    // No longer using Default Parser

    // Function not in use
//    //@@author averageandyyy
//    @Test
//    public void getMangaNameFromInput_validName_nameMatch() {
//        try {
//            String[] userInputList = {"-m", "Bleach"};
//            String authorName = parser.getMangaNameFromInput(userInputList);
//            assertEquals(authorName, "Bleach");
//        } catch (TantouException e) {
//            // The code should not fail at this point
//            fail();
//        }
//    }

//    //@@author averageandyyy
//    @Test
//    public void getMangaNameFromInput_invalidOption_parseExceptionThrown() {
//        // Simulate a ParseException with an invalid option that leads to a TantouException
//        String[] userInputList = {"-x", ""};
//        assertThrows(TantouException.class, () -> {
//            parser.getMangaNameFromInput(userInputList);
//        });
//    }
}
