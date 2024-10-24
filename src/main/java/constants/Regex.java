package constants;

import java.util.regex.Pattern;

import static constants.Options.AUTHOR_OPTION;
import static constants.Options.DELETE_OPTION;
import static constants.Options.MANGA_OPTION;
import static constants.Options.PRICE_OPTION;
import static constants.Options.QUANTITY_OPTION;

public class Regex {
    public static final String SPACE_REGEX = " ";
    public static final String EMPTY_REGEX = "";
    public static final String AUTHOR_OPTION_REGEX = SPACE_REGEX + AUTHOR_OPTION + SPACE_REGEX;
    public static final String DELETE_OPTION_REGEX = SPACE_REGEX + DELETE_OPTION;
    public static final String MANGA_OPTION_REGEX = SPACE_REGEX + MANGA_OPTION + SPACE_REGEX;
    public static final String PRICE_OPTION_REGEX = SPACE_REGEX + PRICE_OPTION + SPACE_REGEX;
    public static final String QUANTITY_OPTION_REGEX = SPACE_REGEX + QUANTITY_OPTION + SPACE_REGEX;

    /** Pattern to match quoted strings or individual words */
    // public static final Pattern USER_COMMAND_REGEX = Pattern.compile("\"[^\"]*\"|\\S+");
    public static final Pattern USER_COMMAND_REGEX = Pattern.compile("\"[^\"]+\"|\\S+");
}
