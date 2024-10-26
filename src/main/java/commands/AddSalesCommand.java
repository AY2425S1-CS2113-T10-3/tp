package commands;

import author.Author;
import author.AuthorList;
import exceptions.NoAuthorProvidedException;
import exceptions.NoMangaProvidedException;
import exceptions.PriceTooLargeException;
import exceptions.QuantityTooLargeException;
import exceptions.TantouException;
import manga.Manga;
import sales.Sale;
import ui.Ui;

import static constants.Command.AUTHOR_INDEX;
import static constants.Command.MANGA_INDEX;
import static constants.Command.PRICE_INDEX;
import static constants.Command.QUANTITY_INDEX;
import static constants.Command.SALES_COMMAND;

//@@author sarahchow03

/**
 * Represents the <code>sales</code> command that handles adding sales data to a manga.
 * This command validates the user input, checks if the author and manga exist,
 * and adds sales data (quantity sold and unit price) to the specified manga.
 */
public class AddSalesCommand extends Command {
    private String[] argsAuthorMangaQtyPrice;

    public AddSalesCommand(String[] argsAuthorMangaQtyPrice) {
        super(SALES_COMMAND);
        this.argsAuthorMangaQtyPrice = argsAuthorMangaQtyPrice;
    }

    /**
     * Executes the add sales command by validating user input, checking the existence of the author
     * and manga, and adding the sales data.
     * If any validation fails, a TantouException is thrown with an appropriate error message.
     *
     * @param ui the user interface to interact with the user
     * @param authorList the list of authors where the manga's author is checked
     * @throws TantouException if validation checks fail (e.g., invalid input, missing author or manga)
     */
    @Override
    public void execute(Ui ui, AuthorList authorList) throws TantouException {
        String authorName = argsAuthorMangaQtyPrice[AUTHOR_INDEX];
        String mangaName = argsAuthorMangaQtyPrice[MANGA_INDEX];
        int quantitySold = 0;
        double unitPrice = Double.parseDouble(argsAuthorMangaQtyPrice[PRICE_INDEX]);;
        try {
            quantitySold = Integer.parseInt(argsAuthorMangaQtyPrice[QUANTITY_INDEX]);
            if (quantitySold >= 1000000000) {
                throw new QuantityTooLargeException();
            }
        } catch (NumberFormatException e) {
            throw new QuantityTooLargeException();
        }

        if (quantitySold < 0) {
            throw new TantouException("Quantity sold cannot be less than 0!");
        }

        if (unitPrice > 999999999) {
            throw new PriceTooLargeException();
        }

        if (unitPrice < 0) {
            throw new TantouException("Unit price cannot be less than 0!");
        }

        if (authorName.isEmpty()) {
            logger.warning("No author provided.");
            throw new NoAuthorProvidedException();
        }

        if (mangaName.isEmpty()) {
            logger.warning("No manga provided.");
            throw new NoMangaProvidedException();
        }

        Sale salesData = new Sale(quantitySold, unitPrice);
        Author incomingAuthor = new Author(authorName);
        Manga incomingManga = new Manga(mangaName, incomingAuthor);

        if (!authorList.hasAuthor(incomingAuthor)) {
            throw new TantouException("Author does not exist! You have to add an author first!");
        }

        Author existingAuthor = authorList.getAuthor(incomingAuthor);

        if (!existingAuthor.hasManga(incomingManga)) {
            throw new TantouException("Manga does not exist! This manga needs to exist before adding sales data!");
        }

        existingAuthor.getManga(incomingManga.getMangaName()).addSalesData(salesData);
        System.out.printf("Sales data added for %s %s\n", incomingManga.getMangaName(), salesData);
    }
}
