package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.Arrays;

import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.AddressContainsKeywordsPredicate;
import seedu.address.model.person.EmailContainsKeywordsPredicate;
import seedu.address.model.person.NameContainsKeywordsPredicate;
import seedu.address.model.person.PersonContainsKeywordsPredicate;
import seedu.address.model.person.PhoneContainsKeywordsPredicate;
import seedu.address.model.person.TagsContainsKeywordsPredicate;

/**
 * Parses input arguments and creates a new FindCommand object
 */
public class FindCommandParser implements Parser<FindCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the FindCommand
     * and returns an FindCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public FindCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();
        if (trimmedArgs.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }

        //@@author melvintzw
        String[] arguments = trimmedArgs.split("\\s+");
        String[] keywords;
        //check arguments[0] for specifier

        if (arguments[0].matches("\\p{Alnum}+.*+")) {
            return new FindCommand(new PersonContainsKeywordsPredicate(Arrays.asList(arguments)));
        }

        switch (arguments[0]) {
        case "-all":
            keywords = Arrays.copyOfRange(arguments, 1, arguments.length);
            return new FindCommand(new PersonContainsKeywordsPredicate(Arrays.asList(keywords)));
        case "-n":
            keywords = Arrays.copyOfRange(arguments, 1, arguments.length);
            return new FindCommand(new NameContainsKeywordsPredicate(Arrays.asList(keywords)));
        case "-p":
            keywords = Arrays.copyOfRange(arguments, 1, arguments.length);
            return new FindCommand(new PhoneContainsKeywordsPredicate(Arrays.asList(keywords)));
        case "-e":
            keywords = Arrays.copyOfRange(arguments, 1, arguments.length);
            return new FindCommand(new EmailContainsKeywordsPredicate(Arrays.asList(keywords)));
        case "-a":
            keywords = Arrays.copyOfRange(arguments, 1, arguments.length);
            return new FindCommand(new AddressContainsKeywordsPredicate(Arrays.asList(keywords)));
        case "-t":
            keywords = Arrays.copyOfRange(arguments, 1, arguments.length);
            return new FindCommand(new TagsContainsKeywordsPredicate(Arrays.asList(keywords)));
        default:
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }
    }

}
