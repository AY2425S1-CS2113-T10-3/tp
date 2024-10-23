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
import commands.AddScheduleCommand;
import exceptions.TantouException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import java.util.ArrayList;
import java.util.regex.Matcher;

import static constants.Command.BYE_COMMAND;
import static constants.Command.CATALOG_COMMAND;
import static constants.Command.VIEW_COMMAND;
import static constants.Command.COMMAND_INDEX;
import static constants.Command.DELETE_COMMAND;
import static constants.Command.SALES_COMMAND;
import static constants.Command.SCHEDULE_COMMAND;
import static constants.Options.LONG_OPTION_INDEX;
import static constants.Options.OPTIONS_ARRAY;
import static constants.Options.OPTION_DESC_INDEX;
import static constants.Options.REQUIRE_ARGS_INDEX;
import static constants.Options.REQUIRE_ARGS_TRUE;
import static constants.Options.SHORT_OPTION_INDEX;
import static constants.Regex.USER_COMMAND_REGEX;

public class Parser {
    private CommandLineParser ownParser;
    private Options options;
    private CommandLine command;

    public Parser() {
        this.ownParser = new DefaultParser();
        this.options = new Options();
        initializeOptions();
    }

    //@@author averageandyyy
    /**
     * Initializes command-line options by adding each option defined in the {@link constants.Options#OPTIONS_ARRAY} to
     * the options list.
     *
     * <p>This method iterates over the options defined in the
     * {@link constants.Options#OPTIONS_ARRAY} and adds each option to the internal options collection. Each option is
     * configured with its short name, long name, and description, and is marked as requiring an argument.</p>
     *
     * @see constants.Options#OPTIONS_ARRAY
     * @see #options
     */
    public void initializeOptions() {
        for (String[] option : OPTIONS_ARRAY) {
            this.options.addOption(option[SHORT_OPTION_INDEX], option[LONG_OPTION_INDEX],
                    option[REQUIRE_ARGS_INDEX].equals(REQUIRE_ARGS_TRUE), option[OPTION_DESC_INDEX]);
        }
    }

    public Command getUserCommand(String userInput) throws TantouException {
        userInput = userInput.trim();
        String[] inputList = getUserInputAsList(userInput);

        if (inputList.length == 0) {
            throw new TantouException("Hey! Say something!");
        }

        switch (inputList[COMMAND_INDEX]) {
        case BYE_COMMAND:
            return new ByeCommand();
        case CATALOG_COMMAND:
            // Returns either a Add or Delete command, which will either be Author or Manga related
            return processCatalogCommand(userInput);
        //@@author xenthm
        case VIEW_COMMAND:
            if (isValidViewAuthorsCommand(userInput)) {
                return new ViewAuthorsCommand();
            }
            if (isValidViewMangaCommand(userInput)) {
                return new ViewMangasCommand(userInput);
            }
            throw new TantouException("Invalid view command provided!");
        //@@author sarahchow03
        case DELETE_COMMAND:
            if (isValidMangaCommand(userInput)) {
                return new DeleteMangaCommand(userInput);
            } else if (isValidAuthorCommand(userInput)) {
                return new DeleteAuthorCommand(userInput);
            }
            throw new TantouException("Invalid delete command provided!");
        case SALES_COMMAND:
            return processAddSalesCommand(userInput);
        //@@author iaso1774
        case SCHEDULE_COMMAND:
            if (isValidScheduleCommand(userInput)) {
                return new AddScheduleCommand(userInput);
            }
            throw new TantouException("Invalid schedule command provided!");
        default:
            throw new TantouException("Invalid command provided!");
        }
    }

