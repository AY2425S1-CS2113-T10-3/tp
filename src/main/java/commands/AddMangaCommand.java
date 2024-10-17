package commands;

import author.Author;
import author.AuthorList;
import exceptions.TantouException;
import manga.Manga;
import ui.Ui;

import static constants.Command.CATALOG_COMMAND;

public class AddMangaCommand extends Command {
    private String userInput;

    public AddMangaCommand(String userInput) {
        super(CATALOG_COMMAND);
        this.userInput = userInput;
    }

    @Override
    public void execute(Ui ui, AuthorList authorList) throws TantouException {
        // Empty user input should have been caught at the Parser level
        assert !(userInput.isEmpty()) : "No user input provided";

        String authorName = parser.getAuthorNameFromInput(userInput);
        String mangaName = parser.getMangaNameFromInput(userInput);

        if (authorName.isEmpty() || mangaName.isEmpty()) {
            logger.warning("No author or manga provided!");
            throw new TantouException("No author or manga provided!");
        }

        Author incomingAuthor = new Author(authorName);
        Manga incomingManga = new Manga(mangaName, incomingAuthor);

        // If author exists add manga to the existing author
        if (authorList.hasAuthor(incomingAuthor)) {
            // Obtain the same Author object in authorList
            Author existingAuthor = authorList.getAuthor(incomingAuthor);
            if (!existingAuthor.hasManga(incomingManga)) {
                existingAuthor.addManga(incomingManga);
                System.out.printf("Manga %s added successfully to author %s\n",
                        incomingManga.getMangaName(), existingAuthor.getAuthorName());

                // Assert that the manga was successfully added
                assert authorList.getAuthor(incomingAuthor).hasManga(incomingManga) : "Failed to add manga";

                return;
            }

            // Exception is thrown when adding manga that already exists with author
            assert authorList.getAuthor(incomingAuthor).hasManga(incomingManga) : "Manga does not actually exist!";
            logger.info("Manga already exists!");
            throw new TantouException("Manga already exists!");
        }

        // Otherwise create new Author and add Manga to it
        authorList.addAuthor(incomingAuthor);
        incomingAuthor.addManga(incomingManga);
        assert authorList.getAuthor(incomingAuthor).hasManga(incomingManga) : "Failed to add author and manga";
        System.out.printf("Manga %s added successfully to author %s\n", incomingManga.getMangaName(),
                incomingAuthor.getAuthorName());
    }
}
