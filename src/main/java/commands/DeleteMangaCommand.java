package commands;

import author.Author;
import author.AuthorList;
import exceptions.TantouException;
import manga.Manga;
import storage.Storage;
import ui.Ui;

import java.util.logging.Level;

import static constants.Command.DELETE_COMMAND;

//@@author sarahchow03
/**
 * Represents a command to delete a manga from an author's mangaList.
 * It checks for the existence of the author and manga in the list before deletion.
 * If the author or manga is not found, an exception is thrown.
 *
 * This class extends the Command class and overrides the execute method to handle the delete operation.
 */
public class DeleteMangaCommand extends Command {
    // private static final Logger logger = Logger.getLogger(DeleteMangaCommand.class.getName());
    private String authorName;
    private String mangaName;


    /**
     * Constructs a DeleteMangaCommand with the given user input.
     *
     * @param userInputList The user's input list which consists of the author and manga.
     */
    public DeleteMangaCommand(String[] userInputList) {
        super(DELETE_COMMAND);
        this.authorName = userInputList[0];
        this.mangaName = userInputList[1];
    }

    /**
     * Executes the delete command by conducting several checks on the user input and author list.
     * If the checks are successful, the specified manga is deleted from the author's list.
     *
     * @param ui          The Ui object that handles user interface interactions.
     * @param authorList  The list of authors containing the list of existing authors in the catalog.
     * @throws TantouException If the author or manga does not exist, or if the input is invalid.
     */
    @Override
    public void execute(Ui ui, AuthorList authorList) throws TantouException {
        if (authorName.isEmpty() || mangaName.isEmpty()) {
            logger.log(Level.SEVERE, "Author name or manga name is empty");
            throw new TantouException("No author or manga provided!");
        }
        assert (!authorName.isEmpty() && !mangaName.isEmpty()) : "Author or manga name is empty";
        logger.log(Level.INFO, "Deleting manga... " + mangaName + " from " + authorName);

        Author attachedAuthor = new Author(authorName);
        Manga deletingManga = new Manga(mangaName, attachedAuthor);

        if (authorList.hasAuthor(attachedAuthor)) {
            Author existingAuthor = authorList.getAuthor(attachedAuthor);
            if (existingAuthor.hasManga(deletingManga)) {
                existingAuthor.deleteManga(deletingManga);
                System.out.printf("Manga %s successfully deleted from author %s\n",
                        deletingManga.getMangaName(), existingAuthor.getAuthorName());
                logger.log(Level.INFO, "Successfully deleted manga: " + deletingManga.getMangaName());

                Storage.getInstance().saveAuthorListToDataFile(authorList);
                return;
            }
            assert !existingAuthor.hasManga(deletingManga): "No manga found";
            logger.log(Level.SEVERE, "Manga not found");

            throw new TantouException("Manga does not exist!");
        }
        assert !authorList.hasAuthor(attachedAuthor): "Author not found";
        logger.log(Level.SEVERE, "Author not found");

        throw new TantouException("Author does not exist!");
    }


}