    //@@author averageandyyy
    /**
     * Parses the user input string into an array of strings, capturing quoted strings and individual words, while
     * validating the argument format.
     *
     * <p>This method uses a regular expression to extract quoted strings
     * (including the quotes) and unquoted words from the provided user input. It then calls
     * {@link #validateArguments(ArrayList)} to ensure that all options have their corresponding arguments in quotes. If
     * any argument is incorrectly formatted, a {@link TantouException} is thrown.</p>
     *
     * @param userInput a String representing the user input containing command-line arguments. It may include options
     *                  and arguments in quoted format.
     * @return array of strings representing the parsed user input, with each option and argument as separate entries.
     * @throws TantouException if any option's argument is not enclosed in quotes or if the input format is invalid.
     */
    public String[] getUserInputAsList(String userInput) throws TantouException {
        Matcher matcher = USER_COMMAND_REGEX.matcher(userInput);
        ArrayList<String> list = new ArrayList<>();

        while (matcher.find()) {
            String match = matcher.group();
            list.add(match);
        }

        validateArguments(list);

        return list.toArray(new String[0]);
    }

    //@@author averageandyyy
    /**
     * Validates the arguments provided in the list to ensure that all options have their corresponding arguments
     * enclosed in quotes.
     *
     * <p>This method iterates through the list of arguments and checks each
     * option against the predefined options in the {@link constants.Options#OPTIONS_ARRAY}. If an option is found, it
     * verifies that the next element in the list is a quoted string. If the argument is not quoted, a
     * {@link TantouException} is thrown with a descriptive message.</p>
     *
     * @param list an ArrayList of Strings representing the command-line arguments provided by the user. Each option
     *             must have its argument in quotes (e.g., "-a \"Kubo Tite\"").
     * @throws TantouException if any option's argument is not enclosed in quotes.
     */
    public void validateArguments(ArrayList<String> list) throws TantouException {
        for (int i = 0; i < list.size(); i++) {
            for (String[] option : OPTIONS_ARRAY) {
                // Validate that the argument is in quotations only if the option requires it
                if (option[REQUIRE_ARGS_INDEX].equals(REQUIRE_ARGS_TRUE)) {
                    String optionWithDash = "-" + option[SHORT_OPTION_INDEX];
                    validateArgument(list, optionWithDash, i);
                }
            }
        }
    }

    //@@author averageandyyy
    /**
     * Validates that the argument following an option is a quoted string.
     *
     * @param list        the command-line arguments
     * @param optionIndex the index of the option being validated
     * @throws TantouException if the argument is not enclosed in quotes
     */
    public void validateArgument(ArrayList<String> list, String optionWithDash, int optionIndex)
            throws TantouException {
        if (optionWithDash.equals(list.get(optionIndex))) {
            // Check if the next element is a quoted string
            if (optionIndex + 1 >= list.size() || !isQuotedString(list.get(optionIndex + 1))) {
                throw new TantouException("Arguments must be in quotes, e.g., -a \"Kubo Tite\"");
            }
        }
    }

    //@@author averageandyyy
    /**
     * Checks if a given string is enclosed in quotes.
     *
     * @param argument the string to check
     * @return true if the string starts and ends with a quote, false otherwise
     */
    public boolean isQuotedString(String argument) {
        return argument.startsWith("\"") && argument.endsWith("\"") && argument.length() >= 2;
    }

    //@@author averageandyyy
    public String getAuthorNameFromInput(String userInput) throws TantouException {
        try {
            command = ownParser.parse(options, getUserInputAsList(userInput));
            return command.getOptionValue(constants.Options.AUTHOR_OPTION);
        } catch (ParseException e) {
            throw new TantouException(String.format("Something went wrong when parsing: %s", e.getMessage()));
        }
    }

    //@@author averageandyyy
    public String getMangaNameFromInput(String userInput) throws TantouException {
        try {
            command = ownParser.parse(options, getUserInputAsList(userInput));
            return command.getOptionValue(constants.Options.MANGA_OPTION);
        } catch (ParseException e) {
            throw new TantouException(String.format("Something went wrong when parsing: %s", e.getMessage()));
        }
    }

    //@@author
    public String getDeadlineDateFromInput(String userInput) throws TantouException {
        try {
            command = ownParser.parse(options, getUserInputAsList(userInput));
            return command.getOptionValue(constants.Options.BY_DATE_OPTION);
        } catch (ParseException e) {
            throw new TantouException(String.format("Something went wrong when parsing: %s", e.getMessage()));
        }
    }

