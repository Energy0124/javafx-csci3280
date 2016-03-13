/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db;

import java.sql.*;
import java.util.ArrayList;

/**
 *
 * @author Administrator
 */
public class SQLite {

    private String dbName;
    private Connection c;
    Statement stmt;

    public SQLite(String dbName) {
        try {
            Class.forName("org.sqlite.JDBC");
            this.c = DriverManager.getConnection("jdbc:sqlite:" + dbName);
            this.stmt = this.c.createStatement();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

    public ResultSet displayAll() throws SQLException {
        ResultSet rs = stmt.executeQuery("SELECT * FROM playlist");
        return rs;
    }

    public ResultSet search(String keySeq) throws SQLException {
        String keys[] = keySeq.split("\\s+");
        String cols[] = {"title", "album", "singer"};
        ArrayList<String> cond_part = new ArrayList<String>();
        String cond_str = null;
        for (int i=0; i<cols.length; i++)
            for (int j=0; j<keys.length; j++)
                cond_part.add(cols[i] + " LIKE '%" + keys[j] + "%'");
        ResultSet rs = stmt.executeQuery("SELECT * FROM playlist WHERE " + String.join(" OR ", cond_part));
        return rs;
    }

    public void close() {
        try {
            this.c.close();
        } catch (SQLException e) {

        }
    }
}
