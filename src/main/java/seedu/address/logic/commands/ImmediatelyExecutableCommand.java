package seedu.address.logic.commands;

//@@author jonleeyz
/**
 * This interface is utilised in the {@code ExecuteCommandRequestEvent} class, where it is used
 * to provide a handle to {@code Commands} that immediately execute on press of their respective
 * keyboard shortcuts.
 */
public interface ImmediatelyExecutableCommand {
    /** Returns the command word of the Command */
    String getCommandWord();
}
//@@author
