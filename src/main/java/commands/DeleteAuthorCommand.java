package commands;

import author.Author;
import author.AuthorList;
import exceptions.TantouException;
import storage.Storage;
import ui.Ui;

import java.util.logging.Level;

import static constants.Command.DELETE_COMMAND;

//@@author sarahchow03
/**
 * Represents a command to delete an author from the authorList.
 * It checks for the existence of the author in the list before deletion.
 * If the author is not found, an exception is thrown.
 *
 * This class extends the Command class and overrides the execute method to handle the delete operation.
 */
public class DeleteAuthorCommand extends Command {
    // private static final Logger logger = Logger.getLogger(DeleteAuthorCommand.class.getName());
    private String authorName;

    /**
     * Constructs a DeleteAuthorCommand with the given user input.
     *
     * @param userInputList The user's input list containing the author that is to be deleted.
     */
    public DeleteAuthorCommand(String authorName) {
        super(DELETE_COMMAND);
        this.authorName = authorName;
    }

    /**
     * Executes the delete command by conducting several checks on the user input and author list.
     * If the checks are successful, the specified author is deleted from the list.
     *
     * @param ui         The Ui object that handles user interface interactions.
     * @param authorList The list of authors from which the author will be deleted from.
     * @throws TantouException If the author does not exist or if the input is invalid.
     */
    @Override
    public void execute(Ui ui, AuthorList authorList) throws TantouException {

        if (authorName.isEmpty()) {
            logger.log(Level.SEVERE, "Author name cannot be empty");
            throw new TantouException("No author provided!");
        }
        assert !authorName.isEmpty(): "Author name is empty";
        logger.log(Level.INFO, "Deleting author... " + authorName);

        Author deletingAuthor = new Author(authorName);

        if (authorList.hasAuthor(deletingAuthor)) {
            authorList.remove(deletingAuthor);
            System.out.println("Bye bye~");
            System.out.printf("Successfully deleted author: %s\n", deletingAuthor.getAuthorName());
            logger.log(Level.INFO, "Successfully deleted author: " + deletingAuthor.getAuthorName());

            Storage.getInstance().saveAuthorListToDataFile(authorList);
            return;
        }
        assert !authorList.hasAuthor(deletingAuthor): "Author not found";
        logger.log(Level.SEVERE, "Author not found");

        throw new TantouException("Author does not exist!");
    }

}
