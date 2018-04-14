package guitests.guihandles;

import javafx.collections.ObservableList;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import seedu.address.ui.CommandBox;

/**
 * A handle to the {@code CommandBox} in the GUI.
 */
public class CommandBoxHandle extends NodeHandle<TextField> {

    public static final String COMMAND_INPUT_FIELD_ID = "#commandTextField";

    public CommandBoxHandle(TextField commandBoxNode) {
        super(commandBoxNode);
    }

    /**
     * Returns the text in the command box.
     */
    public String getInput() {
        return getRootNode().getText();
    }

    /**
     * Enters the given command in the Command Box and presses enter.
     * @return true if the command succeeded, false otherwise.
     */
    public boolean run(String command) {
        click();
        guiRobot.interact(() -> getRootNode().setText(command));
        guiRobot.pauseForHuman();

        guiRobot.type(KeyCode.ENTER);

        return !getStyleClass().contains(CommandBox.ERROR_STYLE_CLASS);
    }

    //@@author jonleeyz
    /**
     * Sets text in the command box
     */
    public boolean setInput(String text) {
        click();
        guiRobot.interact(() -> getRootNode().setText(text));
        return !getStyleClass().contains(CommandBox.ERROR_STYLE_CLASS);
    }

    /**
     * Clears all text in the command box.
     * @return true if the command succeeded, false otherwise.
     */
    public boolean clear() {
        click();
        guiRobot.interact(() -> getRootNode().clear());
        return getRootNode().getText().equals("");
    }

    /**
     * Gets caret position in the command box
     */
    public int getCaretPosition() {
        return getRootNode().getCaretPosition();
    }

    /**
     * Sets caret position in the command box
     */
    public void setCaretPosition(int index) {
        click();
        guiRobot.interact(() -> getRootNode().positionCaret(index));
    }
    //@@author

    /**
     * Returns the list of style classes present in the command box.
     */
    public ObservableList<String> getStyleClass() {
        return getRootNode().getStyleClass();
    }
}
