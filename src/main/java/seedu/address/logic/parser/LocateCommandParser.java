//@@author zhangriqi
package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.Arrays;

import seedu.address.logic.commands.LocateCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.AddressContainsKeywordsPredicate;
import seedu.address.model.person.EmailContainsKeywordsPredicate;
import seedu.address.model.person.NameContainsKeywordsPredicate;
import seedu.address.model.person.PersonContainsKeywordsPredicate;
import seedu.address.model.person.PhoneContainsKeywordsPredicate;
import seedu.address.model.person.TagsContainsKeywordsPredicate;

/**
 * Parse input arguments and create a new LocateCommand object
 */
public class LocateCommandParser implements Parser<LocateCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the DeleteCommand
     * and returns an DeleteCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    @Override
    public LocateCommand parse(String args) throws ParseException {

        String trimmedArgs = args.trim();
        if (trimmedArgs.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, LocateCommand.MESSAGE_USAGE));
        }

        String[] arguments = trimmedArgs.split("\\s+");
        String[] keywords;
        //check arguments[0] for specifier

        if (arguments[0].matches("\\p{Alnum}+.++")) {
            return new LocateCommand(new PersonContainsKeywordsPredicate(Arrays.asList(arguments)));
        }

        switch (arguments[0]) {
        case "-n":
            keywords = Arrays.copyOfRange(arguments, 1, arguments.length);
            return new LocateCommand(new NameContainsKeywordsPredicate(Arrays.asList(keywords)));
        case "-p":
            keywords = Arrays.copyOfRange(arguments, 1, arguments.length);
            return new LocateCommand(new PhoneContainsKeywordsPredicate(Arrays.asList(keywords)));
        case "-e":
            keywords = Arrays.copyOfRange(arguments, 1, arguments.length);
            return new LocateCommand(new EmailContainsKeywordsPredicate(Arrays.asList(keywords)));
        case "-a":
            keywords = Arrays.copyOfRange(arguments, 1, arguments.length);
            return new LocateCommand(new AddressContainsKeywordsPredicate(Arrays.asList(keywords)));
        case "-t":
            keywords = Arrays.copyOfRange(arguments, 1, arguments.length);
            return new LocateCommand(new TagsContainsKeywordsPredicate(Arrays.asList(keywords)));
        default:
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, LocateCommand.MESSAGE_USAGE));
        }
    }


}

