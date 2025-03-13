import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class SonnetHandler extends DefaultHandler {
    private String firstName = "";
    private String lastName = "";
    private String title = "";
    private StringBuilder lines = new StringBuilder();
    private boolean isLine = false;

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getTitle() {
        return title;
    }

    public String getLines() {
        return lines.toString();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        if (qName.equalsIgnoreCase("line")) {
            isLine = true;
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) {
        if (isLine) {
            lines.append(new String(ch, start, length)).append("\n");
        } else if (firstName.isEmpty()) {
            firstName = new String(ch, start, length).trim();
        } else if (lastName.isEmpty()) {
            lastName = new String(ch, start, length).trim();
        } else if (title.isEmpty()) {
            title = new String(ch, start, length).trim();
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) {
        if (qName.equalsIgnoreCase("line")) {
            isLine = false;
        }
    }
}