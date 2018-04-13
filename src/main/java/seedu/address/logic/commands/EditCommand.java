package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_INTEREST;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MONEYOWED;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_OWEDUEDATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_OWESTARTDATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import seedu.address.commons.core.EventsCenter;
import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.commons.events.ui.FieldsChangedEvent;
import seedu.address.commons.events.ui.JumpToListRequestEvent;
import seedu.address.commons.util.CollectionUtil;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.customer.Customer;
import seedu.address.model.person.customer.LateInterest;
import seedu.address.model.person.customer.MoneyBorrowed;
import seedu.address.model.person.customer.StandardInterest;
import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.model.person.exceptions.PersonNotFoundException;
import seedu.address.model.person.runner.Runner;
import seedu.address.model.tag.Tag;

/**
 * Edits the details of an existing person in the address book.
 */
public class EditCommand extends UndoableCommand implements PopulatableCommand {

    public static final String COMMAND_WORD = "edit";
    public static final String COMMAND_ALIAS = "e";

    public static final String MESSAGE_USAGE =
            COMMAND_WORD + " | Edits the details of the person identified "
                    + "by the index number used in the last person listing."
                    + "\n\tEditable fields are NAME, PHONE, EMAIL, ADDRESS, MONEY_BORROWED, WEEKLY_INTEREST, "
                    + "OWE_START_DATE, "
                    + "OWE_DUE_DATE."
                    + "\n\t"
                    + "Existing values will be overwritten by the input values. "
                    + "Refer to the User Guide (press \"F1\") for detailed information about this command!"

                    + "\n\t"
                    + "Parameters:\t"
                    + COMMAND_WORD + " "
                    + "INDEX (must be a positive integer) "
                    + "[" + PREFIX_NAME + " NAME] "
                    + "[" + PREFIX_PHONE + " PHONE] "
                    + "[" + PREFIX_EMAIL + " EMAIL] "
                    + "[" + PREFIX_ADDRESS + " ADDRESS] "
                    + "[" + PREFIX_TAG + " TAG]"
                    + "\n\t[" + PREFIX_MONEYOWED + " MONEY_BORROWED] "
                    + "[" + PREFIX_INTEREST + " WEEKLY_INTEREST] "
                    + "[" + PREFIX_OWESTARTDATE + " OWE_START_DATE] "
                    + "[" + PREFIX_OWEDUEDATE + " OWE_DUE_DATE] "

                    + "\n\t"
                    + "Example:\t\t"
                    + COMMAND_WORD + " 1 "
                    + PREFIX_PHONE + " 999 "
                    + PREFIX_EMAIL + " ahlong@houseofhuat.com";

    public static final String MESSAGE_EDIT_PERSON_SUCCESS = "Edited Person:\n\n%1$s";
    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";
    public static final String MESSAGE_DUPLICATE_PERSON = "This person already exists in the address book.";

    private final Index index;
    private final EditPersonDescriptor editPersonDescriptor;

    private Person personToEdit;
    private Person editedPerson;

    /**
     * @param index                of the person in the filtered person list to edit
     * @param editPersonDescriptor details to edit the person with
     */
    public EditCommand(Index index, EditPersonDescriptor editPersonDescriptor) {
        requireNonNull(index);
        requireNonNull(editPersonDescriptor);

        this.index = index;
        this.editPersonDescriptor = new EditPersonDescriptor(editPersonDescriptor);
    }

    /**
     * For call in PopulatePrefixRequestEvent class, to assign string values.
     */
    public EditCommand() {
        index = null;
        editPersonDescriptor = null;
    }


    @Override
    public CommandResult executeUndoableCommand() throws CommandException {
        try {
            model.updatePerson(personToEdit, editedPerson);
            EventsCenter.getInstance().post(new JumpToListRequestEvent(index));
            EventsCenter.getInstance().post(new FieldsChangedEvent(editedPerson));
        } catch (DuplicatePersonException dpe) {
            throw new CommandException(MESSAGE_DUPLICATE_PERSON);
        } catch (PersonNotFoundException pnfe) {
            throw new AssertionError("The target person cannot be missing");
        }
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        return new CommandResult(String.format(MESSAGE_EDIT_PERSON_SUCCESS, editedPerson));
    }

