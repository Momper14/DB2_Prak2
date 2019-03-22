package connection;

import contenthandler.ContentHandlerAdapter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConnectionCreator {

    private ConnectionCreator() {

    }

    public static Connection createOracleConnection() throws SQLException {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            return DriverManager.getConnection("jdbc:oracle:thin:@schelling.nt.fh-koeln.de:1521:xe", "dbprak13", "dbprak13");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ContentHandlerAdapter.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(1);
            return null;
        }
    }
}
