package seedu.address.logic.commands;

import seedu.address.model.person.PersonContainsKeywordsPredicate;

/**
 * Finds and lists all persons in address book whose name contains any of the argument keywords.
 * Keyword matching is case sensitive.
 */
public class FindCommand extends Command {

    public static final String COMMAND_WORD = "find";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds all persons whose names contain any of "
            + "the specified keywords (case-sensitive) and displays them as a list with index numbers.\n"
            + "Parameters: KEYWORD [MORE_KEYWORDS]...\n"
            + "Example: " + COMMAND_WORD + " alice bob charlie";

    private final PersonContainsKeywordsPredicate personContainsKeyWordsPredicate;

    public FindCommand(PersonContainsKeywordsPredicate personContainsKeyWordsPredicate) {
        this.personContainsKeyWordsPredicate = personContainsKeyWordsPredicate;
    }

    @Override
    public CommandResult execute() {
        model.updateFilteredPersonList(personContainsKeyWordsPredicate);
        return new CommandResult(getMessageForPersonListShownSummary(model.getFilteredPersonList().size()));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof FindCommand // instanceof handles nulls
                && this.personContainsKeyWordsPredicate.equals(((FindCommand) other).personContainsKeyWordsPredicate)); // state check
    }
}
