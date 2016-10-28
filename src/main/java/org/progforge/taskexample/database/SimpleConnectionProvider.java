package org.progforge.taskexample.database;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SimpleConnectionProvider implements ConnectionProvider{

    public SimpleConnectionProvider(){
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            throw new RuntimeException("There is no mysql jdbc driver!");
        }
    }

    private final DatabaseConfiguration config = new DatabaseFileConfiguration(new File("database.properties"));

    public Connection getConnection(){
        try {
            return DriverManager.getConnection(config.getURL(), config.getUser(), config.getPassword());
        } catch (SQLException ex) {
            throw new RuntimeException("Can not connect to the org.progforge.taskexample.database");
        }
    }

}