package b2baqjms;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.naming.NamingException;

import oracle.sql.BLOB;
import oracle.sql.CLOB;


public class OracleDBConnection {
    private static final String CHARACTER_SET = "UTF-8";
    private static final String DEFAULT_CHARSET = "UTF-8";
    private static final String UTF8_CHARSET = "UTF-8";
    private static final String UTF16BE_CHARSET = "UTF-16BE";
    private static final String UTF16LE_CHARSET = "UTF-16LE";
    // Return a JDBC Connection appropriately either outside or inside Oracle8i
    // Connection info is only needed for testing java outside the database

    public static Connection getConnection(String username, String password, 
                                           String hostname, int port, 
                                           String SID) throws SQLException {
        String thinConn;
        String default8iConn = "jdbc:oracle:kprb:";
        Connection cn = null;
        try {
            thinConn = 
                    "jdbc:oracle:thin:@" + hostname + ":" + port + ":" + SID;
            // Register the JDBC Driver
            Driver d = new oracle.jdbc.driver.OracleDriver();
            // Connect with the Native (kprb) Driver if inside Oracle8i
            if (!insideOracleDB()) { // Connect with the Thin Driver
                cn = DriverManager.getConnection(thinConn, username, password);
            } else {
                cn = DriverManager.getConnection(default8iConn);
            }
            // Make sure JDBC Auto-Commit is off.
            cn.setAutoCommit(false);
            return cn;
        } catch (Exception e) {
            throw new SQLException("Error Loading JDBC Driver"+e.getMessage());
        }
    }

    public static Connection getConnection(String username, String password, 
                                           String connectString) throws SQLException {
        String thinConn;
        String default8iConn = "jdbc:oracle:kprb:";
        Connection cn = null;
        try {
            thinConn = "jdbc:oracle:thin:@" + connectString;
            // Register the JDBC Driver
            Driver d = new oracle.jdbc.driver.OracleDriver();
            // Connect with the Native (kprb) Driver if inside Oracle8i
            if (!insideOracleDB()) { // Connect with the Thin Driver
                cn = DriverManager.getConnection(thinConn, username, password);
            } else {
                cn = DriverManager.getConnection(default8iConn);
            }
            // Make sure JDBC Auto-Commit is off.
            cn.setAutoCommit(false);
            return cn;
        } catch (Exception e) {
            throw new SQLException("Error Loading JDBC Driver");
        }
    }

    public static boolean insideOracleDB() {
        // If oracle.server.version is non null, we're running in the database
        String ver = System.getProperty("oracle.server.version");
        return (ver != null && !ver.equals(""));
    }


    public static CLOB createCLOB(Connection sqlConnection, 
                                  String content) throws Exception {
        CLOB tmpClob = null;
        try {
            System.out.println("Try to create CLOB");
            tmpClob = 
                    CLOB.createTemporary(sqlConnection, true, CLOB.DURATION_SESSION);
            tmpClob.open(CLOB.MODE_READWRITE);
            Writer tmpClobWriter = tmpClob.setCharacterStream(1L);
            tmpClobWriter.write(content);
            tmpClobWriter.flush();
            tmpClobWriter.close();
            tmpClob.close();
            System.out.println("Return created CLOB");
            return tmpClob;
        } catch (Exception exp) {
            exp.printStackTrace();
            throw new Exception("AQ - 1020");
        }
    }

    public static String readCLOB(CLOB clob) throws Exception {
        String retValue;
        int noChars;
        long clobLength;
        System.out.println("Read Clob");
        try {
            clobLength = clob.getLength();
            System.out.println("Clob Length: " + clobLength);
            System.out.println("Create Reader");
            Reader clobReader = clob.getCharacterStream(1L);
            System.out.println("Read into Buffer");
            // Read CLOB data from CLOB locator into Reader clobReader.
            char[] buffer = new char[(int)clobLength];
            noChars = clobReader.read(buffer, 0, (int)clobLength);
            System.out.println("Copy buffer into string");
            retValue = String.valueOf(buffer);
            return retValue;
        } catch (Exception exp) {
            exp.printStackTrace();
            throw new Exception("AQ - 1030");
        }
    }

    /**
     * Utillity method to create and fill a BLOB object.
     * 
     * @param stringContent the offered content that is embedded in the BLOB
     * @return a temporary BLOB object
     * @throws SQLException if an SQL exception occurs
     * @throws NamingException if a naming exception occurs
     * @throws IOException if an IO exception occurs
     */
    public static BLOB createBLOB(Connection sqlConnection, 
                                  String stringContent) throws SQLException, 
                                                               NamingException, 
                                                               IOException {
        System.out.println("Try to create BLOB");
        byte[] content = stringContent.getBytes(CHARACTER_SET);
        BLOB returnValue = 
            BLOB.createTemporary(sqlConnection, false, BLOB.DURATION_SESSION);
        OutputStream outStream = null;
        try {
            outStream = returnValue.setBinaryStream(1L);
            outStream.write(content);
            outStream.flush();
        } finally {
            if (outStream != null) {
                outStream.close();
            }
        }
        System.out.println("Return created BLOB");
        return returnValue;
    }

    public static String readBLOB(BLOB blob) throws Exception {
        StringBuffer messageBuffer = new StringBuffer();
        BufferedInputStream bufferedInputStream = null;

        try {
            bufferedInputStream = 
                    new BufferedInputStream(blob.getBinaryStream());
            bufferedInputStream.mark(5);

            String usedCharset = DEFAULT_CHARSET;

            long strip = 0L;
            byte[] bytes = new byte[3];
            bufferedInputStream.read(bytes);
            if ((bytes[0] == (byte)0xEF) && (bytes[1] == (byte)0xBB) && 
                (bytes[2] == (byte)0xBF)) {
                usedCharset = UTF8_CHARSET;
                strip = 3L;
            } else if ((bytes[0] == (byte)0xFE) && (bytes[1] == (byte)0xFF)) {
                usedCharset = UTF16BE_CHARSET;
                strip = 2L;
            } else if ((bytes[0] == (byte)0xFF) && (bytes[1] == (byte)0xFE)) {
                usedCharset = UTF16LE_CHARSET;
                strip = 2L;
            }

            bufferedInputStream.reset();
            bufferedInputStream.skip(strip);
            BufferedReader reader = 
                new BufferedReader(new InputStreamReader(bufferedInputStream, 
                                                         usedCharset));

            int nextChar = reader.read();
            while (nextChar != -1) {
                messageBuffer.append((char)nextChar);
                nextChar = reader.read();
            }
        } catch (IOException e) {
            throw new Exception("Error reading payload from message", e);
        } catch (SQLException e) {
            throw new Exception("Error reading payload from message", e);
        } finally {
            try {
                if (bufferedInputStream != null) {
                    bufferedInputStream.close();
                }

            } catch (IOException e) {
                throw new Exception("Could not close inputstream", e);
            }
        }

        return messageBuffer.toString();
    }
}
