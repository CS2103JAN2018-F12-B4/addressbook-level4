//@@author zhangriqi
package seedu.address.logic.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.commons.core.Messages.MESSAGE_PERSONS_LISTED_OVERVIEW;
import static seedu.address.logic.commands.LocateCommand.MESSAGE_LOCATE_SELECT;
import static seedu.address.testutil.TypicalPersons.CARL;
import static seedu.address.testutil.TypicalPersons.DANIEL;
import static seedu.address.testutil.TypicalPersons.ELLE;
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
import seedu.address.model.person.AddressContainsKeywordsPredicate;
import seedu.address.model.person.EmailContainsKeywordsPredicate;
import seedu.address.model.person.NameContainsKeywordsPredicate;
import seedu.address.model.person.Person;
import seedu.address.model.person.PersonContainsKeywordsPredicate;
import seedu.address.model.person.PhoneContainsKeywordsPredicate;
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

    @Test
    public void execute_multipleKeywords_multiplePersonsFound() {

        int targetOne = 1;
        //test LocateCommand object that uses the PersonContainsKeyWordsPredicate
        String arguments = "carl daniel elle";
        String[] splitArguments = arguments.split("\\s+");
        List<String> list = Arrays.asList(splitArguments);
        String expectedMessage = String.format(MESSAGE_LOCATE_SELECT, targetOne);
        LocateCommand command = prepareCommand(new PersonContainsKeywordsPredicate(list));
        assertCommandSuccess(command, expectedMessage, Arrays.asList(CARL, DANIEL, ELLE));

        //test FindCommand object that uses the NameContainsKeyWordsPredicate
        arguments = "carl daniel elle";
        splitArguments = arguments.split("\\s+");
        list = Arrays.asList(splitArguments);
        expectedMessage = String.format(MESSAGE_LOCATE_SELECT, targetOne);
        command = prepareCommand(new NameContainsKeywordsPredicate(list));
        assertCommandSuccess(command, expectedMessage, Arrays.asList(CARL, DANIEL, ELLE));

        //test Command object that uses the PhoneContainsKeyWordsPredicate
        arguments = "95352563 87652533 9482224";
        splitArguments = arguments.split("\\s+");
        list = Arrays.asList(splitArguments);
        expectedMessage = String.format(MESSAGE_LOCATE_SELECT, targetOne);
        command = prepareCommand(new PhoneContainsKeywordsPredicate(list));
        assertCommandSuccess(command, expectedMessage, Arrays.asList(CARL, DANIEL, ELLE));

        //test FindCommand object that uses the EmailContainsKeyWordsPredicate
        arguments = "heinz@example.com cornelia@example.com werner@example.com";
        splitArguments = arguments.split("\\s+");
        list = Arrays.asList(splitArguments);
        expectedMessage = String.format(MESSAGE_LOCATE_SELECT, targetOne);
        command = prepareCommand(new EmailContainsKeywordsPredicate(list));
        assertCommandSuccess(command, expectedMessage, Arrays.asList(CARL, DANIEL, ELLE));

        //test FindCommand object that uses the AddressContainsKeyWordsPredicate
        arguments = "wall 10th michegan";
        splitArguments = arguments.split("\\s+");
        list = Arrays.asList(splitArguments);
        expectedMessage = String.format(MESSAGE_LOCATE_SELECT, targetOne);
        command = prepareCommand(new AddressContainsKeywordsPredicate(list));
        assertCommandSuccess(command, expectedMessage, Arrays.asList(CARL, DANIEL, ELLE));
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
