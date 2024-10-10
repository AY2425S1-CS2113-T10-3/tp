package constants;

public class Options {
    public static final String AUTHOR_OPTION = "a";
    public static final String AUTHOR_LONG_OPTION = "author";
    public static final String AUTHOR_OPTION_DESC = "Author name";

    public static final String MANGA_OPTION = "m";
    public static final String MANGA_LONG_OPTION = "manga";
    public static final String MANGA_OPTION_DESC = "Manga name";

    public static final String DEADLINE_DATE_OPTION = "b";
    public static final String DEADLINE_LONG_OPTION = "by";
    public static final String DEADLINE_OPTION_DESC = "Deadline date";

    public static final String[][] OPTIONS_ARRAY = {
            {AUTHOR_OPTION, AUTHOR_LONG_OPTION, AUTHOR_OPTION_DESC},
            {MANGA_OPTION, MANGA_LONG_OPTION, MANGA_OPTION_DESC},
            {DEADLINE_DATE_OPTION, DEADLINE_LONG_OPTION, DEADLINE_OPTION_DESC}
    };

    public static final int SHORT_OPTION_INDEX = 0;
    public static final int LONG_OPTION_INDEX = 1;
    public static final int OPTION_DESC_INDEX = 2;
}
