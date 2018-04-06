# Der-Erlkonig
###### /java/seedu/address/storage/HtmlWriter.java
``` java
/**
 * Writes Person Data to a HTML file
 */
public class HtmlWriter {
    public static final String OPENING_LINE = "<!DOCTYPE html><html><head>\n"
            + "<title>LoanSharkManager</title></head>\n"
            + "<body style=\"background-color:#383838;\"\n>"
            + "<font face=\"Segoe UI Semibold\" size=\"5\" color=\"white\">\n";

    private final String name;
    private final String amountOwed;
    private final String dueDate;
    private final String runnerAssigned;

    private final List<Person> customerList;

    public HtmlWriter() {
        this.name = null;
        this.amountOwed = null;
        this.dueDate = null;
        this.runnerAssigned = null;
        this.customerList = null;
    }

    /**
     * Constructs HtmlWriter with Customer's details
     * @param customer
     */
    public HtmlWriter(Customer customer) {
        this.name = customer.getName().fullName;
        this.amountOwed = String.format("%,.2f", customer.getMoneyCurrentlyOwed());
        this.dueDate = customer.getOweDueDate().toString();
        this.runnerAssigned = customer.getRunner().getName().fullName;
        this.customerList = null;
    }

    /**
     * Constructs HtmlWriter with Runner's Details
     * @param runner
     */
    public HtmlWriter(Runner runner) {
        this.name = runner.getName().fullName;
        this.amountOwed = "test";
        this.dueDate = "test";
        this.runnerAssigned = "test";
        this.customerList = runner.getCustomers();
    }

    /**
     * Writes Customer's data to a HTML file and returns the file location
     * @return
     */
    public String writeCustomer() {
        String filepath = System.getProperty("user.dir") + File.separator + "PersonPage.html";
        String absoluteFilepath;
        File file = new File(filepath);
        try {
            PrintWriter printWriter = new PrintWriter(file);
            printWriter.print(OPENING_LINE);
            printWriter.println("<p>Name: " + name + "</p>");
            printWriter.println("<p>Amount Owed: $" + amountOwed + "</p>");
            printWriter.println("<p>Due Date: " + dueDate + "</p>");
            printWriter.println("<p>Status: VIP</p>");
            printWriter.println("<p>Runner Assigned: " + runnerAssigned + "</p>");
            printWriter.println("</body></html>");
            printWriter.close();
        } catch (FileNotFoundException e) {
            return "";
        }
        absoluteFilepath = file.getAbsolutePath();
        absoluteFilepath = absoluteFilepath.replaceAll("\"", "/");
        return absoluteFilepath;
    }

    /**
     * Writes Runner's data to HTML file and returns the file location
     * @return
     */
    public String writeRunner() {
        String filepath = System.getProperty("user.dir") + File.separator + "PersonPage.html";
        String absoluteFilepath;
        File file = new File(filepath);
        try {
            PrintWriter printWriter = new PrintWriter(file);
            printWriter.print(OPENING_LINE);
            printWriter.println("<p>Name: " + name + "</p>");
            printWriter.println("<br><hr>");
            printWriter.println("<p>Customers Assigned:</p>");
            for (Person eachCustomer: customerList) {
                printWriter.println("<p>- " + eachCustomer.getName().fullName + "</p>");
            }
            printWriter.println("</body></html>");
            printWriter.close();
        } catch (FileNotFoundException e) {
            return "";
        }
        absoluteFilepath = file.getAbsolutePath();
        absoluteFilepath = absoluteFilepath.replaceAll("\"", "/");
        return absoluteFilepath;
    }
}
```
###### /java/seedu/address/ui/BrowserPanel.java
``` java
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

```
