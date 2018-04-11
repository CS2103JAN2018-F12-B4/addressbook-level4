package seedu.address.model.person.customer;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.runner.Runner;
import seedu.address.model.tag.Tag;

//@@author melvintzw
/**
 * Represents a customer in the address book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Customer extends Person {

    private final MoneyBorrowed moneyBorrowed;
    private final Date oweStartDate;
    private final Date oweDueDate;
    private final StandardInterest standardInterest; //in percent
    private final LateInterest lateInterest; //in percent
    private final Person runner;

    /**
     * customer constructor
     */
    public Customer() {
        super();
        this.setType(PersonType.CUSTOMER);
        this.moneyBorrowed = new MoneyBorrowed();
        this.oweStartDate = new Date(0);
        this.oweDueDate = new Date(0);
        this.standardInterest = new StandardInterest();
        this.lateInterest = new LateInterest();
        this.runner = new Runner();
    }

    public Customer(Name name, Phone phone, Email email, Address address, Set<Tag> tags,
                    MoneyBorrowed moneyBorrowed, Date oweStartDate, Date oweDueDate, StandardInterest
                            standardInterest, LateInterest lateInterest, Person runner) {
        super(name, phone, email, address, tags);
        this.setType(PersonType.CUSTOMER);
        this.moneyBorrowed = moneyBorrowed;
        this.standardInterest = standardInterest;
        this.lateInterest = lateInterest;
        this.oweStartDate = oweStartDate;
        this.oweDueDate = oweDueDate;
        this.runner = runner;
    }

    public MoneyBorrowed getMoneyBorrowed() {
        return moneyBorrowed;
    }

    public StandardInterest getStandardInterest() {
        return standardInterest;
    }

    public Date getOweStartDate() {
        return oweStartDate;
    }

    public Date getOweDueDate() {
        return oweDueDate;
    }

    public LateInterest getLateInterest() {
        return lateInterest;
    }

    public Person getRunner() {
        return runner;
    }

    /**
     * @return amount of money owed, after compounded standardInterest, based on num of weeks that has passed since
     * oweStartDate
     */
    public double getMoneyCurrentlyOwed() {
        final int numOfMsPerWeek = 60 * 60 * 24 * 7 * 1000; //10080 seconds per week; 1000 ms per second

        Date currentDate = new Date();
        long elapsedTime = currentDate.getTime() - oweStartDate.getTime();
        if (elapsedTime < 0) {
            return moneyBorrowed.value;
        }
        long elapsedWeeks = elapsedTime / numOfMsPerWeek;
        return moneyBorrowed.value * Math.pow(1 + standardInterest.value / 100, (double) elapsedWeeks);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Customer)) {
            return false;
        }

        Customer otherPerson = (Customer) other;
        return otherPerson.getName().equals(this.getName())
                && otherPerson.getPhone().equals(this.getPhone())
                && otherPerson.getEmail().equals(this.getEmail())
                && otherPerson.getAddress().equals(this.getAddress());
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("Name: ")
                .append(getName() + ";")
                .append(" Phone: ")
                .append(getPhone() + ";")
                .append(" Email: ")
                .append(getEmail() + ";")
                .append(" Address: ")
                .append(getAddress() + ";")
                .append(" Tags: ");
        getTags().forEach(builder::append);

        SimpleDateFormat simpledate = new SimpleDateFormat("EEE, d MMM yyyy");
        String oweStartDate = simpledate.format(getOweStartDate());
        String oweDueDate = simpledate.format(getOweDueDate());

        builder.append("\nMoney Owed: ")
                .append(String.format("$%.2f", getMoneyCurrentlyOwed()))
                .append(" Weekly Interest Rate: ")
                .append(getStandardInterest() + "%" + ";")
                .append(" Start Date: ")
                .append(oweStartDate + ";")
                .append(" Due Date: ")
                .append(oweDueDate)
                .append("\nRunner Assigned: ")
                .append(runner.getName());
        return builder.toString();
    }
}
