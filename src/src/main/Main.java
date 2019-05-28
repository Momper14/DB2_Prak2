package main;

import connection.ConnectionCreator;
import contenthandler.InsertArtikelContentHandler;
import contenthandler.InsertBestellAtContentHandler;
import errorhandler.MyErrorHandler;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Struct;
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
            System.out.println("(1) XML Insert auf Artikel");
            System.out.println("(2) XML Insert auf BestellAt");
            System.out.println("(3) Select auf BestellAt");
            System.out.println("(4) Lösche inhalt von BestellAt");
            System.out.println("(0) Beenden");
            System.out.println("");

            try {
                choise = Integer.parseInt(read.readLine());
            } catch (NumberFormatException ex) {
                choise = -1;
            }

            switch (choise) {
                case 1: {
                    ValidatorHandler validatorHandler = createValidatorHandler("ARTIKEL1.xsd");
                    ContentHandler contentHandler = new InsertArtikelContentHandler(validatorHandler.getTypeInfoProvider());
                    validatorHandler.setContentHandler(contentHandler);
                    parseXML("xml/ARTIKEL1.xml", validatorHandler);
                    break;
                }
                case 2: {
                    ValidatorHandler validatorHandler = createValidatorHandler("BESTELLAT.xsd");
                    ContentHandler contentHandler = new InsertBestellAtContentHandler(validatorHandler.getTypeInfoProvider());
                    validatorHandler.setContentHandler(contentHandler);
                    parseXML("xml/BESTELLAT.xml", validatorHandler);
                    break;
                }
                case 3: {
                    selectOnBestellAt();
                    break;
                }
                case 4: {
                    delBestellAt();
                    break;
                }
                case 0:
                    System.out.println("Auf Wiedersehen");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Ungültige eingabe");
            }
        } while (true);
    }

    private static void delBestellAt() {
        try (Connection con = ConnectionCreator.createOracleConnection(); Statement sm = con.createStatement()) {
            sm.execute("delete from BESTELLAT");
            System.out.println("Alle Bestellungen gelöscht.");
        } catch (SQLException ex) {
            Logger.getLogger(InsertArtikelContentHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
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
            System.out.println(ex.getMessage());
            //Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static ValidatorHandler createValidatorHandler(String schema) throws SAXException, IOException {
        SchemaFactory schemaFactory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
        return schemaFactory.newSchema(new File(schema)).newValidatorHandler();
    }

    private static void selectOnBestellAt() {
        String format = "%-25s%-15s%-15s%n";

        try (Connection con = ConnectionCreator.createOracleConnection(); Statement sm = con.createStatement()) {
            ResultSet rs = sm.executeQuery("SELECT * FROM BESTELLAT");
            while (rs.next()) {
                System.out.println("");
                System.out.print("Bestellnummer: " + rs.getString("BSTNR"));
                System.out.print('\t');
                System.out.print("Kundennummer: " + rs.getString("KNR"));
                System.out.print('\t');
                System.out.print("Rechnungsnummer: " + rs.getString("RSUM"));
                System.out.println("");
                Object[] artList = (Object[]) rs.getArray("ARTLIST").getArray();
                for (Object bestA : artList) {
                    Object[] values = ((Struct) bestA).getAttributes();
                    System.out.print('\t');
                    try (Statement stmtTmp = con.createStatement()) {
                        ResultSet rsArt = stmtTmp.executeQuery("SELECT ARTBEZ FROM ARTIKEL WHERE ARTNR = " + values[0]);
                        rsArt.next();
                        System.out.printf(format, "Artikel: " + rsArt.getString("ARTBEZ"), "Menge: " + values[1], "Wert: " + values[2]);
                    }
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(InsertArtikelContentHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
