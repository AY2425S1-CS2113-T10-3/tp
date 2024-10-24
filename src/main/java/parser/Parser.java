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
import exceptions.TantouException;

import static constants.Command.BYE_COMMAND;
import static constants.Command.CATALOG_COMMAND;
import static constants.Command.COMMAND_INDEX;
import static constants.Command.SALES_COMMAND;
import static constants.Command.VIEW_COMMAND;
import static constants.Options.AUTHOR_OPTION;
import static constants.Options.MANGA_OPTION;
import static constants.Options.PRICE_OPTION;
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
        String[] inputList = getUserInputAsList(trimmedUserInput);

        if (inputList.length == 0) {
            throw new TantouException("Hey! Say something!");
        }

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

//        switch (inputList[COMMAND_INDEX]) {
//        case BYE_COMMAND:
//            return new ByeCommand();
//        case CATALOG_COMMAND:
//            // Returns either a Add or Delete command, which will either be Author or Manga related
//            return processCatalogCommand(inputList);
//        //@@author xenthm
//        case VIEW_COMMAND:
//            if (isValidViewAuthorsCommand(inputList)) {
//                return new ViewAuthorsCommand();
//            }
//            if (isValidViewMangaCommand(inputList)) {
//                return new ViewMangasCommand(inputList);
//            }
//            throw new TantouException("Invalid view command provided!");
//        //@@author sarahchow03
//        case DELETE_COMMAND:
//            if (isValidDeadlineCommand(inputList)) {
//                return new DeleteDeadlineCommand(inputList);
//            } else if (isValidMangaCommand(inputList)) {
//                return new DeleteMangaCommand(inputList);
//            } else if (isValidAuthorCommand(inputList)) {
//                return new DeleteAuthorCommand(inputList);
//            }
//            throw new TantouException("Invalid delete command provided!");
//        case SALES_COMMAND:
//            return processAddSalesCommand(inputList);
//        default:
//            throw new TantouException("Invalid command provided!");
//        }
    }


    //@@author averageandyyy
    public String[] getUserInputAsList(String userInput) throws TantouException {
        return userInput.split(SPACE_REGEX);
    }

    //@@author averageandyyy
    public String getAuthorNameFromInput(String userInput) throws TantouException {
        if (!userInput.contains(AUTHOR_OPTION_REGEX)) {
            throw new TantouException("You have not provided an author argument!");
        }
        return userInput.replace(AUTHOR_OPTION_REGEX, EMPTY_REGEX).trim();
    }

    //@@author averageandyyy
//    public String getMangaNameFromInput(String[] userInputList) throws TantouException {
//        try {
//            command = ownParser.parse(options, userInputList);
//            return command.getOptionValue(constants.Options.MANGA_OPTION);
//        } catch (ParseException e) {
//            throw new TantouException(String.format("Something went wrong when parsing: %s", e.getMessage()));
//        }
//    }

    //@@author
//    public String getDeadlineDateFromInput(String[] userInputList) throws TantouException {
//        try {
//            command = ownParser.parse(options, userInputList);
//            return command.getOptionValue(constants.Options.BY_DATE_OPTION);
//        } catch (ParseException e) {
//            throw new TantouException(String.format("Something went wrong when parsing: %s", e.getMessage()));
//        }
//    }

    //@@author sarahchow03

    /**
     * Parses the user input to extract the quantity sold.
     * It checks the format of the input and returns the quantity as an integer.
     *
     * @param userInputList The user's input list containing the quantity sold information.
     * @return The quantity sold as an integer.
     * @throws TantouException If the input format is incorrect, or if the quantity is not a valid integer.
     */
