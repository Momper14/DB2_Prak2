package contenthandler;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.validation.TypeInfoProvider;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class InsertBestellAtContentHandler extends ContentHandlerAdapter {

    private static final int TABLE = 1;

    private final TypeInfoProvider tip;

    public InsertBestellAtContentHandler(TypeInfoProvider tip) {
        this.tip = tip;
    }

    // Inhalt für Spaltennamen und Werte
    private StringBuilder val = new StringBuilder();
    private String datatype, table = "BESTELLAT2";
    private boolean noCom = true;

    @Override
    // Spaltennamen in col speichern und Datentyp festhalten
    // Wenn das Element "Artikel" ist, zwichenspeicher leeren
    public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
        if (getLevel() == TABLE) {
            // Zwichenspeicher leeren
            val = new StringBuilder();
            noCom = true;
        } else if (getTypeName().toLowerCase().equals("artlist")) {
            setCom();
            val.append("BESTA_NT(");
            noCom = true;
        } else if (getTypeName().toLowerCase().equals("besta")) {
            setCom();
            val.append("BESTA(");
            noCom = true;
        } else {
            datatype = getTypeName();
        }

        super.startElement(uri, localName, qName, atts);
    }

    @Override
    // Vollständigen insert in die Datenbank schreiben
    public void endElement(String uri, String localName, String qName) throws SAXException {

        super.endElement(uri, localName, qName);

        if (getLevel() == TABLE) {
            try (Statement sm = getCon().createStatement()) {
                String stmnt = "insert into " + table + " values(" + val.toString() + ")";
                sm.executeUpdate(stmnt);
                System.out.println(stmnt + ";");
            } catch (SQLException ex) {
                Logger.getLogger(InsertBestellAtContentHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (getTypeName().toLowerCase().equals("artlist") || getTypeName().toLowerCase().equals("besta")) {
            val.append(")");
        }
    }

    @Override
    // Werte typspezifisch in val speichern
    public void characters(char[] ch, int start, int length) throws SAXException {
        setCom();
        val.append(strToVal(datatype, new String(ch, start, length)));
    }

    private String getTypeName() {
        return tip.getElementTypeInfo().getTypeName();
    }

    private void setCom() {
        if (!noCom) {
            val.append(", ");
        } else {
            noCom = !noCom;
        }
    }
}
