import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.sql.*;


public class PersistenceProvider {

    private final static Logger logger = LoggerFactory.getLogger(PersistenceProvider.class);

    private Connection conn;
    private static PersistenceProvider persistenceProvider;

    public static PersistenceProvider getInstance(){
        if(persistenceProvider == null){
            persistenceProvider = new PersistenceProvider("db_file");
        }
        return persistenceProvider;
    }

    private PersistenceProvider(String db_file_name_prefix) {    // note more general exception

        try {
            Class.forName("org.hsqldb.jdbcDriver");

            conn = DriverManager.getConnection("jdbc:hsqldb:" + db_file_name_prefix,"sa", "");
            //initDb();
        } catch (ClassNotFoundException e) {
            logger.error(e.getMessage(),e);
        } catch (SQLException e) {
            logger.error(e.getMessage(),e);
        }
    }

    public void shutdown() {

        try {
            Statement st = conn.createStatement();
            st.execute("SHUTDOWN");
            conn.close();
            persistenceProvider = null;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public synchronized ResultSet query(String expression) throws SQLException {

        Statement st = null;
        ResultSet rs = null;

        st = conn.createStatement();
        rs = st.executeQuery(expression);
        return rs;
    }

    public synchronized void update(String expression) throws SQLException {

        logger.debug(expression);

        Statement st = null;

        st = conn.createStatement();    // statements

        int i = st.executeUpdate(expression);    // run the query

        if (i == -1) {
            logger.error("db error : " + expression);
        }

        st.close();
    }

    public static void dump(ResultSet rs) throws SQLException {

        ResultSetMetaData meta   = rs.getMetaData();
        int               colmax = meta.getColumnCount();
        int               i;
        Object            o = null;

        for (; rs.next(); ) {
            for (i = 0; i < colmax; ++i) {
                o = rs.getObject(i + 1);    // Is SQL the first column is indexed

                // with 1 not 0
                logger.debug (o.toString() + " ");
            }

            logger.debug(" ");
        }
    }                                       //void dump( ResultSet rs )

    public void initDb(){
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        InputStream stream = loader.getResourceAsStream("db_creation_script.txt");

        BufferedReader bufferedReader = null;
        InputStreamReader inputStreamReader = null;

        try {

            inputStreamReader = new InputStreamReader(stream);
            bufferedReader = new BufferedReader(inputStreamReader);

            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                update(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                inputStreamReader.close();
                bufferedReader.close();
            } catch (IOException ex) {
                logger.error(ex.getMessage(),ex);
            }
        }


    }

    public PreparedStatement prepareStatement(String sql){
        try {
            return conn.prepareStatement(sql);
        } catch (SQLException e) {
            logger.error(e.getMessage(),e);
        }
        return null;
    }


    public static void main(String[] args) {

        PersistenceProvider db = null;

        try {
            db = new PersistenceProvider("db_file");
        } catch (Exception ex1) {
            logger.error(ex1.getMessage(),ex1);   // could not start db

            return;                   // bye bye
        }

        db.initDb();
        db.shutdown();
    }
}

