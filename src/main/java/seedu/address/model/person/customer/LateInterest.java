package seedu.address.model.person.customer;

//import static java.util.Objects.requireNonNull;
//import static seedu.address.commons.util.AppUtil.checkArgument;

//@@author melvintzw

/**
 * Represents a customer's late interest rate.
 * Guarantees: immutable;
 */
public class LateInterest {

    /*
    public static final String MESSAGE_PHONE_CONSTRAINTS =
            "Phone numbers can only contain numbers, and should be at least 3 digits long";
    public static final String PHONE_VALIDATION_REGEX = "\\d{3,}";
    */

    public final double value;

    public LateInterest() {
        value = 0;
    }

    /**
     * Constructs a {@code Phone}.
     *
     * @param value an amount borrowed form the loanshark
     */
    public LateInterest(double value) {
        //checkArgument(isValidPhone(phone), MESSAGE_PHONE_CONSTRAINTS);
        this.value = value;
    }

    /**
     * Returns true if a given string is a valid person phone number.
     */
    /*
    public static boolean isValidPhone(String test) {
        return test.matches(PHONE_VALIDATION_REGEX);
    }
    */


    @Override
    public String toString() {
        return Double.toString(value);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof LateInterest // instanceof handles nulls
                && this.value == ((LateInterest) other).value); // state check
    }

    @Override
    public int hashCode() {
        return new Double(value).hashCode();
    }

}
