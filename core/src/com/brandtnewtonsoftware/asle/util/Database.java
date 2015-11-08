package com.brandtnewtonsoftware.asle.util;

import java.io.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

/**
 * Helper class for all interactions with the SQLite database used for this program.
 */
public class Database {
    public static final String DATABASE_FILE = "data/AslTutor.db";
    private static final long VERSION = 2;

    public Database() {
        try {
            if (VERSION != getVersion()) {
                runScripts();
                setVersion();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        Connection connection = null;
        try {
            DriverManager.registerDriver(new org.sqlite.JDBC());
            connection = DriverManager.getConnection("jdbc:sqlite:" + DATABASE_FILE);
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    private void setVersion() throws SQLException {
        Connection connection = getConnection();
        PreparedStatement stmt = null;

        try{
            stmt = connection.prepareStatement("PRAGMA USER_VERSION = " + VERSION);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (stmt != null) {
                stmt.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }

    private long getVersion() throws SQLException {
        long version = 0;
        Connection connection = getConnection();
        Statement stmt = null;
        try {
            stmt = connection.createStatement();
            ResultSet results = stmt.executeQuery("PRAGMA user_version");
            results.next();
            version = results.getLong(1);
            results.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (stmt != null) {
                stmt.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
        return version;
    }

    private void runScripts() throws Exception {
        final String scriptPath = "data/scripts";
        Connection connection = getConnection();

        File[] files = new File(scriptPath).listFiles();

        if (files == null) {
            System.err.println("No scripts at " + scriptPath);
            return;
        }

        Arrays.sort(files);

        System.out.println("Running scripts.");

        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            if (file.isFile()) {
                try {
                    String sql = readFile(file);
                    Statement stmt = connection.createStatement();
                    stmt.executeUpdate(sql);
                    stmt.close();
                    System.out.println("Script " + (i + 1) + " passed.");
                } catch (Exception e) {
                    System.err.println("There was an error with the following script: " + file.getName());
                    throw new Exception(e);
                }
            }
        }
        connection.close();
        System.out.println("Finished running all scripts.");
    }
    private String readFile(File file) throws IOException {
        FileInputStream fis = new FileInputStream(file);
        byte[] data = new byte[(int) file.length()];
        fis.read(data);
        fis.close();
        return new String(data, "UTF-8");
    }

    public static boolean isSafeChar(char character) {
        return ('0' <= character && character <= '9') ||
                ('A' <= character && character <= 'Z') ||
                ('a' <= character && character <= 'z') || character == ' ';
    }

    public static boolean isSafeString(CharSequence str) {
        for (int i = 0; i < str.length(); i++) {
            if (!isSafeChar(str.charAt(i)))
                return false;
        }
        return true;
    }

    public static String sanitizeString(CharSequence str) {
        StringBuilder strBuilder = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (isSafeChar(c))
                strBuilder.append(c);
        }
        return strBuilder.toString();
    }

    public static String dateToSQLiteString(Date date) {
        return dateToSQLiteString(date, true);
    }

    public static String dateToSQLiteString(Date date, boolean includeTime) {
        final String format = "yyyy-MM-dd" + ((includeTime)? " HH:mm:ss" : "");
        SimpleDateFormat iso8601Format = new SimpleDateFormat(
                format, Locale.US);
        return iso8601Format.format(date);
    }
}
