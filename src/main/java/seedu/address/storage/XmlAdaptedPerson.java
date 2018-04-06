package seedu.address.storage;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.xml.bind.annotation.XmlElement;

import seedu.address.commons.exceptions.IllegalValueException;
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

//@@author melvintzw
/**
 * JAXB-friendly version of the Person.
 */
public class XmlAdaptedPerson {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Person's %s field is missing!";

    @XmlElement(required = true)
    private Person.PersonType personType;

    @XmlElement(required = true)
    private String name;
    @XmlElement(required = true)
    private String phone;
    @XmlElement(required = true)
    private String email;
    @XmlElement(required = true)
    private String address;

    @XmlElement
    private List<XmlAdaptedTag> tagged = new ArrayList<>();

    //Customer fields
    @XmlElement(required = true)
    private MoneyBorrowed moneyBorrowed;
    @XmlElement(required = true)
    private StandardInterest standardInterest;
    @XmlElement(required = true)
    private LateInterest lateInterest;
    @XmlElement(required = true)
    private Date oweStartDate;
    @XmlElement(required = true)
    private Date oweDueDate;
    @XmlElement(required = true)
    private Runner runner;

    //Runner fields
    @XmlElement(required = true)
    private List<XmlAdaptedPerson> customers = new ArrayList<>();

    /**
     * Constructs an XmlAdaptedPerson.
     * This is the no-arg constructor that is required by JAXB.
     */
    public XmlAdaptedPerson() {}

    /**
     * Constructs an {@code XmlAdaptedPerson} with the given person details.
     */
    public XmlAdaptedPerson(String name, String phone, String email, String address, List<XmlAdaptedTag> tagged) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        if (tagged != null) {
            this.tagged = new ArrayList<>(tagged);
        }
        this.personType = Person.PersonType.PERSON;
    }

    /**
     * Converts a given Person into this class for JAXB use.
     *
     * @param source future changes to this will not affect the created XmlAdaptedPerson
     */
    public XmlAdaptedPerson(Person source) {
        name = source.getName().fullName;
        phone = source.getPhone().value;
        email = source.getEmail().value;
        address = source.getAddress().value;
        tagged = new ArrayList<>();
        for (Tag tag : source.getTags()) {
            tagged.add(new XmlAdaptedTag(tag));
        }
        personType = source.getType();

        if (source instanceof Customer) {
            moneyBorrowed = ((Customer) source).getMoneyBorrowed();
            standardInterest = ((Customer) source).getStandardInterest();
            lateInterest = ((Customer) source).getLateInterest();
            oweStartDate = ((Customer) source).getOweStartDate();
            oweDueDate = ((Customer) source).getOweDueDate();
            runner = ((Customer) source).getRunner();
        }

        if (source instanceof Runner) {
            customers = new ArrayList<>();
            for (Person person : ((Runner) source).getCustomers()) {
                customers.add(new XmlAdaptedPerson(person));
            }
        }

    }

    /**
     * Converts this jaxb-friendly adapted person object into the model's Person object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted person
     */
    public Person toModelType() throws IllegalValueException {
        final List<Tag> personTags = new ArrayList<>();
        for (XmlAdaptedTag tag : tagged) {
            personTags.add(tag.toModelType());
        }

        if (this.name == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName()));
        }
        if (!Name.isValidName(this.name)) {
            throw new IllegalValueException(Name.MESSAGE_NAME_CONSTRAINTS);
        }
        final Name name = new Name(this.name);

        if (this.phone == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Phone.class.getSimpleName()));
        }
        if (!Phone.isValidPhone(this.phone)) {
            throw new IllegalValueException(Phone.MESSAGE_PHONE_CONSTRAINTS);
        }
        final Phone phone = new Phone(this.phone);

        if (this.email == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Email.class.getSimpleName()));
        }
        if (!Email.isValidEmail(this.email)) {
            throw new IllegalValueException(Email.MESSAGE_EMAIL_CONSTRAINTS);
        }
        final Email email = new Email(this.email);

        if (this.address == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Address.class.getSimpleName()));
        }
        if (!Address.isValidAddress(this.address)) {
            throw new IllegalValueException(Address.MESSAGE_ADDRESS_CONSTRAINTS);
        }
        final Address address = new Address(this.address);

        final Set<Tag> tags = new HashSet<>(personTags);

        //TODO: implement runner and customers field
        if (this.personType == Person.PersonType.CUSTOMER) {
            //moneyBorrowed
            if (this.moneyBorrowed == null) {
                throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, MoneyBorrowed.class
                        .getSimpleName()));
            }
            //TODO: write valid regex check
            final MoneyBorrowed moneyBorrowed = new MoneyBorrowed(this.moneyBorrowed.value);

            //oweStartDate
            if (this.oweStartDate == null) {
                throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Date.class.getSimpleName
                        ()));
            }
            //TODO: write valid regex check
            final Date oweStartDate = this.oweStartDate;

            //oweDueDate
            if (this.oweDueDate == null) {
                throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Date.class.getSimpleName
                        ()));
            }
            //TODO: write valid regex check
            final Date oweDueDate = this.oweDueDate;

            //standardInterest
            if (this.standardInterest == null) {
                throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, StandardInterest.class
                        .getSimpleName()));
            }
            //TODO: write valid regex check
            final StandardInterest standardInterest = this.standardInterest;

            //lateInterest
            if (this.lateInterest == null) {
                throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, LateInterest.class
                        .getSimpleName()));
            }
            //TODO: write valid regex check
            final LateInterest lateInterest = this.lateInterest;

            return new Customer(name, phone, email, address, tags, moneyBorrowed, oweStartDate, oweDueDate,
                    standardInterest, lateInterest, new Runner());

        } else if (this.personType == Person.PersonType.RUNNER) {
            if (this.customers == null) {
                throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, StandardInterest.class
                        .getSimpleName()));
            }
            //TODO: write valid regex check

            final List<Person> customerList = new ArrayList<>();
            for (XmlAdaptedPerson person : customers) {
                customerList.add(person.toModelType());
            }

            return new Runner(name, phone, email, address, tags, customerList);

        } else {
            return new Person(name, phone, email, address, tags);

        }

    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof XmlAdaptedPerson)) {
            return false;
        }

        XmlAdaptedPerson otherPerson = (XmlAdaptedPerson) other;
        return Objects.equals(name, otherPerson.name)
                && Objects.equals(phone, otherPerson.phone)
                && Objects.equals(email, otherPerson.email)
                && Objects.equals(address, otherPerson.address)
                && tagged.equals(otherPerson.tagged);
    }
}
