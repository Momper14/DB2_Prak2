package contenthandler;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class UpdateKundeContentHandler extends ContentHandlerAdapter {

    private static final int UPDATE = 1;

    private int level = 0;

    // Schreibweisen als Konstante festhalten (Änderbarkeit)
    public static final String UPKUNDE = "UPKUNDE";

    @Override
    // Spaltennamen in col speichern und Datentyp festhalten
    // Wenn das Element "Artikel" ist, zwichenspeicher leeren
    public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
        if (level == UPDATE) {
            String col, val, knr;
            knr = atts.getValue("KNR");
            col = atts.getValue("USP");
            val = strToVal(atts.getValue("DTUSP"), atts.getValue("UWERT"));
            try (Statement sm = getCon().createStatement()) {
                selectKunde(sm, knr);
                sm.execute("UPDATE KUNDE SET " + col + "=" + val + " where KNR=" + knr);
                selectKunde(sm, knr);
                System.out.println();
            } catch (SQLException ex) {
                Logger.getLogger(InsertArtikelContentHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        level++;
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        level--;
    }

    private void selectKunde(Statement sm, String knr) {

        try {
            ResultSet rs = sm.executeQuery("SELECT * FROM KUNDE WHERE KNR=" + knr);
            ResultSetMetaData rsm = rs.getMetaData();
            ArrayList<String> columnName = new ArrayList<String>();
            for (int i = 1; i <= rsm.getColumnCount(); i++) {
                columnName.add(rsm.getColumnName(i));
            }
            if (rs.next()) {
                for (String name : columnName) {
                    System.out.print(name + ": " + rs.getString(name) + "\t");
                }
                System.out.println();
            }
        } catch (SQLException ex) {
            Logger.getLogger(UpdateKundeContentHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
