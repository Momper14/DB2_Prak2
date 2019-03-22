package main;

import connection.ConnectionCreator;
import contenthandler.InsertArtikelContentHandler;
import contenthandler.UpdateKundeContentHandler;
import errorhandler.MyErrorHandler;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

public class Main {

    public static void main(String args[]) throws IOException, FileNotFoundException, IOException, SAXException, ParserConfigurationException {

        System.setProperty("user.dir", System.getProperty("user.dir") + "/xml");

        BufferedReader read = new BufferedReader(new InputStreamReader(System.in));

        int choise;
        do {
            System.out.println("");
            System.out.println("Bitte wählen Sie einen der folgenden Menüpunkte");
            System.out.println("(1) XML Insert auf Artikel");
            System.out.println("(2) XML Update auf Kunde");
            System.out.println("(3) Loesche alle Artikel");
            System.out.println("(0) Beenden");
            System.out.println("");

            try {
                choise = Integer.parseInt(read.readLine());
            } catch (NumberFormatException ex) {
                choise = -1;
            }

            switch (choise) {
                case 1:
                    parseXML("xml/ARTIKEL.xml", new InsertArtikelContentHandler());
                    break;
                case 2:
                    parseXML("xml/KUNDE.xml", new UpdateKundeContentHandler());
                    break;
                case 3:
                    delArtikel();
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

    public static void delArtikel() {

        try (Connection con = ConnectionCreator.createOracleConnection(); Statement sm = con.createStatement()) {
            sm.execute("delete from artikel");
            System.out.println("Alle Artikel gelöscht.");
        } catch (SQLException ex) {
            Logger.getLogger(InsertArtikelContentHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void parseXML(String file, ContentHandler contentHandler) {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            factory.setValidating(true);
            XMLReader xmlReader = factory.newSAXParser().getXMLReader();
            xmlReader.setContentHandler(contentHandler);
            xmlReader.setErrorHandler(new MyErrorHandler());
            xmlReader.parse(new InputSource(new FileReader(file)));
        } catch (FileNotFoundException ex) {
            System.err.println(ex.getMessage());
        } catch (ParserConfigurationException | SAXException | IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
