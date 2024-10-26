package parser;

import commands.AddAuthorCommand;
import commands.AddMangaCommand;
import commands.AddSalesCommand;
import commands.ByeCommand;
import commands.Command;
import commands.DeleteAuthorCommand;
import commands.DeleteMangaCommand;
import commands.ViewAuthorsCommand;
import commands.ViewMangasCommand;
import exceptions.InvalidCatalogCommandException;
import exceptions.InvalidDeleteCommandException;
import exceptions.MangaArgsWrongOrderException;
import exceptions.NoAuthorProvidedException;
import exceptions.NoMangaProvidedException;
import exceptions.TantouException;

import static constants.Command.BYE_COMMAND;
import static constants.Command.CATALOG_COMMAND;
import static constants.Command.COMMAND_INDEX;
import static constants.Command.SALES_COMMAND;
import static constants.Command.VIEW_COMMAND;
import static constants.Options.AUTHOR_OPTION;
import static constants.Options.MANGA_OPTION;
import static constants.Regex.AUTHOR_OPTION_REGEX;
import static constants.Regex.DELETE_OPTION_REGEX;
import static constants.Regex.EMPTY_REGEX;
import static constants.Regex.MANGA_OPTION_REGEX;
import static constants.Regex.PRICE_OPTION_REGEX;
import static constants.Regex.QUANTITY_OPTION_REGEX;
import static constants.Regex.SPACE_REGEX;

public class Parser {
    public Command getUserCommand(String userInput) throws TantouException {
        String trimmedUserInput = userInput.trim();

        if (trimmedUserInput.isEmpty()) {
            throw new TantouException("Hey! Say something!");
        }

        String[] inputList = getUserInputAsList(trimmedUserInput);

        return switch (inputList[COMMAND_INDEX]) {
        case BYE_COMMAND -> new ByeCommand();
        case CATALOG_COMMAND ->
                processCatalogCommand(trimmedUserInput);
        case VIEW_COMMAND ->
                processViewCommand(trimmedUserInput);
        case SALES_COMMAND ->
                processAddSalesCommand(trimmedUserInput);
        default ->
                throw new TantouException("Invalid command provided!");
        };
    }

    //@@author averageandyyy
    private String[] getUserInputAsList(String userInput) throws TantouException {
        return userInput.split(SPACE_REGEX);
    }

    // Catalog Functions
    //@@author averageandyyy
    /**
     * Processes the catalog command based on the user input.
     * Determines whether the command is an Add or Delete operation based on the presence of the delete flag (`-d`).
     * Further categorizes the command as either related to an Author or Manga and
     * returns the appropriate command object.
     * <p>
     * If the `-d` flag is present, it will return a `DeleteAuthorCommand` or `DeleteMangaCommand` based on
     * the content of the user input.
     * Otherwise, it returns an `AddAuthorCommand` or `AddMangaCommand`.
     *
     * @param userInput the input string provided by the user to be parsed into a command
     * @return a Command object representing the parsed operation (either Add or Delete, for Author or Manga)
     * @throws TantouException if the user input is invalid for either Add or Delete operations
     */
    private Command processCatalogCommand(String userInput) throws TantouException {
        userInput = removeCatalogPrefix(userInput);
        if (isValidDeleteCommand(userInput)) {
            userInput = removeDeleteOption(userInput);
            return processDeleteAuthorMangaCommand(userInput);
        }
        return processAddAuthorMangaCommand(userInput);
    }

    private String removeCatalogPrefix(String userInput) {
        return userInput.replace(CATALOG_COMMAND, EMPTY_REGEX);
    }

    private String removeDeleteOption(String userInput) {
        return userInput.replace(DELETE_OPTION_REGEX, EMPTY_REGEX);
    }

    //@@author averageandyyy
    /**
     * Checks that the -d option is present and at the end of the command
     *
     * @param userInput the text input provided by the user
     * @return true if the -d flag is present, false otherwise
     * @throws TantouException if the user input is invalid at the validation or parsing stage
     */
    private boolean isValidDeleteCommand(String userInput) throws TantouException {
        if (userInput.contains(DELETE_OPTION_REGEX)) {
            if (!userInput.endsWith(DELETE_OPTION_REGEX)) {
                throw new InvalidDeleteCommandException();
            }
            return true;
        }
        return false;
    }

    //@@author averageandyyy
    /**
     * Processes the user input to create an Add command for either a Manga or Author.
     * Determines whether the input corresponds to a valid Manga or Author command
     * and returns the appropriate Add command object.
     * <p>
     * If the input is valid for a Manga command, it returns an `AddMangaCommand`.
     * If the input is valid for an Author command, it returns an `AddAuthorCommand`.
     *
     * @param userInput the input string provided by the user,
     *                  which should specify an Add operation for either Manga or Author
     * @return a Command object representing the Add operation for Manga or Author
     * @throws TantouException if the user input is invalid for both Manga and Author Add commands
     */
    private Command processAddAuthorMangaCommand(String userInput) throws TantouException {
        if (isValidMangaCommand(userInput)) {
            String[] authorAndMangaNames = getAuthorAndMangaFromInput(userInput);
            return new AddMangaCommand(authorAndMangaNames);
        } else if (isValidAuthorCommand(userInput)) {
            String authorName = getAuthorNameFromInput(userInput);
            return new AddAuthorCommand(authorName);
        }

        throw new InvalidCatalogCommandException();
    }

