//@@author zhangriqi
package seedu.address.commons.events.ui;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.events.BaseEvent;

/**
 * Indicates a request to locate the list of persons
 */
public class LocateRequestEvent extends BaseEvent {

    public final int targetInex;

    public LocateRequestEvent(Index targetIndex) {
        this.targetInex = targetIndex.getZeroBased();
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
