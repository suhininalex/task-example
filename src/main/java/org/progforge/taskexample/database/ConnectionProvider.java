package org.progforge.taskexample.database;

import java.sql.Connection;

public interface ConnectionProvider {

    Connection getConnection();
}
