# Der-Erlkonig
###### /java/seedu/address/storage/HtmlWriterTest.java
``` java
public class HtmlWriterTest {
    private HtmlWriter htmlWriter;

    @Before
    public void setUp() throws Exception {
        htmlWriter = new HtmlWriter();
    }

    @Test
    public void checkOpeningLine() {
        String testOpeningLine = "<!DOCTYPE html><html><head>\n"
                + "<title>LoanSharkManager</title></head>\n"
                + "<body style=\"background-color:#383838;\"\n>"
                + "<font face=\"Segoe UI Semibold\" size=\"5\" color=\"white\">\n";
        assertEquals(testOpeningLine, htmlWriter.OPENING_LINE);
    }
}
```
