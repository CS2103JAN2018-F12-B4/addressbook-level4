package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_INTEREST;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MONEYOWED;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_OWEDUEDATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_OWESTARTDATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TYPE;

import java.util.ArrayList;
import java.util.Date;
import java.util.Set;
import java.util.stream.Stream;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;
import seedu.address.model.person.customer.Customer;
import seedu.address.model.person.customer.LateInterest;
import seedu.address.model.person.customer.MoneyBorrowed;
import seedu.address.model.person.customer.StandardInterest;
import seedu.address.model.person.runner.Runner;
import seedu.address.model.tag.Tag;

//@@author melvintzw

/**
 * Parses input arguments and creates a new AddCommand object
 */
public class AddCommandParser implements Parser<AddCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the AddCommand
     * and returns an AddCommand object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format
     */
    public AddCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_TYPE, PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL,
                        PREFIX_ADDRESS, PREFIX_TAG, PREFIX_MONEYOWED, PREFIX_OWESTARTDATE, PREFIX_OWEDUEDATE,
                        PREFIX_INTEREST);

        //TODO: add test case
        //@@author melvintzw
        if (!arePrefixesPresent(argMultimap, PREFIX_NAME, PREFIX_TYPE)
                || !argMultimap.getPreamble().isEmpty()
                || !argMultimap.getValue(PREFIX_TYPE).get().matches("[cCrR]")) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));
        }

        try {
            Name name = ParserUtil.parseName(argMultimap.getValue(PREFIX_NAME)).get();
            Phone phone = ParserUtil.parsePhone(argMultimap.getValue(PREFIX_PHONE)).orElse(new Phone());
            Email email = ParserUtil.parseEmail(argMultimap.getValue(PREFIX_EMAIL)).orElse(new Email());
            Address address = ParserUtil.parseAddress(argMultimap.getValue(PREFIX_ADDRESS)).orElse(new Address());
            Set<Tag> tagList = ParserUtil.parseTags(argMultimap.getAllValues(PREFIX_TAG));

            if (argMultimap.getValue(PREFIX_TYPE).get().matches("[cC]")) {
                Date oweStartDate = ParserUtil.parseDate(argMultimap.getValue(PREFIX_OWESTARTDATE)).orElse(new Date(0));
                Date oweDueDate = ParserUtil.parseDate(argMultimap.getValue(PREFIX_OWEDUEDATE)).orElse(new Date(0));

                if (oweDueDate.compareTo(oweStartDate) < 0) {
                    throw new ParseException("OWE_DUE_DATE cannot be before OWE_START_DATE");
                }

                MoneyBorrowed moneyBorrowed = ParserUtil.parseMoneyBorrowed(argMultimap.getValue(PREFIX_MONEYOWED))
                        .orElse(new MoneyBorrowed());

                StandardInterest standardInterest = ParserUtil.parseStandardInterest(argMultimap
                        .getValue(PREFIX_INTEREST)).orElse(new StandardInterest());

                Customer customer = new Customer(name, phone, email, address, tagList, moneyBorrowed,
                        oweStartDate, oweDueDate, standardInterest, new LateInterest(), new Runner());

                return new AddCommand(customer);

            } else if (argMultimap.getValue(PREFIX_TYPE).get().matches("[rR]")) {
                if (argMultimap.getValue(PREFIX_MONEYOWED).isPresent()
                        || argMultimap.getValue(PREFIX_OWEDUEDATE).isPresent()
                        || argMultimap.getValue(PREFIX_OWESTARTDATE).isPresent()
                        || argMultimap.getValue(PREFIX_INTEREST).isPresent()) {
                    throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                            AddCommand.MESSAGE_INVALID_PREFIX));
                }
                Runner runner = new Runner(name, phone, email, address, tagList, new ArrayList<>());
                return new AddCommand(runner);

            } else {
                throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));
            }

        } catch (IllegalValueException ive) {
            throw new ParseException(ive.getMessage(), ive);
        }
        //@@author
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }

}
