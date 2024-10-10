package commands;

import author.Author;
import exceptions.TantouException;
import manga.Manga;
import ui.Ui;
import author.AuthorList;

import static constants.Command.DEADLINE_COMMAND;

public class AddDeadlineCommand extends Command {
    private String userInput;

    public AddDeadlineCommand(String userInput) {
        super(DEADLINE_COMMAND);
        this.userInput = userInput;
    }

    @Override
    public void execute(Ui ui, AuthorList authorList) throws TantouException {
        String authorName = parser.getAuthorNameFromInput(userInput);
        String mangaName = parser.getMangaNameFromInput(userInput);
        String deadline = parser.getDeadlineDateFromInput(userInput);

        if (deadline.isEmpty() || mangaName.isEmpty() || authorName.isEmpty()) {
            throw new TantouException("No deadline date, author, or manga provided!");
        }

        Author incomingAuthor = new Author(authorName);
        Manga incomingManga = new Manga(mangaName, incomingAuthor);

        // If author exists, find the manga
        if (authorList.hasAuthor(authorName)) {
            // Obtain the same Author object in authorList
            Author existingAuthor = authorList.getAuthor(incomingAuthor);
            // If manga exists, add the deadline
            if (existingAuthor.hasManga(incomingManga)) {
                existingAuthor.getManga(incomingManga.getMangaName()).addDeadline(deadline);
                System.out.printf("Deadline %s added successfully to manga %s\n",
                        deadline, incomingManga.getMangaName());
                return;
            }

            // Exception thrown if the manga doesn't exist
            throw new TantouException("Manga doesn't exist!");
        }

        // Exception thrown if the author doesn't exist
        throw new TantouException("Author doesn't exist!");
    }
}
