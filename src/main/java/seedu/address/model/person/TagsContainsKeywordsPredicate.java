package seedu.address.model.person;

import java.util.List;
import java.util.function.Predicate;

import seedu.address.commons.util.StringUtil;
import seedu.address.model.tag.Tag;

//@@author melvintzw
/**
 * Tests that a {@code Person}'s {@code Name} matches any of the keywords given.
 */
public class TagsContainsKeywordsPredicate implements Predicate<Person> {
    private final List<String> keywords;

    public TagsContainsKeywordsPredicate(List<String> keywords) {
        this.keywords = keywords;
    }

    @Override
    //test existence of keywords in person's full name and tags.
    public boolean test(Person person) {

        String stringOfTags = getStringOfTags(person);

        return keywords.stream()
                    .anyMatch(keyword -> StringUtil.containsWordIgnoreCase(stringOfTags, keyword));
    }

    private String getStringOfTags(Person person) {
        String stringOfTags = "";

        for (Tag x : person.getTags()) {
            stringOfTags = stringOfTags + " " + x.tagName;
        }
        return stringOfTags.trim();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof TagsContainsKeywordsPredicate // instanceof handles nulls
                && this.keywords.equals(((TagsContainsKeywordsPredicate) other).keywords)); // state check
    }

}
