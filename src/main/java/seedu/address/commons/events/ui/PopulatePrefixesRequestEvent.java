package seedu.address.commons.events.ui;

import seedu.address.commons.events.BaseEvent;
import seedu.address.logic.commands.PopulatableCommand;

//@@author jonleeyz
/**
 * Indicates that a new result is available.
 */
public class PopulatePrefixesRequestEvent extends BaseEvent {

    public final String commandUsageMessage;
    public final String commandTemplate;
    public final int caretIndex;
    private final String commandWord;

    public PopulatePrefixesRequestEvent(PopulatableCommand command) {
        commandUsageMessage = command.getUsageMessage();
        commandTemplate = command.getTemplate();
        caretIndex = command.getCaretIndex();
        commandWord = command.getCommandWord();
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + ": " + commandWord;
    }
}
//@@author
