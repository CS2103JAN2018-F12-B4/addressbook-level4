package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
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

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Optional;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.EditCommand.EditPersonDescriptor;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.customer.MoneyBorrowed;
import seedu.address.model.person.customer.StandardInterest;
import seedu.address.model.tag.Tag;

//@@author melvintzw
/**
 * Parses input arguments and creates a new EditCommand object
 */
public class EditCommandParser implements Parser<EditCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the EditCommand
     * and returns an EditCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public EditCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL, PREFIX_ADDRESS, PREFIX_TAG);

        Index index;

        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (IllegalValueException ive) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE));
        }

        EditPersonDescriptor editPersonDescriptor = new EditPersonDescriptor();
        try {
            ParserUtil.parseName(argMultimap.getValue(PREFIX_NAME)).ifPresent(editPersonDescriptor::setName);
            ParserUtil.parsePhone(argMultimap.getValue(PREFIX_PHONE)).ifPresent(editPersonDescriptor::setPhone);
            ParserUtil.parseEmail(argMultimap.getValue(PREFIX_EMAIL)).ifPresent(editPersonDescriptor::setEmail);
            ParserUtil.parseAddress(argMultimap.getValue(PREFIX_ADDRESS)).ifPresent(editPersonDescriptor::setAddress);
            parseTagsForEdit(argMultimap.getAllValues(PREFIX_TAG)).ifPresent(editPersonDescriptor::setTags);

            //@@author melvintzw
            if (argMultimap.getValue(PREFIX_OWESTARTDATE).isPresent()) {
                Date oweStartDate = ParserUtil.parseDate(argMultimap.getValue(PREFIX_OWESTARTDATE).get());
                editPersonDescriptor.setOweStartDate(oweStartDate);
            }
            if (argMultimap.getValue(PREFIX_OWEDUEDATE).isPresent()) {
                Date oweDueDate = ParserUtil.parseDate(argMultimap.getValue(PREFIX_OWEDUEDATE).get());
                editPersonDescriptor.setOweDueDate(oweDueDate);
            }

            if (argMultimap.getValue(PREFIX_MONEYOWED).isPresent()) {
                MoneyBorrowed moneyBorrowed = ParserUtil.parseMoneyBorrowed(argMultimap.getValue(PREFIX_MONEYOWED)
                        .get());
                editPersonDescriptor.setMoneyBorrowed(moneyBorrowed);
            }

            if (argMultimap.getValue(PREFIX_INTEREST).isPresent()) {
                StandardInterest standardInterest = ParserUtil.parseStandardInterest(argMultimap.getValue
                        (PREFIX_INTEREST).get());
                editPersonDescriptor.setStandardInterest(standardInterest);
            }
        } catch (IllegalValueException ive) {
            throw new ParseException(ive.getMessage(), ive);
        }
        //@@author

        if (!editPersonDescriptor.isAnyFieldEdited()) {
            throw new ParseException(EditCommand.MESSAGE_NOT_EDITED);
        }

        return new EditCommand(index, editPersonDescriptor);
    }

    /**
     * Parses {@code Collection<String> tags} into a {@code Set<Tag>} if {@code tags} is non-empty.
     * If {@code tags} contain only one element which is an empty string, it will be parsed into a
     * {@code Set<Tag>} containing zero tags.
     */
    private Optional<Set<Tag>> parseTagsForEdit(Collection<String> tags) throws IllegalValueException {
        assert tags != null;

        if (tags.isEmpty()) {
            return Optional.empty();
        }
        Collection<String> tagSet = tags.size() == 1 && tags.contains("") ? Collections.emptySet() : tags;
        return Optional.of(ParserUtil.parseTags(tagSet));
    }

}
