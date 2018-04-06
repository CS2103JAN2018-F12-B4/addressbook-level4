//@@author zhangriqi
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
