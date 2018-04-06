package seedu.address.logic.commands;

//@@author jonleeyz
/**
 * This interface is utilised in the {@code ExecuteCommandRequestEvent} class, where it is used
 * to provide a handle to {@code Commands} that immediately execute on press of their respective
 * keyboard shortcuts.
 */
public interface PopulatableCommand {
    /** Returns the command word of the Command */
    String getCommandWord();

    /** Returns the complete template (command word + all prefixes) of the Command */
    String getTemplate();

    /** Returns the index where the cursor should be after population of the Command */
    int getCaretIndex();

    /** Returns the usage message of the Command */
    String getUsageMessage();
}
//@@author