    //@@author sarahchow03
    /**
     * Parses the user input to extract the quantity sold.
     * It checks the format of the input and returns the quantity as an integer.
     *
     * @param userInput The user's input string containing the quantity sold information.
     * @return The quantity sold as an integer.
     * @throws TantouException If the input format is incorrect, or if the quantity is not a valid integer.
     */
    public int getQuantitySoldFromInput(String userInput) throws TantouException {
        try {
            command = ownParser.parse(options, getUserInputAsList(userInput));
            return Integer.parseInt(command.getOptionValue(constants.Options.QUANTITY_OPTION));
        } catch (ParseException e) {
            throw new TantouException(String.format("Something went wrong when parsing: %s", e.getMessage()));
        } catch (NumberFormatException e) {
            throw new TantouException(String.format("Please enter a whole number for quantity sold!"));
        }
    }

    //@@author sarahchow03
    /**
     * Parses the user input to extract the unit price of the item.
     * It checks the format of the input and returns the unit price as a double.
     *
     * @param userInput The user's input string containing the unit price information.
     * @return The unit price as a double.
     * @throws TantouException If the input format is incorrect, or if the price is not a valid double.
     */
    public double getUnitPriceFromInput(String userInput) throws TantouException {
        try {
            command = ownParser.parse(options, getUserInputAsList(userInput));
            return Double.parseDouble(command.getOptionValue(constants.Options.PRICE_OPTION));
        } catch (ParseException e) {
            throw new TantouException(String.format("Something went wrong when parsing: %s", e.getMessage()));
        } catch (NumberFormatException e) {
            throw new TantouException(String.format("Please enter a number for unit price!"));
        }
    }

    //@@author averageandyyy
    /**
     * Processes the catalog command based on the user input.
     * Determines whether the command is an Add or Delete operation based on the presence of the delete flag (`-d`).
     * Further categorizes the command as either related to an Author or Manga and
     * returns the appropriate command object.
     *
     * If the `-d` flag is present, it will return a `DeleteAuthorCommand` or `DeleteMangaCommand` based on
     * the content of the user input.
     * Otherwise, it returns an `AddAuthorCommand` or `AddMangaCommand`.
     *
     * @param userInput the raw input provided by the user to be parsed into a command
     * @return a Command object representing the parsed operation (either Add or Delete, for Author or Manga)
     * @throws TantouException if the user input is invalid for either Add or Delete operations
     */
    public Command processCatalogCommand(String userInput) throws TantouException {
        if (isDeleteCommand(userInput)) {
            return processDeleteAuthorMangaCommand(userInput);
        }

        return processAddAuthorMangaCommand(userInput);
    }

    //@@author averageandyyy
    /**
     * Checks for the presence of the -d flag
     *
     * @param userInput the text input provided by the user
     * @return true if the -d flag is present, false otherwise
     * @throws TantouException if the user input is invalid at the validation or parsing stage
     */
    public boolean isDeleteCommand(String userInput) throws TantouException {
        try {
            command = ownParser.parse(options, getUserInputAsList(userInput));
            return command.hasOption(constants.Options.DELETE_OPTION);
        } catch (ParseException e) {
            throw new TantouException(String.format("Something went wrong when parsing: %s", e.getMessage()));
        }
    }

    //@@author averageandyyy
    /**
     * Processes the user input to create an Add command for either a Manga or Author.
     * Determines whether the input corresponds to a valid Manga or Author command
     * and returns the appropriate Add command object.
     *
     * If the input is valid for a Manga command, it returns an `AddMangaCommand`.
     * If the input is valid for an Author command, it returns an `AddAuthorCommand`.
     *
     * @param userInput the raw input string provided by the user,
     *                  which should specify an Add operation for either Manga or Author
     * @return a Command object representing the Add operation for Manga or Author
     * @throws TantouException if the user input is invalid for both Manga and Author Add commands
     */
    public Command processAddAuthorMangaCommand(String userInput) throws TantouException {
        if (isValidMangaCommand(userInput)) {
            return new AddMangaCommand(userInput);
        } else if (isValidAuthorCommand(userInput)) {
            return new AddAuthorCommand(userInput);
        }

        throw new TantouException("Invalid catalog command provided!");
    }