    //@@author averageandyyy
    /**
     * Processes the user input to create a Delete command for either a Manga or Author.
     * Determines whether the input corresponds to a valid Manga or Author command
     * and returns the appropriate Delete command object.
     * <p>
     * If the input is valid for a Manga command, it returns an `DeleteMangaCommand`.
     * If the input is valid for an Author command, it returns an `DeleteAuthorCommand`.
     *
     * @param userInput the input string provided by the user,
     *                  which should specify an Add operation for either Manga or Author
     * @return a Command object representing the Add operation for Manga or Author
     * @throws TantouException if the user input is invalid for both Manga and Author Add commands
     */
    private Command processDeleteAuthorMangaCommand(String userInput) throws TantouException {
        if (isValidMangaCommand(userInput)) {
            String[] authorAndMangaNames = getAuthorAndMangaFromInput(userInput);
            return new DeleteMangaCommand(authorAndMangaNames);
        } else if (isValidAuthorCommand(userInput)) {
            String authorName = getAuthorNameFromInput(userInput);
            return new DeleteAuthorCommand(authorName);
        }

        throw new InvalidDeleteCommandException();
    }

    //@@author averageandyyy
    private boolean isValidAuthorCommand(String userInput) throws TantouException {
        return hasAuthorFlagAndArgument(userInput);
    }

    //@@author averageandyyy
    private boolean isValidMangaCommand(String userInput) throws TantouException {
        return hasAuthorFlagAndArgument(userInput) &&
                hasMangaFlagAndArgument(userInput) &&
                isAuthorBeforeManga(userInput);
    }

    //@@author averageandyyy
    private boolean hasAuthorFlagAndArgument(String userInput) throws TantouException {
        // Input contains " -a" but not " -a ", the second space is needed to indicate an incoming argument
        if (userInput.contains(SPACE_REGEX + AUTHOR_OPTION) && !userInput.contains(AUTHOR_OPTION_REGEX)) {
            throw new NoAuthorProvidedException();
        }
        return userInput.contains(AUTHOR_OPTION_REGEX);
    }

    //@@author averageandyyy
    private boolean hasMangaFlagAndArgument(String userInput) throws TantouException {
        // Input contains " -m" but not " -m ", the second space is needed to indicate an incoming argument
        if (userInput.contains(SPACE_REGEX + MANGA_OPTION) && !userInput.contains(MANGA_OPTION_REGEX)) {
            throw new NoMangaProvidedException();
        }
        return userInput.contains(MANGA_OPTION_REGEX);
    }

    private boolean isAuthorBeforeManga(String userInput) throws TantouException {
        int indexOfAuthor = userInput.indexOf(AUTHOR_OPTION_REGEX);
        int indexOfManga = userInput.indexOf(MANGA_OPTION_REGEX);

        if (indexOfAuthor == -1 || indexOfManga == -1) {
            throw new TantouException("No author or manga option found!");
        }

        if (indexOfAuthor > indexOfManga) {
            throw new MangaArgsWrongOrderException();
        }

        return indexOfAuthor < indexOfManga;
    }

    //@@author averageandyyy
    private String getAuthorNameFromInput(String userInput) throws TantouException {
        if (!userInput.contains(AUTHOR_OPTION_REGEX)) {
            throw new TantouException("You have not provided an author argument!");
        }
        return userInput.replace(AUTHOR_OPTION_REGEX, EMPTY_REGEX).trim();
    }

    private String[] getAuthorAndMangaFromInput(String userInput) throws TantouException {
        int indexOfAuthor = userInput.indexOf(AUTHOR_OPTION_REGEX);
        int indexOfManga = userInput.indexOf(MANGA_OPTION_REGEX);

        // Should have been caught at the validation stage
        assert (indexOfAuthor != -1 && indexOfManga != -1) : "Invalid manga command format";
        assert (indexOfAuthor < indexOfManga) : "Invalid manga command format";

        String authorName = extractAuthorName(userInput, indexOfAuthor, indexOfManga);
        String mangaName = extractMangaName(userInput, indexOfManga);

        if (authorName.isEmpty()) {
            throw new NoAuthorProvidedException();
        }

        if (mangaName.isEmpty()) {
            throw new NoMangaProvidedException();
        }

        return new String[]{authorName, mangaName};
    }

