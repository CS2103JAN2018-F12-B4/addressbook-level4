package systemtests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.ui.testutil.GuiTestAssert.assertListMatching;

import org.junit.Test;

import guitests.GuiRobot;
import guitests.guihandles.HelpWindowHandle;
import javafx.scene.input.KeyCode;
import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.commands.HelpCommand;
import seedu.address.logic.commands.SelectCommand;
import seedu.address.ui.BrowserPanel;
import seedu.address.ui.StatusBarFooter;

/**
 * A system test class for the help window, which contains interaction with other UI components.
 */
public class HelpCommandSystemTest extends AddressBookSystemTest {
    private static final String ERROR_MESSAGE = "ATTENTION!!!! : On some computers, this test may fail when run on "
            + "non-headless mode as FxRobot#clickOn(Node, MouseButton...) clicks on the wrong location. We suspect "
            + "that this is a bug with TestFX library that we are using. If this test fails, you have to run your "
            + "tests on headless mode. See UsingGradle.adoc on how to do so.";

    private final GuiRobot guiRobot = new GuiRobot();

    @Test
    public void openHelpWindow() {
        //use accelerator
        getCommandBox().click();
        executeHelpCommandUsingAccelerator();
        assertHelpWindowOpen();

        getResultDisplay().click();
        executeHelpCommandUsingAccelerator();
        assertHelpWindowOpen();

        getPersonListPanel().click();
        executeHelpCommandUsingAccelerator();
        assertHelpWindowOpen();

        getBrowserPanel().click();
        executeHelpCommandUsingAccelerator();
        assertHelpWindowNotOpen();

        //use menu button
        executeHelpCommandUsingMenu();
        assertHelpWindowOpen();

        //use command box
        executeCommand(HelpCommand.COMMAND_WORD);
        assertHelpWindowOpen();

        // open help window and give it focus
        executeCommand(HelpCommand.COMMAND_WORD);
        getMainWindowHandle().focus();

        // assert that while the help window is open the UI updates correctly for a command execution
        executeCommand(SelectCommand.COMMAND_WORD + " " + INDEX_FIRST_PERSON.getOneBased());
        assertEquals("", getCommandBox().getInput());
        assertCommandBoxAndResultDisplayShowsDefaultStyle();
        assertNotEquals(HelpCommand.SHOWING_HELP_MESSAGE, getResultDisplay().getText());
        assertNotEquals(BrowserPanel.DEFAULT_PAGE, getBrowserPanel().getLoadedUrl());
        assertListMatching(getPersonListPanel(), getModel().getFilteredPersonList());

        // assert that the status bar too is updated correctly while the help window is open
        // note: the select command tested above does not update the status bar
        executeCommand(DeleteCommand.COMMAND_WORD + " " + INDEX_FIRST_PERSON.getOneBased());
        assertNotEquals(StatusBarFooter.SYNC_STATUS_INITIAL, getStatusBarFooter().getSyncStatus());
    }

    /**
     * Asserts that the help window is open, and closes it after checking.
     */
    private void assertHelpWindowOpen() {
        assertTrue(ERROR_MESSAGE, HelpWindowHandle.isWindowPresent());
        guiRobot.pauseForHuman();

        new HelpWindowHandle(guiRobot.getStage(HelpWindowHandle.HELP_WINDOW_TITLE)).close();
        getMainWindowHandle().focus();
    }

    /**
     * Asserts that the help window isn't open.
     */
    private void assertHelpWindowNotOpen() {
        assertFalse(ERROR_MESSAGE, HelpWindowHandle.isWindowPresent());
    }

    //@@author jonleeyz
    /**
     * Executes the HelpCommand using its accelerator in {@code MainMenu}
     */
    private void executeHelpCommandUsingAccelerator() {
        executeUsingAccelerator(KeyCode.F1);
    }

    /**
     * Executes the HelpCommand using its menu bar item in {@code MainMenu}.
     */
    private void executeHelpCommandUsingMenu() {
        executeUsingMenuItem("Help", "F1");
    }
    //@@author
}
