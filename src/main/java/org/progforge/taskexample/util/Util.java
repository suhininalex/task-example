package org.progforge.taskexample.util;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Scanner;

public class Util {
    static public boolean notNull(Object ... objects){
        return Arrays.stream(objects).allMatch( o -> o != null);
    }

    static public String readFile(File file) throws IOException {
        try (Scanner scanner = new Scanner(file)){
            return scanner.useDelimiter("\\A").next();
        }
    }

    static public Long getGeneratedKey(ResultSet resultSet) throws SQLException {
        if (resultSet.next()) {
            return resultSet.getLong(1);
        } else {
            return null;
        }
    }

    /**
     *  Wraps all checked exception!
     */
    public static <R> R withTransaction(Connection connection, CheckedFunction<R> runnable) {
        try {
            connection.setAutoCommit(false);
            R result = runnable.apply();
            connection.commit();
            return result;
        } catch (Throwable ex) {
            try {
                connection.rollback();
            } catch (SQLException e) {
                throw new RuntimeException("Transaction failed.",e);
            }
            throw new RuntimeException("Transaction failed.",ex);
        }
    }

    /**
     *  Wraps all checked exception!
     */
    public static void withTransaction(Connection connection, CheckedProcedure runnable) {
        try {
            connection.setAutoCommit(false);
            runnable.apply();
            connection.commit();
        } catch (Exception ex) {
            try {
                connection.rollback();
            } catch (SQLException e) {
                throw new RuntimeException("Transaction failed.",e);
            }
            throw new RuntimeException("Transaction failed.",ex);
        }
    }

    @FunctionalInterface
    public interface CheckedFunction<R> {
        R apply() throws Exception;
    }

    @FunctionalInterface
    public interface CheckedProcedure {
        void apply() throws Exception;
    }
}
