package seedu.address.model.person.customer;

//import static java.util.Objects.requireNonNull;
//import static seedu.address.commons.util.AppUtil.checkArgument;

//@@author melvintzw

import static seedu.address.commons.util.AppUtil.checkArgument;
import static seedu.address.model.person.Phone.MESSAGE_PHONE_CONSTRAINTS;

/**
 * Represents a customer's standard interest rate.
 * Guarantees: immutable;
 */
public class StandardInterest {


    public static final String MESSAGE_INTEREST_CONSTRAINTS =
            "Interest rates can only be given as integers or floating numbers and should not be negative";

    public final double value;

    public StandardInterest() {
        value = 0;
    }

    /**
     * Constructs a {@code Phone}.
     *
     * @param value an amount borrowed form the loanshark
     */
    public StandardInterest(double value) {
        checkArgument(isValidInterest(value), MESSAGE_INTEREST_CONSTRAINTS);
        this.value = value;
    }

    /**
     * Returns true if a given string is a valid person phone number.
     */

    public static boolean isValidInterest(double test) {
        return (!(test < 0));
    }

    @Override
    public String toString() {
        return Double.toString(value);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof StandardInterest // instanceof handles nulls
                && this.value == ((StandardInterest) other).value); // state check
    }

    @Override
    public int hashCode() {
        return new Double(value).hashCode();
    }

}
