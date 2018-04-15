//@@author zhangriqi
package systemtests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static seedu.address.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;

import org.junit.Test;

import javafx.scene.input.KeyCode;
import seedu.address.commons.events.ui.PopulatePrefixesRequestEvent;
import seedu.address.logic.commands.LocateCommand;
import seedu.address.model.Model;

public class LocateCommandSystemTest extends AddressBookSystemTest {

    //@@author zhangriqi-reused
    @Test
    public void focusOnCommandBox_populateLocateCommandTemplate_usingAccelerator() {
        //use accelerator
        getCommandBox().click();
        populateLocateCommandUsingAccelerator();
        assertPopulationSuccess();
    }

    @Test
    public void focusOnResultDisplay_populateLocateCommandTemplate_usingAccelerator() {
        getResultDisplay().click();
        populateLocateCommandUsingAccelerator();
        assertPopulationSuccess();
    }

    @Test
    public void focusOnPersonListPanel_populateLocateCommandTemplate_usingAccelerator() {
        getPersonListPanel().click();
        populateLocateCommandUsingAccelerator();
        assertPopulationSuccess();
    }

    @Test
    public void focusOnBrowserPanel_populateLocateCommandTemplate_usingAccelerator() {
        getBrowserPanel().click();
        populateLocateCommandUsingAccelerator();
        assertPopulationSuccess();
    }

    @Test
    public void populateLocateCommandTemplate_usingMenuButton() {
        populateLocateCommandUsingMenu();
        assertPopulationSuccess();
    }
    /**
     * Executes {@code command} and asserts that the,<br>
     * 1. Command box displays {@code command}.<br>
     * 2. Command box has the error style class.<br>
     * 3. Result display box displays {@code expectedResultMessage}.<br>
     * 4. {@code Model}, {@code Storage} and {@code PersonListPanel} remain unchanged.<br>
     * 5. Browser url, selected card and status bar remain unchanged.<br>
     * Verifications 1, 3 and 4 are performed by
     * {@code AddressBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.<br>
     * @see AddressBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     */
    private void assertCommandFailure(String command, String expectedResultMessage) {
        Model expectedModel = getModel();

        executeCommand(command);
        assertApplicationDisplaysExpected(command, expectedResultMessage, expectedModel);
        assertSelectedCardUnchanged();
        assertCommandBoxAndResultDisplayShowsErrorStyle();
        assertStatusBarUnchanged();
    }

    /**
     * Asserts that population of the {@code CommandBox} with the LocateCommand
     * template was successful.
     */
    private void assertPopulationSuccess() {
        assertEquals(LocateCommand.COMMAND_TEMPLATE, getCommandBox().getInput());
        assertEquals(LocateCommand.MESSAGE_USAGE, getResultDisplay().getText());
        assertTrue(eventsCollectorRule.eventsCollector.getMostRecent() instanceof PopulatePrefixesRequestEvent);
        guiRobot.pauseForHuman();

        executeCommand("invalid command");
        assertTrue(getCommandBox().clear());
        assertEquals(MESSAGE_UNKNOWN_COMMAND, getResultDisplay().getText());
        guiRobot.pauseForHuman();
    }

    /**
     * Populates the {@code CommandBox} with the LocateCommand template
     * using the associated accelerator in {@code MainWindow}.
     */
    private void populateLocateCommandUsingAccelerator() {
        populateUsingAccelerator(KeyCode.CONTROL, KeyCode.L);
    }

    /**
     * Populates the {@code CommandBox} with the LocateCommand template
     * using the menu bar in {@code MainWindow}.
     */
    private void populateLocateCommandUsingMenu() {
        populateUsingMenu("Actions", "Locate a Person...");
    }
}
