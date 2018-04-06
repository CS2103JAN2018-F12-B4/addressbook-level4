package seedu.address.model.person.runner;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.tag.Tag;

//@@author melvintzw
/**
 * Represents a runner in the address book.
 */
public class Runner extends Person {
    private final List<Person> customers;

    public Runner() {
        super();
        this.customers = new ArrayList<>();
        this.setType(PersonType.RUNNER);
    }

    public Runner(Name name, Phone phone, Email email, Address address, Set<Tag> tags, List<Person> customers) {
        super(name, phone, email, address, tags);
        this.setType(PersonType.RUNNER);
        this.customers = customers;
    }

    public List<Person> getCustomers() {
        return customers;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Runner)) {
            return false;
        }

        Runner otherPerson = (Runner) other;
        return otherPerson.getName().equals(this.getName())
                && otherPerson.getPhone().equals(this.getPhone())
                && otherPerson.getEmail().equals(this.getEmail())
                && otherPerson.getAddress().equals(this.getAddress());

    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("Name: ")
                .append(getName())
                .append(" Phone: ")
                .append(getPhone())
                .append(" Email: ")
                .append(getEmail())
                .append(" Address: ")
                .append(getAddress())
                .append(" Tags: ");
        getTags().forEach(builder::append);
        builder.append("\n");
        builder.append("Customers:\n");
        for (Person customer : customers) {
            builder.append(customer.getName());
            builder.append(" ");
        }
        return builder.toString();
    }
}
