//@@author zhangriqi
package seedu.address.logic.commands;

import java.util.List;

import seedu.address.commons.core.EventsCenter;
import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.commons.events.ui.LocateRequestEvent;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.person.Person;
import seedu.address.ui.MainWindow;

/**
 * Display the place identified using its las displayed index from the address book
 */
public class LocateCommand extends Command {
    public static final String COMMAND_WORD = "locate";
    public static final String COMMAND_ALIAS = "lo";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Display the place on Google Map identified by the index number used in the last person listing.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_LOCATE_SUCCESS = "Locate: %1$s";

    private final Index targetIndex;

    public LocateCommand (Index targetIndex) {
        this.targetIndex = targetIndex;

    }

    @Override
    public CommandResult execute() throws CommandException {

        List<Person> lastShownList = model.getFilteredPersonList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);

        }

        Person location = lastShownList.get(targetIndex.getZeroBased());
        //@@author unknown-reused
        // Open Google Map on BrowserPanel
        MainWindow.loadUrl("https://www.google.com.sg/maps/place/"
                + location.getAddress().toString());
        //@@author
        EventsCenter.getInstance().post(new LocateRequestEvent(targetIndex));
        return new CommandResult(String.format(MESSAGE_LOCATE_SUCCESS, targetIndex.getOneBased()));

    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof LocateCommand // instanceof handles nulls
                && this.targetIndex.equals(((LocateCommand) other).targetIndex)); //start check
    }
}
