package contenthandler;

import connection.ConnectionCreator;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

public abstract class ContentHandlerAdapter implements ContentHandler {

    private Connection con;

    @Override
    // Nichts
    public void setDocumentLocator(Locator locator) {
    }

    @Override
    // Verbindung aufbauen
    public void startDocument() throws SAXException {
        try {
            con = ConnectionCreator.createOracleConnection();
        } catch (SQLException ex) {
            Logger.getLogger(InsertArtikelContentHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    // Verbindung beenden
    public void endDocument() throws SAXException {
        try {
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(InsertArtikelContentHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    // Nichts
    public void startPrefixMapping(String prefix, String uri) throws SAXException {
    }

    @Override
    // Nichts
    public void endPrefixMapping(String prefix) throws SAXException {
    }

    @Override
    // Nichts
    public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
    }

    @Override
    // Nichts
    public void endElement(String uri, String localName, String qName) throws SAXException {
    }

    @Override
    // Nichts
    public void characters(char[] ch, int start, int length) throws SAXException {
    }

    @Override
    // Nichts
    public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
    }

    @Override
    // Nichts
    public void processingInstruction(String target, String data) throws SAXException {
    }

    @Override
    // Nichts
    public void skippedEntity(String name) throws SAXException {
    }

    public Connection getCon() {
        return con;
    }

    // passt den String für das Einfügen in die Datenbank an anhand des Datentyps (z.b. banane -> 'banane')
    public String strToVal(String datatype, String str) {
        // Unterscheidung in Typen, da angabe unterschiedlich
        if (datatype.toLowerCase().contains("char")) {
            return "'" + str + "'";
        } else if (datatype.toLowerCase().equals("date")) {
            return "TO_DATE('" + str + "','DD.MM.YYYY')";
        } else {
            return str;
        }
    }
}
