package seedu.address.logic.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.commons.core.Messages.MESSAGE_PERSONS_LISTED_OVERVIEW;
import static seedu.address.testutil.TypicalPersons.CARL;
import static seedu.address.testutil.TypicalPersons.DANIEL;
import static seedu.address.testutil.TypicalPersons.ELLE;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

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

/**
 * Contains integration tests (interaction with the Model) for {@code FindCommand}.
 */
public class FindCommandTest {
    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void equals() {
        PersonContainsKeywordsPredicate firstPredicate =
                new PersonContainsKeywordsPredicate(Collections.singletonList("first"));
        PersonContainsKeywordsPredicate secondPredicate =
                new PersonContainsKeywordsPredicate(Collections.singletonList("second"));

        FindCommand findFirstCommand = new FindCommand(firstPredicate);
        FindCommand findSecondCommand = new FindCommand(secondPredicate);

        // same object -> returns true
        assertTrue(findFirstCommand.equals(findFirstCommand));

        // same values -> returns true
        FindCommand findFirstCommandCopy = new FindCommand(firstPredicate);
        assertTrue(findFirstCommand.equals(findFirstCommandCopy));

        // different types -> returns false
        assertFalse(findFirstCommand.equals(1));

        // null -> returns false
        assertFalse(findFirstCommand.equals(null));

        // different person -> returns false
        assertFalse(findFirstCommand.equals(findSecondCommand));
    }

    @Test
    public void execute_zeroKeywords_noPersonFound() {

        String arguments = " ";
        String[] splitArguments = arguments.split("\\s+");
        List<String> list = Arrays.asList(splitArguments);
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 0);
        FindCommand command = prepareCommand(new PersonContainsKeywordsPredicate(list));
        assertCommandSuccess(command, expectedMessage, Collections.emptyList());
    }

    @Test
    public void execute_multipleKeywords_multiplePersonsFound() {

        //test FindCommand object that uses the PersonContainsKeyWordsPredicate
        String arguments = "carl daniel elle";
        String[] splitArguments = arguments.split("\\s+");
        List<String> list = Arrays.asList(splitArguments);
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 3);
        FindCommand command = prepareCommand(new PersonContainsKeywordsPredicate(list));
        assertCommandSuccess(command, expectedMessage, Arrays.asList(CARL, DANIEL, ELLE));

        //test FindCommand object that uses the NameContainsKeyWordsPredicate
        arguments = "carl daniel elle";
        splitArguments = arguments.split("\\s+");
        list = Arrays.asList(splitArguments);
        expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 3);
        command = prepareCommand(new NameContainsKeywordsPredicate(list));
        assertCommandSuccess(command, expectedMessage, Arrays.asList(CARL, DANIEL, ELLE));

        //test Command object that uses the PhoneContainsKeyWordsPredicate
        arguments = "95352563 87652533 9482224";
        splitArguments = arguments.split("\\s+");
        list = Arrays.asList(splitArguments);
        expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 3);
        command = prepareCommand(new PhoneContainsKeywordsPredicate(list));
        assertCommandSuccess(command, expectedMessage, Arrays.asList(CARL, DANIEL, ELLE));

        //test FindCommand object that uses the EmailContainsKeyWordsPredicate
        arguments = "heinz@example.com cornelia@example.com werner@example.com";
        splitArguments = arguments.split("\\s+");
        list = Arrays.asList(splitArguments);
        expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 3);
        command = prepareCommand(new EmailContainsKeywordsPredicate(list));
        assertCommandSuccess(command, expectedMessage, Arrays.asList(CARL, DANIEL, ELLE));

        //test FindCommand object that uses the AddressContainsKeyWordsPredicate
        arguments = "wall 10th michegan";
        splitArguments = arguments.split("\\s+");
        list = Arrays.asList(splitArguments);
        expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 3);
        command = prepareCommand(new AddressContainsKeywordsPredicate(list));
        assertCommandSuccess(command, expectedMessage, Arrays.asList(CARL, DANIEL, ELLE));
    }

    /**
     * Parses {@code userInput} into a {@code FindCommand}.
     */
    private FindCommand prepareCommand(Predicate<Person> predicate) {
        FindCommand command =
                new FindCommand(predicate);
        command.setData(model, new CommandHistory(), new UndoRedoStack());
        return command;
    }

    /**
     * Asserts that {@code command} is successfully executed, and<br>
     *     - the command feedback is equal to {@code expectedMessage}<br>
     *     - the {@code FilteredList<Person>} is equal to {@code expectedList}<br>
     *     - the {@code AddressBook} in model remains the same after executing the {@code command}
     */
    private void assertCommandSuccess(FindCommand command, String expectedMessage, List<Person> expectedList) {
        AddressBook expectedAddressBook = new AddressBook(model.getAddressBook());
        CommandResult commandResult = command.execute();

        assertEquals(expectedMessage, commandResult.feedbackToUser);
        assertEquals(expectedList, model.getFilteredPersonList());
        assertEquals(expectedAddressBook, model.getAddressBook());
    }
}
