package seedu.address.testutil;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.customer.Customer;
import seedu.address.model.person.customer.LateInterest;
import seedu.address.model.person.customer.MoneyBorrowed;
import seedu.address.model.person.customer.StandardInterest;
import seedu.address.model.person.runner.Runner;
import seedu.address.model.tag.Tag;
import seedu.address.model.util.SampleDataUtil;

//@@author melvintzw
/**
 * A utility class to help with building Person objects.
 */
public class PersonBuilder {

    public static final String DEFAULT_NAME = "Alice Pauline";
    public static final String DEFAULT_PHONE = "85355255";
    public static final String DEFAULT_EMAIL = "alice@gmail.com";
    public static final String DEFAULT_ADDRESS = "123, Jurong West Ave 6, #08-111";
    public static final String DEFAULT_TAGS = "friends";

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
    private Runner runner;

    //Runner fields:
    private List<Customer> customers;

    public PersonBuilder() {
        name = new Name(DEFAULT_NAME);
        phone = new Phone(DEFAULT_PHONE);
        email = new Email(DEFAULT_EMAIL);
        address = new Address(DEFAULT_ADDRESS);
        tags = SampleDataUtil.getTagSet(DEFAULT_TAGS);

        //Customer fields
        moneyBorrowed = new MoneyBorrowed();
        oweStartDate = new Date(0);
        oweDueDate = new Date(0);
        standardInterest = new StandardInterest();
        lateInterest = new LateInterest();
        runner = new Runner();

        //Runner fields:
        customers = new ArrayList<>();
    }

    /**
     * Initializes the PersonBuilder with the data of {@code personToCopy}.
     */
    public PersonBuilder(Person personToCopy) {
        name = personToCopy.getName();
        phone = personToCopy.getPhone();
        email = personToCopy.getEmail();
        address = personToCopy.getAddress();
        tags = new HashSet<>(personToCopy.getTags());

        //TODO: change the instantiation of below variables according to instanceof
        moneyBorrowed = new MoneyBorrowed();
        oweStartDate = new Date(0);
        oweDueDate = new Date(0);
        standardInterest = new StandardInterest();
        lateInterest = new LateInterest();
        runner = new Runner();

        customers = new ArrayList<>();
    }

    /**
     * Sets the {@code Name} of the {@code Person} that we are building.
     */
    public PersonBuilder withName(String name) {
        this.name = new Name(name);
        return this;
    }

    /**
     * Parses the {@code tags} into a {@code Set<Tag>} and set it to the {@code Person} that we are building.
     */
    public PersonBuilder withTags(String ... tags) {
        this.tags = SampleDataUtil.getTagSet(tags);
        return this;
    }

    /**
     * Sets the {@code Address} of the {@code Person} that we are building.
     */
    public PersonBuilder withAddress(String address) {
        this.address = new Address(address);
        return this;
    }

    /**
     * Sets the {@code Phone} of the {@code Person} that we are building.
     */
    public PersonBuilder withPhone(String phone) {
        this.phone = new Phone(phone);
        return this;
    }

    /**
     * Sets the {@code Email} of the {@code Person} that we are building.
     */
    public PersonBuilder withEmail(String email) {
        this.email = new Email(email);
        return this;
    }

    /**
     * Sets the {@code MoneyBorrowed} of the {@code Person} that we are building.
     */
    public PersonBuilder withMoneyBorrowed(MoneyBorrowed moneyBorrowed) {
        this.moneyBorrowed = moneyBorrowed;
        return this;
    }

    /**
     * Sets the {@code OweStartDate} of the {@code Person} that we are building.
     */
    public PersonBuilder withOweStartDate(Date date) {
        this.oweStartDate = date;
        return this;
    }

    /**
     * Sets the {@code OweDueDate} of the {@code Person} that we are building.
     */
    public PersonBuilder withOweDueDate(Date date) {
        this.oweDueDate = date;
        return this;
    }

    /**
     * Sets the {@code StandardInterest} of the {@code Person} that we are building.
     */
    public PersonBuilder withStandardInterest(StandardInterest interest) {
        this.standardInterest = interest;
        return this;
    }

    /**
     * Sets the {@code LateInterest} of the {@code Person} that we are building.
     */
    public PersonBuilder withLateInterest(LateInterest interest) {
        this.lateInterest = interest;
        return this;
    }

    /**
     * Sets the {@code Runner} of the {@code Person} that we are building.
     */
    public PersonBuilder withRunner(Runner runner) {
        this.runner = runner;
        return this;
    }

    /**
     * Constructs a Person
     */
    public Person build() {
        return new Person(name, phone, email, address, tags);
    }

    /**
     * Constructs a Customer
     */
    public Customer buildCustomer() {
        return new Customer(name, phone, email, address, tags, moneyBorrowed,
                oweStartDate, oweDueDate, standardInterest, lateInterest, runner);
    }

}

