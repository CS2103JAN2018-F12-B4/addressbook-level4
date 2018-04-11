package seedu.address.ui;

import java.net.URL;
import java.util.logging.Logger;

import com.google.common.eventbus.Subscribe;

import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.layout.Region;
import javafx.scene.web.WebView;
import seedu.address.MainApp;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.events.ui.HomeRequestEvent;
import seedu.address.commons.events.ui.PersonPanelSelectionChangedEvent;
import seedu.address.model.person.Person;
import seedu.address.model.person.customer.Customer;
import seedu.address.model.person.runner.Runner;
import seedu.address.storage.HtmlWriter;

/**
 * The Browser Panel of the App.
 */
public class BrowserPanel extends UiPart<Region> {

    public static final String DEFAULT_PAGE = "default.html";
    public static final String SEARCH_PAGE_URL =
            "https://se-edu.github.io/addressbook-level4/DummySearchPage.html?name=";
    public static final String PERSON_PAGE = "PersonPage.html";

    private static final String FXML = "BrowserPanel.fxml";

    private static HtmlWriter htmlWriter;

    private final Logger logger = LogsCenter.getLogger(this.getClass());

    @FXML
    private WebView browser;

    public BrowserPanel() {
        super(FXML);

        // To prevent triggering events for typing inside the loaded Web page.
        // getRoot().setOnKeyPressed(Event::consume);

        loadDefaultPage();
        registerAsAnEventHandler(this);
    }

    //@@author Der-Erlkonig
    /**
     * Loads a HTML file with person details
     * @param person
     */
    private void loadPersonPage(Person person) {
        String personfilepath;
        if (person instanceof Customer) {
            htmlWriter = new HtmlWriter((Customer) person);
            personfilepath = htmlWriter.writeCustomer();
        } else if (person instanceof Runner) {
            htmlWriter = new HtmlWriter((Runner) person);
            personfilepath = htmlWriter.writeRunner();
        } else {
            personfilepath = "";
        }
        loadPage("file:///" + personfilepath);
    }

    public void loadPage(String url) {
        Platform.runLater(() -> browser.getEngine().load(url));
    }

    //@@author
    /**
     * Loads a default HTML file with a background that matches the general theme.
     */
    private void loadDefaultPage() {
        URL defaultPage = MainApp.class.getResource(FXML_FILE_FOLDER + DEFAULT_PAGE);
        loadPage(defaultPage.toExternalForm());
    }

    /**
     * Frees resources allocated to the browser.
     */
    public void freeResources() {
        browser = null;
    }

    @Subscribe
    private void handlePersonPanelSelectionChangedEvent(PersonPanelSelectionChangedEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        loadPersonPage(event.getNewSelection().person);
    }

    /**
     * Handles the event where the Esc key is pressed or "home" is input to the CommandBox.
     * {@code HomeRequestEvent}.
     */
    @Subscribe
    private void handleHomeRequestEvent(HomeRequestEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        loadDefaultPage();
    }

}
