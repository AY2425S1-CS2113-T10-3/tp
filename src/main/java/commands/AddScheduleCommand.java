package commands;

import author.Author;
import author.AuthorList;
import exceptions.TantouException;
import manga.Manga;
import storage.Storage;
import ui.Ui;

import java.util.logging.Level;

import static constants.Command.CATALOG_COMMAND;

//@@author iaso1774
public class AddScheduleCommand extends Command{
    private String userInput;

    public AddScheduleCommand(String userInput) {
        super(CATALOG_COMMAND);
        this.userInput = userInput;
    }

    @Override
    public void execute(Ui ui, AuthorList authorList) throws TantouException {
        // Empty user input should have been caught at the Parser level
        assert !(userInput.isEmpty()) : "No user input provided";

        String authorName = parser.getAuthorNameFromInput(userInput);
        String mangaName = parser.getMangaNameFromInput(userInput);
        String deadline = parser.getDeadlineDateFromInput(userInput);

        if (deadline.isEmpty() || mangaName.isEmpty() || authorName.isEmpty()) {
            logger.warning("No schedule date, author, or manga provided.");
            throw new TantouException("No schedule date, author, or manga provided!");
        }

        Author incomingAuthor = new Author(authorName);
        Manga incomingManga = new Manga(mangaName, incomingAuthor);

        // If author doesn't exist, create them
        if (!authorList.hasAuthor(authorName)) {
            logger.log(Level.INFO, "Author not found, creating new author " + authorName);
            authorList.add(incomingAuthor);
        }
        assert authorList.hasAuthor(incomingAuthor) : "Author is missing";
        Author existingAuthor = authorList.getAuthor(incomingAuthor);

        // If manga doesn't exist, create it and add the deadline
        if (!existingAuthor.hasManga(incomingManga)) {
            logger.log(Level.INFO, "Manga not found, creating new manga " + mangaName);
            existingAuthor.addManga(incomingManga);
        }
        assert authorList.getAuthor(authorName).hasManga(incomingManga) : "Manga is missing";
        existingAuthor.getManga(incomingManga.getMangaName()).addDeadline(deadline);

        // Assert that the addition successfully executed
        assert authorList.getAuthor(authorName).getManga(mangaName)
                .getDeadline().equals(deadline) : "Deadline was not added";
        logger.log(Level.INFO, "Deadline added to manga " + mangaName);
        System.out.printf("Deadline %s added successfully to manga %s\n",
                deadline, incomingManga.getMangaName());

        Storage.getInstance().saveAuthorListToDataFile(authorList);
    }
}
