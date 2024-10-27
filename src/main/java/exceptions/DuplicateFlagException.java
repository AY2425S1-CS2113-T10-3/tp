package exceptions;

public class DuplicateFlagException extends TantouException {

    public DuplicateFlagException(String flag) {
        super(getFormatted(flag));
    }

    private static String getFormatted(String flag) {
        return String.format("Duplicate %s flag found!", flag);
    }
}