    // Sales Functions
    //@@author sarahchow03
    /**
     * Processes the user input to create a Sales command for either an Author.
     * <p>
     * If the input is a valid sales command, it returns an `DeleteMangaCommand`.
     *
     * @param userInput the input string provided by the user,
     *                  which should include the author, manga, quantity sold, and unit price.
     * @return a Command object that adds the sales data for the specified manga.
     * @throws TantouException if the user input is missing a parameter.
     */
    private Command processAddSalesCommand(String userInput) throws TantouException {
        userInput = removeSalesPrefix(userInput);
        if (isValidSalesCommand(userInput)) {
            String[] salesArguments = getSalesArguments(userInput);
            return new AddSalesCommand(salesArguments);
        }
        throw new TantouException("Invalid sales command provided!"
                + " You need to provide the author, manga, quantity sold, and unit price.");
    }

    //@@author averageandyyy
    private String removeSalesPrefix(String userInput) {
        return userInput.replace(SALES_COMMAND, EMPTY_REGEX);
    }

    //@@author sarahchow03
    private boolean isValidSalesCommand(String userInput) throws TantouException {
        return hasSalesFlags(userInput) && areSalesFlagsInOrder(userInput);
    }

    //@@author averageandyyy
    private boolean hasSalesFlags(String userInput) {
        return userInput.contains(AUTHOR_OPTION_REGEX) && userInput.contains(MANGA_OPTION_REGEX)
                && userInput.contains(PRICE_OPTION_REGEX) && userInput.contains(QUANTITY_OPTION_REGEX);
    }

    //@@author averageandyyy
    private boolean areSalesFlagsInOrder(String userInput) throws TantouException {
        int indexOfAuthor = userInput.indexOf(AUTHOR_OPTION_REGEX);
        int indexOfManga = userInput.indexOf(MANGA_OPTION);
        int indexOfQuantity = userInput.indexOf(QUANTITY_OPTION_REGEX);
        int indexOfPrice = userInput.indexOf(PRICE_OPTION_REGEX);


        if (!(indexOfAuthor < indexOfManga && indexOfManga < indexOfPrice && indexOfQuantity < indexOfPrice)) {
            // To be refined
            throw new TantouException("Check the order of your arguments! -a -m -p -q");
        }

        return true;
    }

    //@@author averageandyyy
    private String[] getSalesArguments(String userInput) {
        int indexOfAuthor = userInput.indexOf(AUTHOR_OPTION_REGEX);
        int indexOfManga = userInput.indexOf(MANGA_OPTION_REGEX);
        int indexOfQuantity = userInput.indexOf(QUANTITY_OPTION_REGEX);
        int indexOfPrice = userInput.indexOf(PRICE_OPTION_REGEX);


        // Should have been caught at the validation stage
        assert (indexOfAuthor != -1 && indexOfManga != -1
                && indexOfPrice != -1 && indexOfQuantity != -1) : "Invalid sales command format";
        assert (indexOfAuthor < indexOfManga && indexOfManga < indexOfPrice
                && indexOfQuantity < indexOfPrice) : "Invalid sales command format";

        String authorName = extractAuthorName(userInput, indexOfAuthor, indexOfManga);
        String mangaName = extractMangaName(userInput, indexOfManga, indexOfQuantity);
        String quantitySold = extractQuantity(userInput, indexOfQuantity, indexOfPrice);
        String unitPrice = extractPrice(userInput, indexOfPrice);

        return new String[]{authorName, mangaName, quantitySold, unitPrice};
    }

    //@@author xenthm
    // View Functions
    private Command processViewCommand(String userInput) throws TantouException {
        userInput = removeViewPrefix(userInput);
        if (isValidViewMangasCommand(userInput)) {
            String authorName = getAuthorNameFromInput(userInput);
            return new ViewMangasCommand(authorName);
        }

        return new ViewAuthorsCommand();
    }

    private String removeViewPrefix(String userInput) {
        return userInput.replace(VIEW_COMMAND, EMPTY_REGEX);
    }

    private boolean isValidViewMangasCommand(String userInput) {
        // Check for the author option
        return userInput.contains(AUTHOR_OPTION_REGEX) || userInput.contains(AUTHOR_OPTION);
    }

    //@@author
    // Argument extraction functions
    private String extractAuthorName(String userInput, int indexOfAuthor, int indexOfManga) {
        assert userInput.contains(AUTHOR_OPTION_REGEX) : "Must have author option";
        return userInput.substring(indexOfAuthor, indexOfManga).replace(AUTHOR_OPTION, EMPTY_REGEX).trim();
    }

    private String extractMangaName(String userInput, int indexOfManga) {
        return userInput.substring(indexOfManga).replace(MANGA_OPTION_REGEX, EMPTY_REGEX).trim();
    }

    private String extractMangaName(String userInput, int indexOfManga, int nextOptionIndex) {
        return userInput.substring(indexOfManga, nextOptionIndex).replace(MANGA_OPTION_REGEX, EMPTY_REGEX).trim();
    }

    private String extractQuantity(String userInput, int indexOfQuantity, int nextOptionIndex) {
        return userInput.substring(indexOfQuantity, nextOptionIndex).replace(QUANTITY_OPTION_REGEX, EMPTY_REGEX).trim();
    }

    private String extractPrice(String userInput, int indexOfPrice) {
        return userInput.substring(indexOfPrice).replace(PRICE_OPTION_REGEX, EMPTY_REGEX).trim();
    }
}
