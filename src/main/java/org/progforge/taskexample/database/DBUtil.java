package org.progforge.taskexample.database;

import java.sql.Connection;

public class DBUtil {
    public static final ConnectionProvider connectionProvider = new SimpleConnectionProvider();

    public static Connection getConnection(){
        return connectionProvider.getConnection();
    }
}
