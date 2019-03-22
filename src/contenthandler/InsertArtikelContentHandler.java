package contenthandler;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class InsertArtikelContentHandler extends ContentHandlerAdapter {

    // Schreibweisen als Konstante festhalten (Änderbarkeit)
    public static final String ARTIKEL = "artikel";

    // Inhalt für Spaltennamen und Werte
    private StringBuilder col = new StringBuilder(), val = new StringBuilder();
    private String datatype;

    @Override
    // Spaltennamen in col speichern und Datentyp festhalten
    // Wenn das Element "Artikel" ist, zwichenspeicher leeren
    public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {

        datatype = atts.getValue("DT");

        if (qName.equals(ARTIKEL)) {
            // Zwichenspeicher leeren
            col = new StringBuilder();
            val = new StringBuilder();
        } else {
            // , setzen, falls nicht das 1. Element
            if (col.length() != 0) {
                col.append(", ");
                val.append(", ");
            }
            col.append(qName);
        }
    }

    @Override
    // Vollständigen insert in die Datenbank schreiben
    public void endElement(String uri, String localName, String qName) throws SAXException {

        if (qName.equals(ARTIKEL)) {
            try (Statement sm = getCon().createStatement()) {
                String stmnt = "insert into artikel(" + col.toString() + ") values(" + val.toString() + ")";
                sm.executeUpdate(stmnt);
                System.out.println(stmnt + ";");
            } catch (SQLException ex) {
                Logger.getLogger(InsertArtikelContentHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    // Werte typspezifisch in val speichern
    public void characters(char[] ch, int start, int length) throws SAXException {
        val.append(strToVal(datatype, new String(ch, start, length)));
    }
}
