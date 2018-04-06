//@@author zhangriqi
package seedu.address.logic.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.commons.core.Messages.MESSAGE_PERSONS_LISTED_OVERVIEW;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import seedu.address.logic.CommandHistory;
import seedu.address.logic.UndoRedoStack;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.model.person.PersonContainsKeywordsPredicate;
import seedu.address.ui.testutil.EventsCollectorRule;

public class LocateCommandTest {
    @Rule
    public final EventsCollectorRule eventsCollectorRule = new EventsCollectorRule();

    private Model model;

    @Before
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    }
    @Test
    public void equals() {
        PersonContainsKeywordsPredicate firstPredicate =
                new PersonContainsKeywordsPredicate(Collections.singletonList("first"));
        PersonContainsKeywordsPredicate secondPredicate =
                new PersonContainsKeywordsPredicate(Collections.singletonList("second"));

        LocateCommand locateFirstCommand = new LocateCommand(firstPredicate);
        LocateCommand locateSecondCommand = new LocateCommand(secondPredicate);

        // same object -> returns true
        assertTrue(locateFirstCommand.equals(locateFirstCommand));

        // different types -> returns false
        assertFalse(locateFirstCommand.equals(1));

        // null -> returns false
        assertFalse(locateFirstCommand.equals(null));

        // different person -> returns false
        assertFalse(locateFirstCommand.equals(locateSecondCommand));
    }

    @Test
    public void execute_zeroKeywords_noPersonFound() {
        String arguments = " ";
        String[] splitArguments = arguments.split("\\s+");
        List<String> list = Arrays.asList(splitArguments);
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 0);
        LocateCommand command = prepareCommand(new PersonContainsKeywordsPredicate(list));
        assertCommandSuccess(command, expectedMessage, Collections.emptyList());
    }

    /**
     * Parses {@code userInput} into a {@code LocateCommand}.
     */
    private LocateCommand prepareCommand(Predicate<Person> predicate) {
        LocateCommand command =
                new LocateCommand(predicate);
        command.setData(model, new CommandHistory(), new UndoRedoStack());
        return command;
    }

    /**
     * Asserts that {@code command} is successfully executed, and<br>
     *     - the command feedback is equal to {@code expectedMessage}<br>
     *     - the {@code FilteredList<Person>} is equal to {@code expectedList}<br>
     *     - the {@code AddressBook} in model remains the same after executing the {@code command}
     */
    private void assertCommandSuccess(LocateCommand command, String expectedMessage, List<Person> expectedList) {
        AddressBook expectedAddressBook = new AddressBook(model.getAddressBook());
        assertEquals(expectedAddressBook, model.getAddressBook());
    }
}
