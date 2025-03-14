import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.parsers.*;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;
import java.io.*;

public class XMLParser {

    public static void main(String[] args) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Введите 1 для SAX, 2 для DOM или 3 для StAX: ");
            String choice = reader.readLine();

            if (choice.equals("1")) {
                parseWithSAX("src/sonnet.xml");
            } else if (choice.equals("2")) {
                parseWithDOM("src/sonnet.xml");
            } else if (choice.equals("3")) {
                parseWithStAX("src/sonnet.xml");
            } else {
                System.out.println("Неверный выбор. Введите 1, 2 или 3.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // SAX Parser
    private static void parseWithSAX(String xmlFile) {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            SonnetHandler handler = new SonnetHandler();
            saxParser.parse(new File(xmlFile), handler);

            writeToFile(handler.getFirstName(), handler.getLastName(), handler.getTitle(), handler.getLines());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // DOM Parser
    private static void parseWithDOM(String xmlFile) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new File(xmlFile));

            String firstName = document.getElementsByTagName("firstName").item(0).getTextContent();
            String lastName = document.getElementsByTagName("lastName").item(0).getTextContent();
            String title = document.getElementsByTagName("title").item(0).getTextContent();

            NodeList lines = document.getElementsByTagName("line");
            StringBuilder linesContent = new StringBuilder();
            for (int i = 0; i < lines.getLength(); i++) {
                linesContent.append(lines.item(i).getTextContent()).append("\n");
            }

            writeToFile(firstName, lastName, title, linesContent.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // StAX Parser
    private static void parseWithStAX(String xmlFile) {
        try {
            XMLInputFactory factory = XMLInputFactory.newInstance();
            XMLStreamReader reader = factory.createXMLStreamReader(new FileInputStream(xmlFile));

            String firstName = null;
            String lastName = null;
            String title = null;
            StringBuilder lines = new StringBuilder();

            while (reader.hasNext()) {
                int event = reader.next();

                switch (event) {
                    case XMLStreamConstants.START_ELEMENT:
                        String elementName = reader.getLocalName();
                        switch (elementName) {
                            case "firstName":
                                firstName = reader.getElementText();
                                break;
                            case "lastName":
                                lastName = reader.getElementText();
                                break;
                            case "title":
                                title = reader.getElementText();
                                break;
                            case "line":
                                lines.append(reader.getElementText()).append("\n");
                                break;
                        }
                        break;
                }
            }

            writeToFile(firstName, lastName, title, lines.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Write to file
    private static void writeToFile(String firstName, String lastName, String title, String lines) {
        try {
            String fileName = firstName + "_" + lastName + "_" + title + ".txt";
            FileWriter writer = new FileWriter(fileName);
            writer.write(lines);
            writer.close();
            System.out.println("Файл успешно создан: " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}