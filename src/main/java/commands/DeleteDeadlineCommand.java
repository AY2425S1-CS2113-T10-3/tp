package commands;

import author.Author;
import exceptions.TantouException;
import ui.Ui;
import author.AuthorList;

import java.util.Scanner;

import static constants.Command.DEADLINE_COMMAND;

public class DeleteDeadlineCommand extends Command {
    private String userInput;

    public DeleteDeadlineCommand(String userInput) {
        super(DEADLINE_COMMAND);
        this.userInput = userInput;
    }

    @Override
    public void execute(Ui ui, AuthorList authorList) throws TantouException {
        String authorName = parser.getAuthorNameFromInput(userInput);
        String mangaName = parser.getMangaNameFromInput(userInput);

        if (authorName.isEmpty() || mangaName.isEmpty()) {
            throw new TantouException("No author or manga provided!");
        }

        Author incomingAuthor = new Author(authorName);

        // If author exists, find the manga
        if (authorList.hasAuthor(authorName)) {
            // Obtain the same Author object in authorList
            Author existingAuthor = authorList.getAuthor(incomingAuthor);
            // If manga exists, add the deadline
            if (existingAuthor.getManga(mangaName) != null) {
                existingAuthor.getManga(mangaName).deleteDeadline();
                System.out.printf("Deadline successfully deleted from manga %s\n",
                        mangaName);
                return;
            }

            // Exception thrown if the manga doesn't exist
            throw new TantouException("Manga doesn't exist!");
        }

        // Exception thrown if the author doesn't exist
        throw new TantouException("Author doesn't exist!");
    }
}
