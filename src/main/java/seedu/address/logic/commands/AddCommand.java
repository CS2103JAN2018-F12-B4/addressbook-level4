package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
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

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.person.Person;
import seedu.address.model.person.exceptions.DuplicatePersonException;

/**
 * Adds a person to the address book.
 */
public class AddCommand extends UndoableCommand implements PopulatableCommand {

    public static final String COMMAND_WORD = "add";
    public static final String COMMAND_ALIAS = "a";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a person to the address book. "
            + "Parameters: "
            + PREFIX_TYPE + " r(or c) "
            + PREFIX_NAME + " NAME "
            + "[" + PREFIX_PHONE + " PHONE] "
            + "[" + PREFIX_EMAIL + " EMAIL] "
            + "[" + PREFIX_ADDRESS + " ADDRESS] "
            + "[" + PREFIX_TAG + " TAG]...\n"
            + "if type(ty) is 'c' then additional fields are "
            + "[" + PREFIX_OWESTARTDATE + " OWESTARTDATE] "
            + "[" + PREFIX_OWEDUEDATE + " OWEDUEDATE] "
            + "[" + PREFIX_MONEYOWED + " MONEYOWED] "
            + "[" + PREFIX_INTEREST + " INTERESTRATE]\n"

            + "\nExample: " + COMMAND_WORD + " "
            + PREFIX_TYPE + " c "
            + PREFIX_NAME + " John Doe "
            + PREFIX_PHONE + " 98765432 "
            + PREFIX_EMAIL + " johnd@example.com "
            + PREFIX_ADDRESS + " 311, Clementi Ave 2, #02-25 "
            + PREFIX_MONEYOWED + " 1000 "
            + PREFIX_INTEREST + " 2.5 "
            + PREFIX_OWESTARTDATE + " today "
            + PREFIX_OWEDUEDATE + " 5 May 2018 "
            + PREFIX_TAG + " owesMoney";

    public static final String MESSAGE_SUCCESS = "New person added!\n%1$s";
    public static final String MESSAGE_DUPLICATE_PERSON = "This person already exists in the address book";

    private final Person toAdd;

    /**
     * Creates an AddCommand to add the specified {@code Person}
     */
    public AddCommand(Person person) {
        requireNonNull(person);
        toAdd = person;
    }

    /**
     * For call in PopulatePrefixRequestEvent class, to assign string values.
     */
    public AddCommand() {
        toAdd = null;
    }

    @Override
    public CommandResult executeUndoableCommand() throws CommandException {
        requireNonNull(model);
        try {
            model.addPerson(toAdd);
            return new CommandResult(String.format(MESSAGE_SUCCESS, toAdd));
        } catch (DuplicatePersonException e) {
            throw new CommandException(MESSAGE_DUPLICATE_PERSON);
        }

    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof AddCommand // instanceof handles nulls
                && toAdd.equals(((AddCommand) other).toAdd));
    }

    //@@author jonleeyz
    @Override
    public String getCommandWord() {
        return COMMAND_WORD;
    }

    @Override
    public String getTemplate() {
        return COMMAND_WORD + " " + PREFIX_TYPE + "  " + PREFIX_NAME + "  "
                + PREFIX_PHONE + "  " + PREFIX_EMAIL + "  " + PREFIX_ADDRESS + "  "
                + PREFIX_TAG + " ";
    }

    @Override
    public int getCaretIndex() {
        return (COMMAND_WORD + " " + PREFIX_TYPE + " ").length();
    }

    @Override
    public String getUsageMessage() {
        return MESSAGE_USAGE;
    }
    //@@author
}
