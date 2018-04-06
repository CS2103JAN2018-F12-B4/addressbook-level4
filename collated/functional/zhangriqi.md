# zhangriqi
###### /java/seedu/address/commons/events/ui/LocateRequestEvent.java
``` java
package seedu.address.commons.events.ui;

import seedu.address.commons.events.BaseEvent;

/**
 * Indicates a request to locate the list of persons
 */
public class LocateRequestEvent extends BaseEvent {

    public final int target;

    public LocateRequestEvent(int target) {
        this.target = target;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
```
###### /java/seedu/address/logic/commands/LocateCommand.java
``` java
package seedu.address.logic.commands;

import java.util.List;
import java.util.function.Predicate;

import seedu.address.commons.core.EventsCenter;
import seedu.address.commons.events.ui.LocateRequestEvent;
import seedu.address.model.person.Person;
import seedu.address.ui.MainWindow;

/**
 * Locate the address of a person by keywords on Google Map.
 * Keyword matching is case sensitive.
 */
public class LocateCommand extends Command implements PopulatableCommand {
    public static final String COMMAND_WORD = "locate";
    public static final String COMMAND_ALIAS = "lo";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Locate person whose fields contain any of "
            + "the specified keywords (case-insensitive) and displays them as a list with index numbers.\n"
            + "Valid specifiers are -n, -p, -e, -a, -t, for NAME, PHONE, EMAIL, ADDRESS and TAGS"
            + " respectively \n"
            + "Parameters: [SPECIFIER] KEYWORD [MORE_KEYWORDS]...\n"
            + "Example: " + COMMAND_WORD + " -n alice bob charlie";

    public static final String MESSAGE_LOCATE_SUCCESS = "Locate successful";
    public static final String MESSAGE_LOCATE_SELECT = "More than one person found! "
            + "Locate the person on top of the list by default.";

    private final int target = 0;
    private final int targetOne = 1;
    private final Predicate<Person> predicate;

    public LocateCommand(Predicate<Person> predicate) {
        this.predicate = predicate;
    }

    /**
     * For call in PopulatePrefixRequestEvent class, to assign string values.
     */
    public LocateCommand() {
        predicate = null;
    }

    @Override
    public CommandResult execute() {
        List<Person> lastShownList = model.getFilteredPersonList(predicate);

        Person location = lastShownList.get(target);

        // Open Google Map on BrowserPanel
        MainWindow.loadUrl("https://www.google.com.sg/maps/place/"
                + location.getAddress().toString());

        EventsCenter.getInstance().post(new LocateRequestEvent(target));

        if (model.getFilteredPersonList().size() > 1) {
            return new CommandResult(String.format(MESSAGE_LOCATE_SELECT, targetOne));
        }
        return new CommandResult(String.format(MESSAGE_LOCATE_SUCCESS, targetOne));

    }
    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof FindCommand // instanceof handles nulls
                && this.predicate.equals(((LocateCommand) other).predicate));
        // state check
    }

    @Override
    public String getCommandWord() {
        return COMMAND_WORD;
    }

    @Override
    public String getTemplate() {
        return COMMAND_WORD + "-";
    }

    @Override
    public int getCaretIndex() {
        return getTemplate().length();
    }

    @Override
    public String getUsageMessage() {
        return MESSAGE_USAGE;
    }
}
```
###### /java/seedu/address/logic/parser/AddressBookParser.java
``` java
        case AssignCommand.COMMAND_WORD:
        case AssignCommand.COMMAND_ALIAS:
            return new AssignCommandParser().parse(arguments);
```
###### /java/seedu/address/logic/parser/LocateCommandParser.java
``` java
package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.Arrays;

import seedu.address.logic.commands.LocateCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.AddressContainsKeywordsPredicate;
import seedu.address.model.person.EmailContainsKeywordsPredicate;
import seedu.address.model.person.NameContainsKeywordsPredicate;
import seedu.address.model.person.PersonContainsKeywordsPredicate;
import seedu.address.model.person.PhoneContainsKeywordsPredicate;
import seedu.address.model.person.TagsContainsKeywordsPredicate;

/**
 * Parse input arguments and create a new LocateCommand object
 */
public class LocateCommandParser implements Parser<LocateCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the DeleteCommand
     * and returns an DeleteCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    @Override
    public LocateCommand parse(String args) throws ParseException {

        String trimmedArgs = args.trim();
        if (trimmedArgs.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, LocateCommand.MESSAGE_USAGE));
        }

        String[] arguments = trimmedArgs.split("\\s+");
        String[] keywords;
        //check arguments[0] for specifier

        if (arguments[0].matches("\\p{Alnum}+.++")) {
            return new LocateCommand(new PersonContainsKeywordsPredicate(Arrays.asList(arguments)));
        }

        switch (arguments[0]) {
        case "-n":
            keywords = Arrays.copyOfRange(arguments, 1, arguments.length);
            return new LocateCommand(new NameContainsKeywordsPredicate(Arrays.asList(keywords)));
        case "-p":
            keywords = Arrays.copyOfRange(arguments, 1, arguments.length);
            return new LocateCommand(new PhoneContainsKeywordsPredicate(Arrays.asList(keywords)));
        case "-e":
            keywords = Arrays.copyOfRange(arguments, 1, arguments.length);
            return new LocateCommand(new EmailContainsKeywordsPredicate(Arrays.asList(keywords)));
        case "-a":
            keywords = Arrays.copyOfRange(arguments, 1, arguments.length);
            return new LocateCommand(new AddressContainsKeywordsPredicate(Arrays.asList(keywords)));
        case "-t":
            keywords = Arrays.copyOfRange(arguments, 1, arguments.length);
            return new LocateCommand(new TagsContainsKeywordsPredicate(Arrays.asList(keywords)));
        default:
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, LocateCommand.MESSAGE_USAGE));
        }
    }


}

```
###### /java/seedu/address/model/ModelManager.java
``` java
    @Override
    public void updateFilteredPersonList(Predicate<Person> predicate) {
        requireNonNull(predicate);
        filteredPersons.setPredicate(predicate);
    }
```
###### /java/seedu/address/ui/MainWindow.java
``` java
    public static void loadUrl(String url) {
        browserPanel.loadPage(url);
    }
}
```
###### /java/seedu/address/ui/PersonListPanel.java
``` java
    private void locate(int index) {
        Platform.runLater(()-> {
            personListView.scrollTo(index);
        });
    }
```
###### /java/seedu/address/ui/PersonListPanel.java
``` java
    @Subscribe
    private void handleLocateRequestEvent(LocateRequestEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        locate(event.target);
    }
```
###### /resources/view/MainWindow.fxml
``` fxml
    <Image url="@/images/loanshark_logo.png" />
```
###### /resources/view/StatusBarFooter.fxml
``` fxml
<GridPane styleClass="grid-pane" minHeight="10" maxHeight="10" xmlns="http://javafx.com/javafx/8" xmlns:fx="http://javafx.com/fxml/1">
```
