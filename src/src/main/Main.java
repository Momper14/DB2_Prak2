package main;

import contenthandler.InsertArtikelContentHandler;
import errorhandler.MyErrorHandler;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.ValidatorHandler;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

public class Main {

    public static void main(String args[]) throws IOException, FileNotFoundException, IOException, SAXException, ParserConfigurationException {

        System.setProperty("user.dir", System.getProperty("user.dir") + "/xml");

        BufferedReader read = new BufferedReader(new InputStreamReader(System.in));

        int choise;
        do {
            System.out.println("");
            System.out.println("Bitte wählen Sie einen der folgenden Menüpunkte");
            System.out.println("(4) XML Insert auf Artikel");
            System.out.println("(5) XML Update auf Kunde");
            System.out.println("(0) Beenden");
            System.out.println("");

            try {
                choise = Integer.parseInt(read.readLine());
            } catch (NumberFormatException ex) {
                choise = -1;
            }

            switch (choise) {
                case 4:
                    ValidatorHandler validatorHandler = createValidatorHandler("ARTIKEL1.xsd");
                    ContentHandler contentHandler = new InsertArtikelContentHandler(validatorHandler.getTypeInfoProvider());
                    validatorHandler.setContentHandler(contentHandler);
                    parseXML("xml/ARTIKEL1.xml", validatorHandler);
                    break;
                case 5:
                    break;
                case 0:
                    System.out.println("Auf Wiedersehen");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Ungültige eingabe");
            }
        } while (true);
    }

    private static void parseXML(String file, ValidatorHandler validatorHandler) {
        try {
            validatorHandler.setErrorHandler(new MyErrorHandler());

            SAXParserFactory factory = SAXParserFactory.newInstance();
            factory.setValidating(true);

            XMLReader xmlReader = XMLReaderFactory.createXMLReader();
            xmlReader.setContentHandler(validatorHandler);
            xmlReader.parse(new InputSource(new FileReader(file)));
        } catch (FileNotFoundException ex) {
            System.err.println(ex.getMessage());
        } catch (SAXException | IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static ValidatorHandler createValidatorHandler(String schema) throws SAXException, IOException {
        SchemaFactory schemaFactory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
        return schemaFactory.newSchema(new File(schema)).newValidatorHandler();
    }
}
