package constants;

public class Options {
    public static final String AUTHOR_OPTION = "a";
    public static final String AUTHOR_LONG_OPTION = "author";
    public static final String AUTHOR_OPTION_DESC = "Author name";

    public static final String MANGA_OPTION = "m";
    public static final String MANGA_LONG_OPTION = "manga";
    public static final String MANGA_OPTION_DESC = "Manga name";

    public static final String DEADLINE_DATE_OPTION = "b";
    public static final String DEADLINE_LONG_OPTION = "deadline";
    public static final String DEADLINE_OPTION_DESC = "Deadline date";

    public static final String DELETE_OPTION = "d";
    public static final String DELETE_LONG_OPTION = "delete";
    public static final String DELETE_OPTION_DESC = "To delete";

    public static final String REQUIRE_ARGS_TRUE = "true";
    public static final String REQUIRE_ARGS_FALSE = "false";

    public static final String[][] OPTIONS_ARRAY = {
            {AUTHOR_OPTION, REQUIRE_ARGS_TRUE, AUTHOR_LONG_OPTION, AUTHOR_OPTION_DESC},
            {MANGA_OPTION, REQUIRE_ARGS_TRUE, MANGA_LONG_OPTION, MANGA_OPTION_DESC},
            {DEADLINE_DATE_OPTION, REQUIRE_ARGS_TRUE, DEADLINE_LONG_OPTION, DEADLINE_OPTION_DESC},
            {DELETE_OPTION, REQUIRE_ARGS_FALSE, DELETE_LONG_OPTION, DELETE_OPTION_DESC}
    };

    public static final int SHORT_OPTION_INDEX = 0;
    public static final int REQUIRE_ARGS_INDEX = 1;
    public static final int LONG_OPTION_INDEX = 2;
    public static final int OPTION_DESC_INDEX = 3;
}
