package commands;

import author.Author;
import author.AuthorList;
import exceptions.TantouException;
import ui.Ui;

import static constants.Command.CATALOG_COMMAND;


public class AddAuthorCommand extends Command {
    private String userInput;

    public AddAuthorCommand(String userInput) {
        super(CATALOG_COMMAND);
        this.userInput = userInput;
    }

    @Override
    public void execute(Ui ui, AuthorList authorList) throws TantouException {
        // Empty user input should have been caught at the Parser level
        assert !(userInput.isEmpty()) : "No user input provided";

        String authorName = parser.getAuthorNameFromInput(userInput);

        if (authorName.isEmpty()) {
            logger.warning("No author provided!");
            throw new TantouException("No author provided!");
        }

        Author incomingAuthor = new Author(authorName);

        if (!authorList.hasAuthor(incomingAuthor)) {
            authorList.addAuthor(incomingAuthor);
            System.out.printf("Successfully added author: %s\n", incomingAuthor.getAuthorName());

            // Assert that the addition was successfully executed
            assert authorList.hasAuthor(incomingAuthor) : "Author is missing";
            assert authorList.getAuthor(incomingAuthor).getAuthorName()
                    .equals(incomingAuthor.getAuthorName()) : "Author was not added";

            return;
        }

        // The existing author must have the same name for the duplicate to be recognized
        assert authorList.getAuthor(incomingAuthor).getAuthorName()
                .equals(incomingAuthor.getAuthorName()) : "Different author recognized as equal!";

        logger.info("Author already exists");
        throw new TantouException("Author exists!");
    }
}
