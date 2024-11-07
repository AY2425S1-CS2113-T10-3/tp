package parser;

import exceptions.NoPriceProvidedException;
import exceptions.TantouException;

import static constants.Options.PRICE_OPTION;
import static constants.Regex.PRICE_EXTRACTOR_PATTERN;

public class PriceArgumentFinder extends ArgumentFinder {

    public PriceArgumentFinder() {
        super(PRICE_EXTRACTOR_PATTERN, PRICE_OPTION);
    }

    @Override
    public ArgumentResult getArgumentResult(String userInput) {
        String price = null;
        String outputString = userInput;
        matcher = pattern.matcher(userInput);

        if (matcher.find()) {
            price = matcher.group(0).trim();

            // Remove the argument and its matching flag
            outputString = userInput.substring(0, matcher.start(0) - 3) + userInput.substring(matcher.end(0));
        }

        return new ArgumentResult(price, outputString);
    }

}
