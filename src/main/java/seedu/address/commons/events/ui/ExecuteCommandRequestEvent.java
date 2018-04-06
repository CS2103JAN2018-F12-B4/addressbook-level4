package seedu.address.commons.events.ui;

import seedu.address.commons.events.BaseEvent;
import seedu.address.logic.commands.ImmediatelyExecutableCommand;

//@@author jonleeyz
/**
 * Indicates that a new result is available.
 */
public class ExecuteCommandRequestEvent extends BaseEvent {
    public final String commandWord;

    public ExecuteCommandRequestEvent(ImmediatelyExecutableCommand command) {
        commandWord = command.getCommandWord();
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + ": " + commandWord;
    }
}
//@@author
