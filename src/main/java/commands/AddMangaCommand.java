package commands;

import author.Author;
import author.AuthorList;
import exceptions.TantouException;
import manga.Manga;
import storage.Storage;
import ui.Ui;

import static constants.Command.CATALOG_COMMAND;

//@@author averageandyyy
public class AddMangaCommand extends Command {
    private String authorName;
    private String mangaName;

    public AddMangaCommand(String[] userInputList) {
        super(CATALOG_COMMAND);
        this.authorName = userInputList[0];
        this.mangaName = userInputList[1];
    }

    @Override
    public void execute(Ui ui, AuthorList authorList) throws TantouException {
        // Empty user input should have been caught at the Parser level
        assert (authorName != null && mangaName != null) : "No user input provided";

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

                Storage.getInstance().saveAuthorListToDataFile(authorList);
                return;
            }

            // Exception is thrown when adding manga that already exists with author
            assert authorList.getAuthor(incomingAuthor).hasManga(incomingManga) : "Manga does not actually exist!";
            logger.info("Manga already exists!");
            throw new TantouException("Manga already exists!");
        }

        // Otherwise create new Author and add Manga to it
        authorList.add(incomingAuthor);
        incomingAuthor.addManga(incomingManga);
        assert authorList.getAuthor(incomingAuthor).hasManga(incomingManga) : "Failed to add author and manga";
        System.out.printf("Manga %s added successfully to author %s\n", incomingManga.getMangaName(),
                incomingAuthor.getAuthorName());
        Storage.getInstance().saveAuthorListToDataFile(authorList);
    }
}