    //@@author averageandyyy
    /**
     * Processes the user input to create an Delete command for either a Manga or Author.
     * Determines whether the input corresponds to a valid Manga or Author command
     * and returns the appropriate Delete command object.
     *
     * If the input is valid for a Manga command, it returns an `DeleteMangaCommand`.
     * If the input is valid for an Author command, it returns an `DeleteAuthorCommand`.
     *
     * @param userInput the raw input string provided by the user,
     *                  which should specify an Add operation for either Manga or Author
     * @return a Command object representing the Add operation for Manga or Author
     * @throws TantouException if the user input is invalid for both Manga and Author Add commands
     */
    public Command processDeleteAuthorMangaCommand(String userInput) throws TantouException {
        if (isValidMangaCommand(userInput)) {
            return new DeleteMangaCommand(userInput);
        } else if (isValidAuthorCommand(userInput)) {
            return new DeleteAuthorCommand(userInput);
        }

        throw new TantouException("Invalid delete command provided!");
    }

    //@@author sarahchow03
    /**
     * Processes the user input to create a Sales command for either an Author.
     *
     * If the input is a valid sales command, it returns an `DeleteMangaCommand`.
     *
     * @param userInput the raw input string provided by the user,
     *                  which should include the author, manga, quantity sold, and unit price.
     * @return a Command object that adds the sales data for the specified manga.
     * @throws TantouException if the user input is missing a parameter.
     */
    public Command processAddSalesCommand(String userInput) throws TantouException {
        if (isValidSalesCommand(userInput)) {
            return new AddSalesCommand(userInput);
        }
        throw new TantouException("Invalid sales command provided!"
                + " You need to provide the author, manga, quantity sold, and unit price.");
    }

    //@@author averageandyyy
    public boolean isValidAuthorCommand(String userInput) throws TantouException {
        try {
            command = ownParser.parse(options, getUserInputAsList(userInput));
            return command.hasOption(constants.Options.AUTHOR_OPTION);
        } catch (ParseException e) {
            throw new TantouException(String.format("Something went wrong when parsing: %s", e.getMessage()));
        }
    }

    //@@author averageandyyy
    public boolean isValidMangaCommand(String userInput) throws TantouException {
        try {
            command = ownParser.parse(options, getUserInputAsList(userInput));
            return command.hasOption(constants.Options.AUTHOR_OPTION)
                    && command.hasOption(constants.Options.MANGA_OPTION);
        } catch (ParseException e) {
            throw new TantouException(String.format("Something went wrong when parsing: %s", e.getMessage()));
        }
    }

    //@@author xenthm
    private boolean isValidViewMangaCommand(String userInput) throws TantouException {
        try {
            command = ownParser.parse(options, getUserInputAsList(userInput));
            return command.hasOption(constants.Options.AUTHOR_OPTION);
        } catch (ParseException e) {
            throw new TantouException(String.format("Something went wrong when parsing: %s", e.getMessage()));
        }
    }

    private boolean isValidViewAuthorsCommand(String userInput) throws TantouException {
        // A valid view authors command should NOT have constants.Options.AUTHOR_OPTION in it
        return !isValidViewMangaCommand(userInput);
    }

    //@@author iaso1774
    private boolean isValidScheduleCommand(String userInput) throws TantouException {
        try {
            command = ownParser.parse(options, getUserInputAsList(userInput));
            return command.hasOption(constants.Options.BY_DATE_OPTION)
                    && command.hasOption(constants.Options.AUTHOR_OPTION)
                    && command.hasOption(constants.Options.MANGA_OPTION);
        } catch (ParseException e) {
            throw new TantouException(String.format("Something went wrong when parsing: %s", e.getMessage()));
        }
    }

    //@@author sarahchow03
    private boolean isValidSalesCommand(String userInput) throws TantouException {
        try {
            command = ownParser.parse(options, getUserInputAsList(userInput));
            return command.hasOption(constants.Options.QUANTITY_OPTION)
                    && command.hasOption(constants.Options.PRICE_OPTION)
                    && command.hasOption(constants.Options.AUTHOR_OPTION)
                    && command.hasOption(constants.Options.MANGA_OPTION);
        } catch (ParseException e) {
            throw new TantouException(String.format("Something went wrong when parsing: %s", e.getMessage()));
        }
    }

}
