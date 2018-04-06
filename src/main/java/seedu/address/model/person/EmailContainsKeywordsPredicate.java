package seedu.address.model.person;

import java.util.List;
import java.util.function.Predicate;

import seedu.address.commons.util.StringUtil;

//@@author melvintzw
/**
 * Tests that a {@code Person}'s {@code Name} matches any of the keywords given.
 */
public class EmailContainsKeywordsPredicate implements Predicate<Person> {
    private final List<String> keywords;

    public EmailContainsKeywordsPredicate(List<String> keywords) {
        this.keywords = keywords;
    }

    @Override
    //test existence of keywords in person's full name and tags.
    public boolean test(Person person) {
        //TODO: write helper method here or in Email class to extract values before '@' symbol in email address.
        return keywords.stream()
                .anyMatch(keyword -> StringUtil.containsWordIgnoreCase(person.getEmail().value, keyword));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof EmailContainsKeywordsPredicate // instanceof handles nulls
                && this.keywords.equals(((EmailContainsKeywordsPredicate) other).keywords)); // state check
    }

}
