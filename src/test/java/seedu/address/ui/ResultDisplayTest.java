package seedu.address.ui;

import static org.junit.Assert.assertEquals;
import static seedu.address.testutil.EventsUtil.postNow;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import guitests.guihandles.ResultDisplayHandle;
import seedu.address.commons.events.ui.NewResultAvailableEvent;

public class ResultDisplayTest extends GuiUnitTest {

    //@@author jonleeyz-reused
    private static final NewResultAvailableEvent NEW_RESULT_SUCCESS_EVENT_STUB =
            new NewResultAvailableEvent("Stub", true);
    private static final NewResultAvailableEvent NEW_RESULT_FAILURE_EVENT_STUB =
            new NewResultAvailableEvent("Stub", false);

    private List<String> defaultStyleOfResultDisplay;
    private List<String> errorStyleOfResultDisplay;

    private ResultDisplayHandle resultDisplayHandle;
    //@@author

    @Before
    public void setUp() {
        ResultDisplay resultDisplay = new ResultDisplay();
        uiPartRule.setUiPart(resultDisplay);

        resultDisplayHandle = new ResultDisplayHandle(getChildNode(resultDisplay.getRoot(),
                ResultDisplayHandle.RESULT_DISPLAY_ID));

        defaultStyleOfResultDisplay = new ArrayList<>(resultDisplayHandle.getStyleClass());

        errorStyleOfResultDisplay = new ArrayList<>(defaultStyleOfResultDisplay);
        errorStyleOfResultDisplay.add(ResultDisplay.ERROR_STYLE_CLASS);
    }

    //@@author jonleeyz-reused
    @Test
    public void display() {
        // default result text
        guiRobot.pauseForHuman();
        assertEquals("", resultDisplayHandle.getText());
        assertEquals(defaultStyleOfResultDisplay, resultDisplayHandle.getStyleClass());

        // receiving new results
        assertResultDisplay(NEW_RESULT_SUCCESS_EVENT_STUB);
        assertResultDisplay(NEW_RESULT_FAILURE_EVENT_STUB);
    }

    /**
     * Posts the {@code event} to the {@code EventsCentre}, then verifies that <br>
     *      - the text on the result display matches the {@code event}'s message <br>
     *      - the result display's style is the same as {@code defaultStyleOfResultDisplay} if event is successful,
     *      - {@code errorStyleOfResultDisplay} otherwise.
     */
    private void assertResultDisplay(NewResultAvailableEvent event) {
        postNow(event);
        guiRobot.pauseForHuman();
        List<String> expectedStyleClass = event.isSuccessful()
                ? defaultStyleOfResultDisplay
                : errorStyleOfResultDisplay;

        assertEquals(event.message, resultDisplayHandle.getText());
        assertEquals(expectedStyleClass, resultDisplayHandle.getStyleClass());
    }
    //@@author
}
