package seedu.address.logic.commands;

import java.util.function.Predicate;

import seedu.address.model.person.Person;

//@@author melvintzw

/**
 * Finds and lists all persons in address book whose name contains any of the argument keywords.
 * Keyword matching is case sensitive.
 */
public class FindCommand extends Command implements PopulatableCommand {

    public static final String COMMAND_WORD = "find";
    public static final String COMMAND_ALIAS = "f";

    public static final String MESSAGE_USAGE =
            COMMAND_WORD + " | Finds all persons whose fields contain any of the specified keywords (case-insensitive) "
            + "and displays them as a list with index numbers."
            + "\n\t"
            + "Refer to the User Guide (press \"F1\") for detailed information about this command!"
            + "\n\t"
            + "Parameters:\t"
            + COMMAND_WORD + " "
            + "[SPECIFIER] KEYWORD [MORE_KEYWORDS]..."
            + "\n\t"
            + "Specifiers:\t\t"
            + "-all, -n, -p, -e, -a, -t : ALL, NAME, PHONE, EMAIL, ADDRESS and TAGS respectively."
            + "\n\t"
            + "Example:\t\t" + COMMAND_WORD + " -n alice bob charlie";

    private final Predicate<Person> predicate;

    public FindCommand(Predicate<Person> predicate) {
        this.predicate = predicate;
    }

    /**
     * For call in PopulatePrefixRequestEvent class, to assign string values.
     */
    public FindCommand() {
        predicate = null;
    }


    @Override
    public CommandResult execute() {
        model.updateFilteredPersonList(predicate);
        return new CommandResult(getMessageForPersonListShownSummary(model.getFilteredPersonList().size()));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof FindCommand // instanceof handles nulls
                && this.predicate.equals(((FindCommand) other).predicate));
        // state check
    }

    //@@author jonleeyz
    @Override
    public String getCommandWord() {
        return COMMAND_WORD;
    }

    @Override
    public String getTemplate() {
        return COMMAND_WORD + " ";
    }

    @Override
    public int getCaretIndex() {
        return getTemplate().length();
    }

    @Override
    public String getUsageMessage() {
        return MESSAGE_USAGE;
    }
    //@@author
}
