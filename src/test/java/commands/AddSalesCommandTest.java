package commands;

import author.Author;
import author.AuthorList;
import exceptions.TantouException;
import manga.Manga;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sales.Sale;
import ui.Ui;

import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

//@@author sarahchow03
class AddSalesCommandTest {
    private final PrintStream standardOut = System.out;
    private AuthorList authorList;
    private Ui ui;
    private Author existingAuthor;
    private Manga existingManga;
    private AddSalesCommand commandUnderTest;

    @BeforeEach
    public void setUp() {
        authorList = new AuthorList();
        ui = new Ui();
        existingAuthor = new Author("test author");
        existingManga = new Manga("test manga", existingAuthor);
        existingAuthor.addManga(existingManga);
        authorList.add(existingAuthor);
    }

    @Test
    public void execute_validInput_addSalesDataSuccessfully() {
        try {
            String[] userInputList = {"sales", "-a", "test author", "-m", "test manga", "-p", "10.90", "-q", "20"};
            commandUnderTest = new AddSalesCommand(userInputList);
            commandUnderTest.execute(ui, authorList);
            Sale sale = existingAuthor.getManga("test manga").getSalesData();
            assertEquals(20, sale.getQuantitySold());
            assertEquals(10.90, sale.getUnitPrice());
        } catch (TantouException e) {
            fail();
        } finally {
            System.setOut(standardOut);
        }
    }

    @Test
    public void execute_negativeQuantity_cannotBeLessThanZeroExceptionThrown() {
        try {
            String[] userInputList = {"sales", "-a", "test author", "-m", "test manga", "-p", "10.90", "-q", "-20"};
            commandUnderTest = new AddSalesCommand(
                    userInputList
            );
            Exception exception = assertThrows(TantouException.class, () -> {
                commandUnderTest.execute(ui, authorList);
            });

            assertEquals("Quantity sold cannot be less than 0!", exception.getMessage());
        } finally {
            System.setOut(standardOut);
        }
    }

    @Test
    public void execute_negativeUnitPrice_cannotBeLessThanZeroExceptionThrown() {
        try {
            String[] userInputList = {"sales", "-a", "test author", "-m", "test manga", "-p", "-10.90", "-q", "20"};
            commandUnderTest = new AddSalesCommand(
                    userInputList
            );
            Exception exception = assertThrows(TantouException.class, () -> {
                commandUnderTest.execute(ui, authorList);
            });

            assertEquals("Unit price cannot be less than 0!", exception.getMessage());
        } finally {
            System.setOut(standardOut);
        }
    }


}