    @Override
    protected void preprocessUndoableCommand() throws CommandException {
        List<Person> lastShownList = model.getFilteredPersonList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        personToEdit = lastShownList.get(index.getZeroBased());
        editedPerson = createEditedPerson(personToEdit, editPersonDescriptor);
    }

    /**
     * Creates and returns a {@code Person} with the details of {@code personToEdit}
     * edited with {@code editPersonDescriptor}.
     */
    public static Person createEditedPerson(Person personToEdit, EditPersonDescriptor editPersonDescriptor) throws
            CommandException {

        assert personToEdit != null;

        Name updatedName = editPersonDescriptor.getName().orElse(personToEdit.getName());
        Phone updatedPhone = editPersonDescriptor.getPhone().orElse(personToEdit.getPhone());
        Email updatedEmail = editPersonDescriptor.getEmail().orElse(personToEdit.getEmail());
        Address updatedAddress = editPersonDescriptor.getAddress().orElse(personToEdit.getAddress());
        Set<Tag> updatedTags = editPersonDescriptor.getTags().orElse(personToEdit.getTags());

        //@@author melvintzw
        if (personToEdit instanceof Customer) {

            MoneyBorrowed moneyBorrowed = editPersonDescriptor.getMoneyBorrowed().orElse(((Customer) personToEdit)
                    .getMoneyBorrowed());
            Date oweStartDate = editPersonDescriptor.getOweStartDate().orElse(((Customer) personToEdit)
                    .getOweStartDate());
            Date oweDueDate = editPersonDescriptor.getOweDueDate().orElse(((Customer) personToEdit)
                    .getOweDueDate());
            StandardInterest standardInterest = editPersonDescriptor.getStandardInterest()
                    .orElse(((Customer) personToEdit).getStandardInterest());
            LateInterest lateInterest = editPersonDescriptor.getLateInterest().orElse(((Customer) personToEdit)
                    .getLateInterest());
            Person runner = editPersonDescriptor.getRunner().orElse(((Customer) personToEdit)
                    .getRunner());

            if (oweDueDate.compareTo(oweStartDate) < 0) {
                throw new CommandException("OWE_DUE_DATE cannot be before OWE_START_DATE");
            }

            return new Customer(updatedName, updatedPhone, updatedEmail, updatedAddress, updatedTags, moneyBorrowed,
                    oweStartDate, oweDueDate, standardInterest, lateInterest, runner);

        } else if (personToEdit instanceof Runner) {

            if (editPersonDescriptor.getStandardInterest().isPresent()
                    || editPersonDescriptor.getMoneyBorrowed().isPresent()
                    || editPersonDescriptor.getOweStartDate().isPresent()
                    || editPersonDescriptor.getOweDueDate().isPresent()) {
                throw new CommandException("Cannot edit Runner using Customer-only fields");
            }

            List<Person> customers = editPersonDescriptor.getCustomers().orElse(((Runner) personToEdit)
                    .getCustomers());

            return new Runner(updatedName, updatedPhone, updatedEmail, updatedAddress, updatedTags, customers);

        } else {
            throw new CommandException("Error: Invalid Person");
        }
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof EditCommand)) {
            return false;
        }

        // state check
        EditCommand e = (EditCommand) other;
        return index.equals(e.index)
                && editPersonDescriptor.equals(e.editPersonDescriptor)
                && Objects.equals(personToEdit, e.personToEdit);
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

    //@@author melvintzw-reused

    /**
     * Stores the details to edit the person with. Each non-empty field value will replace the
     * corresponding field value of the person.
     */
    public static class EditPersonDescriptor {
        private Name name;
        private Phone phone;
        private Email email;
        private Address address;
        private Set<Tag> tags;

        //Customer fields
        private MoneyBorrowed moneyBorrowed;
        private Date oweStartDate;
        private Date oweDueDate;
        private StandardInterest standardInterest;
        private LateInterest lateInterest;
        private Person runner;

        //Runner fields
        private List<Person> customers;

        public EditPersonDescriptor() {
        }

        /**
         * Copy constructor.
         * A defensive copy of {@code tags} is used internally.
         */
        public EditPersonDescriptor(EditPersonDescriptor toCopy) {
            setName(toCopy.name);
            setPhone(toCopy.phone);
            setEmail(toCopy.email);
            setAddress(toCopy.address);
            setTags(toCopy.tags);

            setMoneyBorrowed(toCopy.moneyBorrowed);
            setOweStartDate(toCopy.oweStartDate);
            setOweDueDate(toCopy.oweDueDate);
            setStandardInterest(toCopy.standardInterest);
            setLateInterest(toCopy.lateInterest);
            setRunner(toCopy.runner);

            setCustomers(toCopy.customers);
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(this.name, this.phone, this.email, this.address, this.tags,
                    this.moneyBorrowed, this.oweStartDate, this.oweDueDate, this.standardInterest, this.lateInterest,
                    this.runner);
        }

        public void setName(Name name) {
            this.name = name;
        }

        public Optional<Name> getName() {
            return Optional.ofNullable(name);
        }

        public void setPhone(Phone phone) {
            this.phone = phone;
        }

        public Optional<Phone> getPhone() {
            return Optional.ofNullable(phone);
        }

        public void setEmail(Email email) {
            this.email = email;
        }

        public Optional<Email> getEmail() {
            return Optional.ofNullable(email);
        }

        public void setAddress(Address address) {
            this.address = address;
        }

        public Optional<Address> getAddress() {
            return Optional.ofNullable(address);
        }

        public void setMoneyBorrowed(MoneyBorrowed moneyBorrowed) {
            this.moneyBorrowed = moneyBorrowed;
        }

        public Optional<MoneyBorrowed> getMoneyBorrowed() {
            return Optional.ofNullable(moneyBorrowed);
        }

        public void setOweStartDate(Date oweStartDate) {
            this.oweStartDate = oweStartDate;
        }

        public Optional<Date> getOweStartDate() {
            return Optional.ofNullable(oweStartDate);
        }

        public void setOweDueDate(Date oweDueDate) {
            this.oweDueDate = oweDueDate;
        }

        public Optional<Date> getOweDueDate() {
            return Optional.ofNullable(oweDueDate);
        }

        public void setStandardInterest(StandardInterest standardInterest) {
            this.standardInterest = standardInterest;
        }

        public Optional<StandardInterest> getStandardInterest() {
            return Optional.ofNullable(standardInterest);
        }

        public void setLateInterest(LateInterest lateInterest) {
            this.lateInterest = lateInterest;
        }

        public Optional<LateInterest> getLateInterest() {
            return Optional.ofNullable(lateInterest);
        }

        public void setRunner(Person runner) {
            this.runner = runner;
        }

        public Optional<Person> getRunner() {
            return Optional.ofNullable(runner);
        }

        public void setCustomers(List<Person> customers) {
            this.customers = customers;
        }

        public Optional<List<Person>> getCustomers() {
            return Optional.ofNullable(customers);
        }

        /**
         * Sets {@code tags} to this object's {@code tags}.
         * A defensive copy of {@code tags} is used internally.
         */
        public void setTags(Set<Tag> tags) {
            this.tags = (tags != null) ? new HashSet<>(tags) : null;
        }

        /**
         * Returns an unmodifiable tag set, which throws {@code UnsupportedOperationException}
         * if modification is attempted.
         * Returns {@code Optional#empty()} if {@code tags} is null.
         */
        public Optional<Set<Tag>> getTags() {
            return (tags != null) ? Optional.of(Collections.unmodifiableSet(tags)) : Optional.empty();
        }

        @Override
        public boolean equals(Object other) {
            // short circuit if same object
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof EditPersonDescriptor)) {
                return false;
            }

            // state check
            EditPersonDescriptor e = (EditPersonDescriptor) other;

            return getName().equals(e.getName())
                    && getPhone().equals(e.getPhone())
                    && getEmail().equals(e.getEmail())
                    && getAddress().equals(e.getAddress())
                    && getTags().equals(e.getTags());
            //TODO: add .equals for Runner and Customer
        }
    }
    //@@author
}
