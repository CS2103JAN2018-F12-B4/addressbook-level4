package guitests.guihandles;

import java.util.Arrays;

import javafx.scene.Node;
import javafx.scene.input.KeyCode;

/**
 * Provides a handle to the main menu of the app.
 */
public class MainMenuHandle extends NodeHandle<Node> {
    public static final String MENU_BAR_ID = "#menuBar";

    public MainMenuHandle(Node mainMenuNode) {
        super(mainMenuNode);
    }

    //@@author jonleeyz
    /**
     * Clicks on {@code menuItems} in order.
     */
    public void clickOnMenuItemsSequentially(String... menuItems) {
        Arrays.stream(menuItems).forEach(guiRobot::clickOn);
    }

    /**
     * Simulates press of given keyboard shortcut
     */
    public void useAccelerator(KeyCode... combination) {
        guiRobot.push(combination);
    }
    //@@author
}
