package seedu.address.ui;

import java.util.logging.Logger;

import com.google.common.eventbus.Subscribe;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Region;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.events.ui.ExecuteCommandRequestEvent;
import seedu.address.commons.events.ui.HomeRequestEvent;
import seedu.address.commons.events.ui.NewResultAvailableEvent;
import seedu.address.commons.events.ui.PopulatePrefixesRequestEvent;
import seedu.address.logic.ListElementPointer;
import seedu.address.logic.Logic;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * The UI component that is responsible for receiving user command inputs.
 */
public class CommandBox extends UiPart<Region> {

    public static final String ERROR_STYLE_CLASS = "error";
    private static final String FXML = "CommandBox.fxml";

    private final Logger logger = LogsCenter.getLogger(CommandBox.class);
    private final Logic logic;
    private ListElementPointer historySnapshot;

    @FXML
    private TextField commandTextField;

    public CommandBox(Logic logic) {
        super(FXML);
        this.logic = logic;
        // calls #setStyleToDefault() whenever there is a change to the text of the command box.
        commandTextField.textProperty().addListener((unused1, unused2, unused3) -> setStyleToDefault());
        historySnapshot = logic.getHistorySnapshot();
        registerAsAnEventHandler(this);
    }

    /**
     * Handles the key press event, {@code keyEvent}.
     */
    @FXML
    private void handleKeyPress(KeyEvent keyEvent) {
        switch (keyEvent.getCode()) {
        case UP:
            // As up and down buttons will alter the position of the caret,
            // consuming it causes the caret's position to remain unchanged
            keyEvent.consume();
            navigateToPreviousInput();
            break;
        case DOWN:
            keyEvent.consume();
            navigateToNextInput();
            break;
        default:
            // let JavaFx handle the keypress
        }
    }

    /**
     * Updates the text field with the previous input in {@code historySnapshot},
     * if there exists a previous input in {@code historySnapshot}
     */
    private void navigateToPreviousInput() {
        assert historySnapshot != null;
        if (!historySnapshot.hasPrevious()) {
            return;
        }
        replaceText(historySnapshot.previous());
    }

    /**
     * Updates the text field with the next input in {@code historySnapshot},
     * if there exists a next input in {@code historySnapshot}
     */
    private void navigateToNextInput() {
        assert historySnapshot != null;
        if (!historySnapshot.hasNext()) {
            return;
        }
        replaceText(historySnapshot.next());
    }

    /**
     * Sets {@code CommandBox}'s text field with {@code text} and
     * positions the caret to the specified index.
     */
    private void replaceText(String text, int caretPosition) {
        commandTextField.setText(text);
        commandTextField.positionCaret(caretPosition);
    }

    /**
     * Sets {@code CommandBox}'s text field with {@code text} and
     * positions the caret to the end of the {@code text}.
     */
    private void replaceText(String text) {
        replaceText(text, text.length());
    }

    /**
     * Handles the Enter button pressed event.
     */
    @FXML
    private void handleCommandInputChanged() {
        try {
            if (commandTextField.getText().equals("")) {
                return;
            }
            CommandResult commandResult = logic.execute(commandTextField.getText());
            initHistory();
            historySnapshot.next();
            // process result of the command
            commandTextField.setText("");
            logger.info("Result: " + commandResult.feedbackToUser);
            raise(new NewResultAvailableEvent(commandResult.feedbackToUser, true));
        } catch (CommandException | ParseException e) {
            initHistory();
            // handle command failure
            setStyleToIndicateCommandFailure();
            logger.info("Invalid command: " + commandTextField.getText());
            raise(new NewResultAvailableEvent(e.getMessage(), false));
        }
    }

    //@@author jonleeyz
    private int getPreviousPrefixPosition(int currentCaretPosition) {
        // find last prefix position
        int previousPrefixPosition = commandTextField.getText().lastIndexOf(":", currentCaretPosition);

        // if last prefix is too close to caret, find the second last prefix position
        if (currentCaretPosition - previousPrefixPosition < 3) {
            previousPrefixPosition = commandTextField.getText().lastIndexOf(":",previousPrefixPosition - 1);
        }

        // set new caret position to be in front of chosen prefix. If prefix not found, then set at index 0.
        int newCaretPosition = previousPrefixPosition != -1 ? previousPrefixPosition + 1 : 0;

        // check for space in front of last prefix. If present, move forward one more index.
        if (commandTextField.getText().substring(newCaretPosition, newCaretPosition + 1).equals(" ")) {
            newCaretPosition += 1;
        }

        return newCaretPosition;
    }

    private int getNextPrefixPosition(int currentCaretPosition) {
        // find next prefix position
        int nextPrefixPosition = commandTextField.getText().indexOf(":", currentCaretPosition);
        int newCaretPosition;

        // set new caret position to be in front of chosen prefix. If prefix not found, then set at last index.
        if (nextPrefixPosition != -1) {
            newCaretPosition = nextPrefixPosition + 1;

            // check for space in front of last prefix. If present, move forward one more index.
            if (commandTextField.getText().substring(newCaretPosition, newCaretPosition + 1).equals(" ")) {
                newCaretPosition += 1;
            }
        } else {
            newCaretPosition = commandTextField.getText().length();
        }

        return newCaretPosition;
    }

    /**
     * Handles the event where a valid keyboard shortcut is pressed
     * to populate the CommandBox with command prefixes,
     * {@code PopulatePrefixesRequestEvent}.
     */
    @Subscribe
    private void handlePopulatePrefixesRequestEvent(PopulatePrefixesRequestEvent event) {
        commandTextField.requestFocus();
        replaceText(event.commandTemplate, event.caretIndex);
    }

    /**
     * Handles the event where a valid keyboard shortcut is pressed
     * to execute a command immediately
     * {@code ExecuteCommandRequestEvent}.
     */
    @Subscribe
    private void handleExecuteCommandRequestEvent(ExecuteCommandRequestEvent event) {
        replaceText(event.commandWord);
        handleCommandInputChanged();
        commandTextField.requestFocus();
    }

    /**
     * Handles the event where the Esc key is pressed or "home" is input to the CommandBox.
     * {@code HomeRequestEvent}.
     */
    @Subscribe
    private void handleHomeRequestEvent(HomeRequestEvent event) {
        replaceText("");
        commandTextField.requestFocus();
    }
    //@@author

    /**
     * Initializes the history snapshot.
     */
    private void initHistory() {
        historySnapshot = logic.getHistorySnapshot();
        // add an empty string to represent the most-recent end of historySnapshot, to be shown to
        // the user if she tries to navigate past the most-recent end of the historySnapshot.
        historySnapshot.add("");
    }

    /**
     * Sets the command box style to use the default style.
     */
    private void setStyleToDefault() {
        commandTextField.getStyleClass().remove(ERROR_STYLE_CLASS);
    }

    /**
     * Sets the command box style to indicate a failed command.
     */
    private void setStyleToIndicateCommandFailure() {
        ObservableList<String> styleClass = commandTextField.getStyleClass();
        if (styleClass.contains(ERROR_STYLE_CLASS)) {
            return;
        }
        styleClass.add(ERROR_STYLE_CLASS);
    }

}
