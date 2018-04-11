package seedu.address.commons.events.ui;

import seedu.address.commons.events.BaseEvent;

//@@author jonleeyz

/**
 * Indicates a request to execute the home command
 */
public class HomeRequestEvent extends BaseEvent {

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
//@@author
