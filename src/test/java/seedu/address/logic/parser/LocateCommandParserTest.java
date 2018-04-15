//@@author zhangriqi
package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;

import org.junit.Test;

import seedu.address.logic.commands.LocateCommand;

public class LocateCommandParserTest {

    private LocateCommandParser parser = new LocateCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "     ", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                LocateCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidSpecifier_throwsParseException() {
        //"-e"
        assertParseFailure(parser, "-z Alice Bob", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                LocateCommand.MESSAGE_USAGE));
    }

}
