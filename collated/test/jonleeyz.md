# jonleeyz
###### /java/guitests/guihandles/CommandBoxHandle.java
``` java
    /**
     * Clears all text in the Command Box.
     * @return true if the command succeeded, false otherwise.
     */
    public boolean clear() {
        click();
        guiRobot.interact(() -> getRootNode().clear());
        return getRootNode().getText().equals("");
    }
```
###### /java/guitests/guihandles/MainMenuHandle.java
``` java
    /**
     * Simulates press of given keyboard shortcut
     */
    public void useAccelerator(KeyCode... combination) {
        guiRobot.push(combination);
    }
```
###### /java/systemtests/AddCommandSystemTest.java
``` java
    @Test
    public void populateAddCommandTemplate() {
        //use accelerator
        getCommandBox().click();
        populateAddCommandUsingAccelerator();
        assertPopulationSuccess();

        getResultDisplay().click();
        populateAddCommandUsingAccelerator();
        assertPopulationSuccess();

        /**Unusual: Ctrl + Space does not work when focus is on PersonListPanel.
         * Although most accelerators work fine when focus is on PersonListPanel,
         * the Space key does not play nice with the PersonListPanel.
         */
        getPersonListPanel().click();
        populateAddCommandUsingAccelerator();
        assertPopulationFailure();

        getBrowserPanel().click();
        populateAddCommandUsingAccelerator();
        assertPopulationFailure();

        //use menu button
        populateAddCommandUsingMenu();
        assertPopulationSuccess();
    }
```
###### /java/systemtests/AddCommandSystemTest.java
``` java
    /**
     * Asserts that population of the {@code CommandBox} with the AddCommand
     * template was successful.
     */
    private void assertPopulationSuccess() {
        AddCommand addCommand = new AddCommand();
        assertEquals(addCommand.getTemplate(), getCommandBox().getInput());
        assertEquals(addCommand.getUsageMessage(), getResultDisplay().getText());
        guiRobot.pauseForHuman();

        executeCommand("invalid command");
        assertTrue(getCommandBox().clear());
        assertEquals(MESSAGE_UNKNOWN_COMMAND, getResultDisplay().getText());
        guiRobot.pauseForHuman();
    }

    /**
     * Asserts that population of the {@code CommandBox} with the AddCommand
     * template was unsuccessful.
     */
    private void assertPopulationFailure() {
        AddCommand addCommand = new AddCommand();
        assertNotEquals(addCommand.getTemplate(), getCommandBox().getInput());
        assertNotEquals(addCommand.getUsageMessage(), getResultDisplay().getText());
        guiRobot.pauseForHuman();

        executeCommand("invalid command");
        assertTrue(getCommandBox().clear());
        assertEquals(MESSAGE_UNKNOWN_COMMAND, getResultDisplay().getText());
        guiRobot.pauseForHuman();
    }

    /**
     * Populates the {@code CommandBox} with the AddCommand template
     * using the associated accelerator in {@code MainWindow}.
     */
    private void populateAddCommandUsingAccelerator() {
        populateUsingAccelerator(KeyCode.CONTROL, KeyCode.SPACE);
    }

    /**
     * Populates the {@code CommandBox} with the AddCommand template
     * using the menu bar in {@code MainWindow}.
     */
    private void populateAddCommandUsingMenu() {
        populateUsingMenu("Actions", "Add a Person...");
    }
```
###### /java/systemtests/AddressBookSystemTest.java
``` java
    /**
     * Executes {@code command} associated with the given keyboard shortcut.
     * Method returns after UI components have been updated.
     */
    protected void executeUsingAccelerator(KeyCode... combination) {
        rememberStates();
        // Injects a fixed clock before executing a command so that the time stamp shown in the status bar
        // after each command is predictable and also different from the previous command.
        clockRule.setInjectedClockToCurrentTime();

        mainWindowHandle.getMainMenu().useAccelerator(combination);

        waitUntilBrowserLoaded(getBrowserPanel());
    }

    /**
     * Populates the appropriate {@code command} template in the application's
     * {@code CommandBox} given a keyboard shortcut.
     */
    protected void populateUsingAccelerator(KeyCode... combination) {
        mainWindowHandle.getMainMenu().useAccelerator(combination);
    }

    /**
     * Executes {@code command} associated with the given menu item.
     * Method returns after UI components have been updated.
     */
    protected void executeUsingMenuItem(String... menuItems) {
        rememberStates();
        // Injects a fixed clock before executing a command so that the time stamp shown in the status bar
        // after each command is predictable and also different from the previous command.
        clockRule.setInjectedClockToCurrentTime();

        mainWindowHandle.getMainMenu().clickOnMenuItemsSequentially(menuItems);

        waitUntilBrowserLoaded(getBrowserPanel());
    }

    /**
     * Populates the appropriate {@code command} template in the application's
     * {@code CommandBox} given the appropriate menu item.
     */
    protected void populateUsingMenu(String... menuItems) {
        mainWindowHandle.getMainMenu().clickOnMenuItemsSequentially(menuItems);
    }
```
###### /java/systemtests/ClearCommandSystemTest.java
``` java
        /* Case: simulate press of Ctrl + Shift + C -> cleared */
        executeCommand(UndoCommand.COMMAND_WORD); // undoes last clear command: address book still will be empty
        executeCommand(UndoCommand.COMMAND_WORD); // restores the original address book
        assertKeyboardShortcutSuccess(ClearCommand.MESSAGE_SUCCESS,
                new ModelManager(),
                KeyCode.CONTROL,
                KeyCode.SHIFT,
                KeyCode.C);
        assertSelectedCardUnchanged();

        /* Case: simulate click of "Clear the Database" menu item -> cleared */
        executeCommand(UndoCommand.COMMAND_WORD); // restores the original address book
        assertMenuItemSuccess(ClearCommand.MESSAGE_SUCCESS,
                new ModelManager(),
                "Edit",
                "Clear the Database");
        assertSelectedCardUnchanged();
```
###### /java/systemtests/ClearCommandSystemTest.java
``` java
    /**
     * Performs the same verification as {@code assertCommandSuccess(String, String, Model)} except that the command
     * is executed using its keyboard shortcut.
     * @see ClearCommandSystemTest#assertCommandSuccess(String, String, Model)
     */
    private void assertKeyboardShortcutSuccess(String expectedResultMessage,
                                               Model expectedModel,
                                               KeyCode... combination) {
        executeUsingAccelerator(combination);
        assertApplicationDisplaysExpected("", expectedResultMessage, expectedModel);
        assertCommandBoxAndResultDisplayShowsDefaultStyle();
        assertStatusBarUnchangedExceptSyncStatus();
    }

    /**
     * Performs the same verification as {@code assertCommandSuccess(String, String, Model)} except that the command
     * is executed using its menu item.
     * @see ClearCommandSystemTest#assertCommandSuccess(String, String, Model)
     */
    private void assertMenuItemSuccess(String expectedResultMessage,
                                               Model expectedModel,
                                               String... menuItems) {
        executeUsingMenuItem(menuItems);
        assertApplicationDisplaysExpected("", expectedResultMessage, expectedModel);
        assertCommandBoxAndResultDisplayShowsDefaultStyle();
        assertStatusBarUnchangedExceptSyncStatus();
    }
```
###### /java/systemtests/DeleteCommandSystemTest.java
``` java
    @Test
    public void populateDeleteCommandTemplate() {
        //use accelerator
        getCommandBox().click();
        populateDeleteCommandUsingAccelerator();
        assertPopulationSuccess();

        getResultDisplay().click();
        populateDeleteCommandUsingAccelerator();
        assertPopulationSuccess();

        getPersonListPanel().click();
        populateDeleteCommandUsingAccelerator();
        assertPopulationSuccess();

        getBrowserPanel().click();
        populateDeleteCommandUsingAccelerator();
        assertPopulationFailure();

        //use menu button
        populateDeleteCommandUsingMenu();
        assertPopulationSuccess();
    }
```
###### /java/systemtests/DeleteCommandSystemTest.java
``` java
    /**
     * Asserts that population of the {@code CommandBox} with the AddCommand
     * template was successful.
     */
    private void assertPopulationSuccess() {
        DeleteCommand deleteCommand = new DeleteCommand();
        assertEquals(deleteCommand.getTemplate(), getCommandBox().getInput());
        assertEquals(deleteCommand.getUsageMessage(), getResultDisplay().getText());
        guiRobot.pauseForHuman();

        executeCommand("invalid command");
        assertTrue(getCommandBox().clear());
        assertEquals(MESSAGE_UNKNOWN_COMMAND, getResultDisplay().getText());
        guiRobot.pauseForHuman();
    }

    /**
     * Asserts that population of the {@code CommandBox} with the AddCommand
     * template was unsuccessful.
     */
    private void assertPopulationFailure() {
        DeleteCommand deleteCommand = new DeleteCommand();
        assertNotEquals(deleteCommand.getTemplate(), getCommandBox().getInput());
        assertNotEquals(deleteCommand.getUsageMessage(), getResultDisplay().getText());
        guiRobot.pauseForHuman();

        executeCommand("invalid command");
        assertTrue(getCommandBox().clear());
        assertEquals(MESSAGE_UNKNOWN_COMMAND, getResultDisplay().getText());
        guiRobot.pauseForHuman();
    }

    /**
     * Populates the {@code CommandBox} with the DeleteCommand template
     * using the associated accelerator in {@code MainWindow}.
     */
    private void populateDeleteCommandUsingAccelerator() {
        populateUsingAccelerator(KeyCode.CONTROL, KeyCode.D);
    }

    /**
     * Populates the {@code CommandBox} with the DeleteCommand template
     * using the menu bar in {@code MainWindow}.
     */
    private void populateDeleteCommandUsingMenu() {
        populateUsingMenu("Actions", "Delete a Person...");
    }
```
###### /java/systemtests/EditCommandSystemTest.java
``` java
    @Test
    public void populateEditCommandTemplate() {
        //use accelerator
        getCommandBox().click();
        populateEditCommandUsingAccelerator();
        assertPopulationSuccess();

        getResultDisplay().click();
        populateEditCommandUsingAccelerator();
        assertPopulationSuccess();

        getPersonListPanel().click();
        populateEditCommandUsingAccelerator();
        assertPopulationSuccess();

        getBrowserPanel().click();
        populateEditCommandUsingAccelerator();
        assertPopulationFailure();

        //use menu button
        populateEditCommandUsingMenu();
        assertPopulationSuccess();
    }
```
###### /java/systemtests/EditCommandSystemTest.java
``` java
    /**
     * Asserts that population of the {@code CommandBox} with the AddCommand
     * template was successful.
     */
    private void assertPopulationSuccess() {
        EditCommand editCommand = new EditCommand();
        assertEquals(editCommand.getTemplate(), getCommandBox().getInput());
        assertEquals(editCommand.getUsageMessage(), getResultDisplay().getText());
        guiRobot.pauseForHuman();

        executeCommand("invalid command");
        assertTrue(getCommandBox().clear());
        assertEquals(MESSAGE_UNKNOWN_COMMAND, getResultDisplay().getText());
        guiRobot.pauseForHuman();
    }

    /**
     * Asserts that population of the {@code CommandBox} with the AddCommand
     * template was unsuccessful.
     */
    private void assertPopulationFailure() {
        EditCommand editCommand = new EditCommand();
        assertNotEquals(editCommand.getTemplate(), getCommandBox().getInput());
        assertNotEquals(editCommand.getUsageMessage(), getResultDisplay().getText());
        guiRobot.pauseForHuman();

        executeCommand("invalid command");
        assertTrue(getCommandBox().clear());
        assertEquals(MESSAGE_UNKNOWN_COMMAND, getResultDisplay().getText());
        guiRobot.pauseForHuman();
    }

    /**
     * Populates the {@code CommandBox} with the EditCommand template
     * using the associated accelerator in {@code MainWindow}.
     */
    private void populateEditCommandUsingAccelerator() {
        populateUsingAccelerator(KeyCode.CONTROL, KeyCode.E);
    }

    /**
     * Populates the {@code CommandBox} with the EditCommand template
     * using the menu bar in {@code MainWindow}.
     */
    private void populateEditCommandUsingMenu() {
        populateUsingMenu("Actions", "Edit a Person...");
    }
```
###### /java/systemtests/FindCommandSystemTest.java
``` java
    @Test
    public void populateFindCommandTemplate() {
        //use accelerator
        getCommandBox().click();
        populateFindCommandUsingAccelerator();
        assertPopulationSuccess();

        getResultDisplay().click();
        populateFindCommandUsingAccelerator();
        assertPopulationSuccess();

        getPersonListPanel().click();
        populateFindCommandUsingAccelerator();
        assertPopulationSuccess();

        getBrowserPanel().click();
        populateFindCommandUsingAccelerator();
        assertPopulationFailure();

        //use menu button
        populateFindCommandUsingMenu();
        assertPopulationSuccess();
    }
```
###### /java/systemtests/FindCommandSystemTest.java
``` java
    /**
     * Asserts that population of the {@code CommandBox} with the AddCommand
     * template was successful.
     */
    private void assertPopulationSuccess() {
        FindCommand findCommand = new FindCommand();
        assertEquals(findCommand.getTemplate(), getCommandBox().getInput());
        assertEquals(findCommand.getUsageMessage(), getResultDisplay().getText());
        guiRobot.pauseForHuman();

        executeCommand("invalid command");
        assertTrue(getCommandBox().clear());
        assertEquals(MESSAGE_UNKNOWN_COMMAND, getResultDisplay().getText());
        guiRobot.pauseForHuman();
    }

    /**
     * Asserts that population of the {@code CommandBox} with the AddCommand
     * template was unsuccessful.
     */
    private void assertPopulationFailure() {
        FindCommand findCommand = new FindCommand();
        assertNotEquals(findCommand.getTemplate(), getCommandBox().getInput());
        assertNotEquals(findCommand.getUsageMessage(), getResultDisplay().getText());
        guiRobot.pauseForHuman();

        executeCommand("invalid command");
        assertTrue(getCommandBox().clear());
        assertEquals(MESSAGE_UNKNOWN_COMMAND, getResultDisplay().getText());
        guiRobot.pauseForHuman();
    }

    /**
     * Populates the {@code CommandBox} with the FindCommand template
     * using the associated accelerator in {@code MainWindow}.
     */
    private void populateFindCommandUsingAccelerator() {
        populateUsingAccelerator(KeyCode.CONTROL, KeyCode.F);
    }

    /**
     * Populates the {@code CommandBox} with the FindCommand template
     * using the menu bar in {@code MainWindow}.
     */
    private void populateFindCommandUsingMenu() {
        populateUsingMenu("View", "Find...");
    }
```
###### /java/systemtests/SelectCommandSystemTest.java
``` java
    @Test
    public void populateSelectCommandTemplate() {
        //use accelerator
        getCommandBox().click();
        populateSelectCommandUsingAccelerator();
        assertPopulationSuccess();

        getResultDisplay().click();
        populateSelectCommandUsingAccelerator();
        assertPopulationSuccess();

        getPersonListPanel().click();
        populateSelectCommandUsingAccelerator();
        assertPopulationSuccess();

        getBrowserPanel().click();
        populateSelectCommandUsingAccelerator();
        assertPopulationFailure();

        //use menu button
        populateSelectCommandUsingMenu();
        assertPopulationSuccess();
    }
```
###### /java/systemtests/SelectCommandSystemTest.java
``` java
    /**
     * Asserts that population of the {@code CommandBox} with the AddCommand
     * template was successful.
     */
    private void assertPopulationSuccess() {
        SelectCommand selectCommand = new SelectCommand();
        assertEquals(selectCommand.getTemplate(), getCommandBox().getInput());
        assertEquals(selectCommand.getUsageMessage(), getResultDisplay().getText());
        guiRobot.pauseForHuman();

        executeCommand("invalid command");
        assertTrue(getCommandBox().clear());
        assertEquals(MESSAGE_UNKNOWN_COMMAND, getResultDisplay().getText());
        guiRobot.pauseForHuman();
    }

    /**
     * Asserts that population of the {@code CommandBox} with the AddCommand
     * template was unsuccessful.
     */
    private void assertPopulationFailure() {
        SelectCommand selectCommand = new SelectCommand();
        assertNotEquals(selectCommand.getTemplate(), getCommandBox().getInput());
        assertNotEquals(selectCommand.getUsageMessage(), getResultDisplay().getText());
        guiRobot.pauseForHuman();

        executeCommand("invalid command");
        assertTrue(getCommandBox().clear());
        assertEquals(MESSAGE_UNKNOWN_COMMAND, getResultDisplay().getText());
        guiRobot.pauseForHuman();
    }

    /**
     * Populates the {@code CommandBox} with the SelectCommand template
     * using the associated accelerator in {@code MainWindow}.
     */
    private void populateSelectCommandUsingAccelerator() {
        populateUsingAccelerator(KeyCode.CONTROL, KeyCode.S);
    }

    /**
     * Populates the {@code CommandBox} with the SelectCommand template
     * using the menu bar in {@code MainWindow}.
     */
    private void populateSelectCommandUsingMenu() {
        populateUsingMenu("Actions", "Select a Person...");
    }
```
