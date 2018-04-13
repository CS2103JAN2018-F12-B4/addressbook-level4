package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.commands.EditCommand.createEditedPerson;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.person.Person;
import seedu.address.model.person.customer.Customer;
import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.model.person.exceptions.PersonNotFoundException;
import seedu.address.model.person.runner.Runner;

/**
 * Deletes a person identified using it's last displayed index from the address book.
 */
public class DeleteCommand extends UndoableCommand implements PopulatableCommand {

    public static final String COMMAND_WORD = "delete";
    public static final String COMMAND_ALIAS = "d";

    public static final String MESSAGE_USAGE =
            COMMAND_WORD + " | Deletes the person associated with the index number used in the last person listing. "
            + "Refer to the User Guide (press \"F1\") for detailed information about this command!"

            + "\n\t"
            + "Parameters:\t"
            + COMMAND_WORD + " "
            + "INDEX (must be a positive integer)"

            + "\n\t"
            + "Example:\t\t" + COMMAND_WORD + " 1";

    public static final String MESSAGE_DELETE_PERSON_SUCCESS = "Deleted Person:\n\n%1$s";

    private final Index targetIndex;

    private Person personToDelete;

    public DeleteCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }

    /**
     * For call in PopulatePrefixRequestEvent class, to assign string values.
     */
    public DeleteCommand() {
        targetIndex = null;
    }


    @Override
    public CommandResult executeUndoableCommand() throws CommandException {
        requireNonNull(personToDelete);
        try {
            //if personToDelete is customer, delete his associated runner's customer
            if (personToDelete instanceof Customer) {
                deleteAssocRunnersCustomer();
            }
            //if personToDelete is runner, delete all his customer's runner
            if (personToDelete instanceof Runner) {
                deleteAssocCustomersRunner();
            }

            model.deletePerson(personToDelete);
        } catch (PersonNotFoundException pnfe) {
            throw new AssertionError("The target person cannot be missing");
        } catch (DuplicatePersonException dpe) {
            throw new AssertionError("duplicate person found");
        }

        return new CommandResult(String.format(MESSAGE_DELETE_PERSON_SUCCESS, personToDelete));
    }

    @Override
    protected void preprocessUndoableCommand() throws CommandException {
        List<Person> lastShownList = model.getFilteredPersonList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        personToDelete = lastShownList.get(targetIndex.getZeroBased());
    }

    /**
     * Pre-condition: personToDelete is a Runner.
     * This method finds associated customers from the addressbook and deletes those customer's runner
     */
    private void deleteAssocCustomersRunner() throws CommandException, DuplicatePersonException,
            PersonNotFoundException {
        assert personToDelete instanceof Runner;
        List<Person> customers = ((Runner) personToDelete).getCustomers();
        for (Person c : customers) {

            EditCommand.EditPersonDescriptor custDesc = new EditCommand.EditPersonDescriptor();

            custDesc.setRunner(new Runner());

            custDesc.setName(c.getName());
            custDesc.setPhone(c.getPhone());
            custDesc.setEmail(c.getEmail());
            custDesc.setAddress(c.getAddress());
            custDesc.setTags(c.getTags());

            custDesc.setMoneyBorrowed(((Customer) c).getMoneyBorrowed());
            custDesc.setOweStartDate(((Customer) c).getOweStartDate());
            custDesc.setOweDueDate(((Customer) c).getOweDueDate());
            custDesc.setStandardInterest(((Customer) c).getStandardInterest());
            custDesc.setLateInterest(((Customer) c).getLateInterest());

            Person editedCust = createEditedPerson(c, custDesc);
            model.updatePerson(c, editedCust);
        }
    }

    /**
     * Pre-condition: personToDelete is a Customer.
     * This method finds this customer's associated Runner from the addressbook and deletes this customer from that
     * runner.
     */
    private void deleteAssocRunnersCustomer() throws CommandException, DuplicatePersonException,
            PersonNotFoundException {
        assert personToDelete instanceof Customer;

        List<Person> pl = model.getAddressBook().getPersonList();

        Person r = ((Customer) personToDelete).getRunner(); //not getting a runner from pl but an incomplete copy
        int indexOfActualRunner = pl.indexOf(r);

        if (indexOfActualRunner >= 0) {
            //the conditional check is necessary so that I'm only modifying valid existing runners

            Person actualRunner = pl.get(indexOfActualRunner); //getting the actual complete runner from pl

            //generate editPersonDescriptor with customer removed from runner's customer list
            EditCommand.EditPersonDescriptor runnerDescWCustRemoved = new EditCommand.EditPersonDescriptor();

            runnerDescWCustRemoved.setName(actualRunner.getName());
            runnerDescWCustRemoved.setPhone(actualRunner.getPhone());
            runnerDescWCustRemoved.setEmail(actualRunner.getEmail());
            runnerDescWCustRemoved.setAddress(actualRunner.getAddress());
            runnerDescWCustRemoved.setTags(actualRunner.getTags());

            List<Person> customers = new ArrayList<>(); //defensive copy of runner's customer list
            customers.addAll(((Runner) actualRunner).getCustomers());
            customers.remove(personToDelete);
            runnerDescWCustRemoved.setCustomers(customers);

            Person editedRunner = createEditedPerson((Runner) actualRunner, runnerDescWCustRemoved);
            model.updatePerson(actualRunner, editedRunner);
        }

    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof DeleteCommand // instanceof handles nulls
                && this.targetIndex.equals(((DeleteCommand) other).targetIndex) // state check
                && Objects.equals(this.personToDelete, ((DeleteCommand) other).personToDelete));
    }

    //@@author jonleeyz
    @Override
    public String getCommandWord() {
        return COMMAND_WORD;
    }

    @Override
    public String getTemplate() {
        return COMMAND_WORD + " ";
    }

    @Override
    public int getCaretIndex() {
        return getTemplate().length();
    }

    @Override
    public String getUsageMessage() {
        return MESSAGE_USAGE;
    }
    //@@author
}