//    public int getQuantitySoldFromInput(String[] userInputList) throws TantouException {
//        try {
//            command = ownParser.parse(options, userInputList);
//            return Integer.parseInt(command.getOptionValue(constants.Options.QUANTITY_OPTION));
//        } catch (ParseException e) {
//            throw new TantouException(String.format("Something went wrong when parsing: %s", e.getMessage()));
//        } catch (NumberFormatException e) {
//            throw new TantouException(String.format("Please enter a whole number for quantity sold!"));
//        }
//    }

    //@@author sarahchow03

    /**
     * Parses the user input to extract the unit price of the item.
     * It checks the format of the input and returns the unit price as a double.
     *
     * @param userInputList The user's input list containing the unit price information.
     * @return The unit price as a double.
     * @throws TantouException If the input format is incorrect, or if the price is not a valid double.
     */
//    public double getUnitPriceFromInput(String[] userInputList) throws TantouException {
//        try {
//            command = ownParser.parse(options, userInputList);
//            return Double.parseDouble(command.getOptionValue(constants.Options.PRICE_OPTION));
//        } catch (ParseException e) {
//            throw new TantouException(String.format("Something went wrong when parsing: %s", e.getMessage()));
//        } catch (NumberFormatException e) {
//            throw new TantouException(String.format("Please enter a number for unit price!"));
//        }
//    }

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
     * @param userInputList the input list provided by the user to be parsed into a command
     * @return a Command object representing the parsed operation (either Add or Delete, for Author or Manga)
     * @throws TantouException if the user input is invalid for either Add or Delete operations
     */
    public Command processCatalogCommand(String userInput) throws TantouException {
        userInput = removeCatalogPrefix(userInput);
        if (isValidDeleteCommand(userInput)) {
            userInput = removeDeleteOption(userInput);
            return processDeleteAuthorMangaCommand(userInput);
        }
        return processAddAuthorMangaCommand(userInput);
    }

    public String removeCatalogPrefix(String userInput) {
        return userInput.replace(CATALOG_COMMAND, EMPTY_REGEX);
    }

    public String removeDeleteOption(String userInput) {
        return userInput.replace(DELETE_OPTION_REGEX, EMPTY_REGEX);
    }

    //@@author averageandyyy

    /**
     * Checks for the presence of the -d flag
     *
     * @param userInputList the text input provided by the user in list form
     * @return true if the -d flag is present, false otherwise
     * @throws TantouException if the user input is invalid at the validation or parsing stage
     */
    public boolean isValidDeleteCommand(String userInput) throws TantouException {
        if (userInput.contains(DELETE_OPTION_REGEX)) {
            if (!userInput.endsWith(DELETE_OPTION_REGEX)) {
                throw new TantouException("Invalid delete command format! Please place \"-d\" at the end of the command!");
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
     * @param userInputList the input list provided by the user,
     *                      which should specify an Add operation for either Manga or Author
     * @return a Command object representing the Add operation for Manga or Author
     * @throws TantouException if the user input is invalid for both Manga and Author Add commands
     */
    public Command processAddAuthorMangaCommand(String userInput) throws TantouException {
        if (isValidMangaCommand(userInput)) {
            String[] authorAndMangaNames = getAuthorAndMangaFromInput(userInput);
            return new AddMangaCommand(authorAndMangaNames);
        } else if (isValidAuthorCommand(userInput)) {
            String authorName = getAuthorNameFromInput(userInput);
            return new AddAuthorCommand(authorName);
        }

        throw new TantouException("Invalid catalog command provided!");
    }

    //@@author averageandyyy

    /**
     * Processes the user input to create an Delete command for either a Manga or Author.
     * Determines whether the input corresponds to a valid Manga or Author command
     * and returns the appropriate Delete command object.
     * <p>
     * If the input is valid for a Manga command, it returns an `DeleteMangaCommand`.
     * If the input is valid for an Author command, it returns an `DeleteAuthorCommand`.
     *
     * @param userInputList the input list provided by the user,
     *                      which should specify an Add operation for either Manga or Author
     * @return a Command object representing the Add operation for Manga or Author
     * @throws TantouException if the user input is invalid for both Manga and Author Add commands
     */
    public Command processDeleteAuthorMangaCommand(String userInput) throws TantouException {
        if (isValidMangaCommand(userInput)) {
            String[] authorAndMangaNames = getAuthorAndMangaFromInput(userInput);
            return new DeleteMangaCommand(authorAndMangaNames);
        } else if (isValidAuthorCommand(userInput)) {
            String authorName = getAuthorNameFromInput(userInput);
            return new DeleteAuthorCommand(authorName);
        }
        throw new TantouException("Invalid delete command provided!");
    }

    //@@author sarahchow03

    /**
     * Processes the user input to create a Sales command for either an Author.
     * <p>
     * If the input is a valid sales command, it returns an `DeleteMangaCommand`.
     *
     * @param userInputList the input list provided by the user,
     *                      which should include the author, manga, quantity sold, and unit price.
     * @return a Command object that adds the sales data for the specified manga.
     * @throws TantouException if the user input is missing a parameter.
     */
    public Command processAddSalesCommand(String userInput) throws TantouException {
        userInput = removeSalesPrefix(userInput);
        if (isValidSalesCommand(userInput)) {
            String[] salesArguments = getSalesArguments(userInput);
            return new AddSalesCommand(salesArguments);
        }
        throw new TantouException("Invalid sales command provided!"
                + " You need to provide the author, manga, quantity sold, and unit price.");
    }

    public String removeSalesPrefix(String userInput) {
        return userInput.replace(SALES_COMMAND, EMPTY_REGEX);
    }

    //@@author averageandyyy
    public boolean isValidAuthorCommand(String userInput) throws TantouException {
        if (userInput.contains(SPACE_REGEX + AUTHOR_OPTION) && !userInput.contains(AUTHOR_OPTION_REGEX)) {
            throw new TantouException("You have not provided an author argument!");
        }
        return userInput.contains(AUTHOR_OPTION_REGEX);
    }

    //@@author averageandyyy
    public boolean isValidMangaCommand(String userInput) throws TantouException {
        return hasAuthorAndMangaFlags(userInput) && isAuthorBeforeManga(userInput);
    }

    public boolean hasAuthorAndMangaFlags(String userInput) throws TantouException {
        if (userInput.contains(SPACE_REGEX + MANGA_OPTION) && !userInput.contains(MANGA_OPTION_REGEX)) {
         throw new TantouException("You have not provided a manga argument!");
        }
        return userInput.contains(AUTHOR_OPTION_REGEX) && userInput.contains(MANGA_OPTION_REGEX);
    }

    public boolean isAuthorBeforeManga(String userInput) throws TantouException {
        int indexOfAuthor = userInput.indexOf(AUTHOR_OPTION_REGEX);
        int indexOfManga = userInput.indexOf(MANGA_OPTION_REGEX);

        if (indexOfAuthor == -1 || indexOfManga == -1) {
            throw new TantouException("No author or manga option found!");
        }

        if (indexOfAuthor > indexOfManga) {
            throw new TantouException("Specify the author before the manga!");
        }

        return indexOfAuthor < indexOfManga;
    }

    public String[] getAuthorAndMangaFromInput(String userInput) throws TantouException {
        int indexOfAuthor = userInput.indexOf(AUTHOR_OPTION_REGEX);
        int indexOfManga = userInput.indexOf(MANGA_OPTION_REGEX);

        // Should have been caught at the validation stage
        assert (indexOfAuthor != -1 && indexOfManga != -1) : "Invalid manga command format";
        assert (indexOfAuthor < indexOfManga) : "Invalid manga command format";

        String authorName = extractAuthorName(userInput, indexOfAuthor, indexOfManga);
        String mangaName = extractMangaName(userInput, indexOfManga);

        if (authorName.isEmpty()) {
            throw new TantouException("You have not provided an author argument!");
        }

        if (mangaName.isEmpty()) {
            throw new TantouException("You have not provided a manga!");
        }

        return new String[]{authorName, mangaName};
    }

    public String extractAuthorName(String userInput, int indexOfAuthor, int indexOfManga) {
        assert userInput.contains(AUTHOR_OPTION_REGEX) : "Must have author option";
        return userInput.substring(indexOfAuthor, indexOfManga).replace(AUTHOR_OPTION, EMPTY_REGEX).trim();
    }

    public String extractMangaName(String userInput, int indexOfManga) {
        return userInput.substring(indexOfManga).replace(MANGA_OPTION_REGEX, EMPTY_REGEX).trim();
    }

    public String extractMangaName(String userInput, int indexOfManga, int nextOptionIndex) {
        return userInput.substring(indexOfManga, nextOptionIndex).replace(MANGA_OPTION_REGEX, EMPTY_REGEX).trim();
    }

    public String extractQuantity(String userInput, int indexOfQuantity, int nextOptionIndex) {
        return userInput.substring(indexOfQuantity, nextOptionIndex).replace(QUANTITY_OPTION_REGEX, EMPTY_REGEX).trim();
    }

    public String extractPrice(String userInput, int indexOfPrice) {
        return userInput.substring(indexOfPrice).replace(PRICE_OPTION_REGEX, EMPTY_REGEX).trim();
    }

    public Command processViewCommand(String userInput) throws TantouException {
        userInput = removeViewPrefix(userInput);
        if (isValidViewMangasCommand(userInput)) {
            String authorName = getAuthorNameFromInput(userInput);
            return new ViewMangasCommand(authorName);
        }

        return new ViewAuthorsCommand();
    }

    public String removeViewPrefix(String userInput) {
        return userInput.replace(VIEW_COMMAND, EMPTY_REGEX);
    }

    //@@author xenthm
    private boolean isValidViewMangasCommand(String userInput) {
//        try {
//            command = ownParser.parse(options, userInputList);
//            return command.hasOption(constants.Options.AUTHOR_OPTION);
//        } catch (ParseException e) {
//            throw new TantouException(String.format("Something went wrong when parsing: %s", e.getMessage()));
//        }
        // Check for the author option
        return userInput.contains(AUTHOR_OPTION_REGEX) || userInput.contains(AUTHOR_OPTION);
    }

//    private boolean isValidViewAuthorsCommand(String[] userInputList) throws TantouException {
//        // A valid view authors command should NOT have constants.Options.AUTHOR_OPTION in it
//        return !isValidViewMangaCommand(userInputList);
//    }

    //@@author iaso1774
//    private boolean isValidDeadlineCommand(String[] inputList) throws TantouException {
//        try {
//            command = ownParser.parse(options, inputList);
//            return command.hasOption(constants.Options.BY_DATE_OPTION)
//                    && command.hasOption(constants.Options.AUTHOR_OPTION)
//                    && command.hasOption(constants.Options.MANGA_OPTION);
//        } catch (ParseException e) {
//            throw new TantouException(String.format("Something went wrong when parsing: %s", e.getMessage()));
//        }
//    }

    //@@author sarahchow03
    private boolean isValidSalesCommand(String userInput) throws TantouException {
//        try {
//            command = ownParser.parse(options, userInputList);
//            return command.hasOption(constants.Options.QUANTITY_OPTION)
//                    && command.hasOption(constants.Options.PRICE_OPTION)
//                    && command.hasOption(constants.Options.AUTHOR_OPTION)
//                    && command.hasOption(constants.Options.MANGA_OPTION);
//        } catch (ParseException e) {
//            throw new TantouException(String.format("Something went wrong when parsing: %s", e.getMessage()));
//        }
        // Have flags and flags are in correct order
        return hasSalesFlags(userInput) && areSalesFlagsInOrder(userInput);
    }

    //@@author averageandyyy
    public boolean hasSalesFlags(String userInput) {
        return userInput.contains(AUTHOR_OPTION_REGEX) && userInput.contains(MANGA_OPTION_REGEX)
                && userInput.contains(PRICE_OPTION_REGEX) && userInput.contains(QUANTITY_OPTION_REGEX);
    }

    //@@author averageandyyy
    public boolean areSalesFlagsInOrder(String userInput) throws TantouException {
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
    public String[] getSalesArguments(String userInput) {
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
}
