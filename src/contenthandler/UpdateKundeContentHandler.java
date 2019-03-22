package contenthandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class UpdateKundeContentHandler extends ContentHandlerAdapter {

    // Schreibweisen als Konstante festhalten (Änderbarkeit)
    public static final String UPKUNDE = "UPKUNDE";

    @Override
    // Spaltennamen in col speichern und Datentyp festhalten
    // Wenn das Element "Artikel" ist, zwichenspeicher leeren
    public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
        if (qName.equals(UPKUNDE)) {
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
    }

    private void selectKunde(Statement sm, String knr) {
        try {
            ResultSet rs = sm.executeQuery("SELECT * FROM KUNDE WHERE KNR=" + knr);
            if (rs.next()) {
                System.out.print("KNR: " + rs.getString("KNR") + "\t");
                System.out.print("KNAME: " + rs.getString("KNAME") + "\t");
                System.out.print("PLZ: " + rs.getString("PLZ") + "\t");
                System.out.print("ORT: " + rs.getString("ORT") + "\t");
                System.out.print("STRASSE: " + rs.getString("STRASSE") + "\t");
                System.out.print("KKLIMIT: " + rs.getString("KKLIMIT") + "\t");
                System.out.println();
            }
        } catch (SQLException ex) {
            Logger.getLogger(UpdateKundeContentHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
