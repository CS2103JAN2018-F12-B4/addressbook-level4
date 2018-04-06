package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.commons.core.index.Index.fromOneBased;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CUSTOMERS;

import java.util.ArrayList;
import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.AssignCommand;
import seedu.address.logic.parser.exceptions.ParseException;

//@@author melvintzw

/**
 * Parses input arguments and creates a new EditCommand object
 */
public class AssignCommandParser implements Parser<AssignCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the EditCommand
     * and returns an EditCommand object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format
     */
    public AssignCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_CUSTOMERS);

        Index runnerIndex; //parameter for AssignCommand
        Index[] customerIndexArray; //parameter for AssignCommand

        try {
            runnerIndex = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (IllegalValueException ive) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AssignCommand.MESSAGE_USAGE));
        }

        try {
            String customers = argMultimap.getValue(PREFIX_CUSTOMERS).get();
            List<Index> customerIndexList = parseCustIndex(customers);
            customerIndexArray = customerIndexList.toArray(new Index[customerIndexList.size()]);

        } catch (IllegalValueException ive) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AssignCommand.MESSAGE_USAGE));
        } catch (NumberFormatException nfe) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AssignCommand.MESSAGE_USAGE));
        }

        return new AssignCommand(runnerIndex, customerIndexArray);
    }

    /**
     * Parses a string of customer numbers (representing indices) into a list of Index objects
     *
     * @param customers a string of numbers presenting indices
     */
    private static List<Index> parseCustIndex(String customers) throws IllegalValueException, NumberFormatException {
        String[] splitIndices = customers.split("\\s");
        List<Index> indexList = new ArrayList<>();
        for (String s : splitIndices) {
            int index = Integer.parseInt(s);
            indexList.add(fromOneBased(index));
        }
        if (indexList.size() < 1) {
            throw new IllegalValueException("no customer index has been specified");
        }
        return indexList;
    }

}
