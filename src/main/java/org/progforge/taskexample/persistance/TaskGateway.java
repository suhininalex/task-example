package org.progforge.taskexample.persistance;

import org.progforge.taskexample.model.TaskImpl;
import org.progforge.taskexample.model.Task;
import org.progforge.taskexample.util.Util;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class TaskGateway {

    private Connection connection;

    private final String table = "TASK";

    public TaskGateway(Connection connection) {
        this.connection = connection;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public void createTable() {
        try {
            ResultSet resultSet = connection.getMetaData().getTables(null, null, table, null);
            if ( ! resultSet.next()){
                String path = getClass().getClassLoader().getResource("sql/tables.sql").getFile();
                String sql = Util.readFile(new File(path));
                Statement statement = connection.createStatement();
                statement.executeUpdate(sql);
            }
        } catch (SQLException | IOException e) {
            throw new RuntimeException("Can not create table " + table +".");
        }
    }

    public Task create(){
        return new TaskImpl();
    }

    private final String statement_add = "INSERT INTO " + table + " (NAME, DEADLINE, PRIORITY, DONE) VALUES (?, ?, ?, ?)";
    public void add(Task task) throws SQLException {
        checkTask(task);
        PreparedStatement statement = connection.prepareStatement(statement_add, Statement.RETURN_GENERATED_KEYS);
        statement.setString(1, task.getName());
        statement.setDate(2, new Date(task.getDeadline().getTime()));
        statement.setInt(3, task.getPriority());
        statement.setBoolean(4, task.isDone());
        statement.execute();
        task.setId(Util.getGeneratedKey(statement.getGeneratedKeys()));
    }

    private final String statement_fetch = "SELECT * FROM " + table + " WHERE ID_TASK=?";
    public Task fetch(long id) throws SQLException {
        PreparedStatement statement = connection.prepareCall(statement_fetch);
        statement.setLong(1, id);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()){
            return loadTask(resultSet);
        } else {
            return null;
        }
    }

    private final String statement_update = "UPDATE " + table + " SET NAME=?, DEADLINE=?, PRIORITY=?, DONE=? WHERE ID_TASK=?";
    public void update(Task task) throws SQLException {
        checkTask(task);
        PreparedStatement statement = connection.prepareStatement(statement_update);
        statement.setString(1, task.getName());
        statement.setDate(2, new Date(task.getDeadline().getTime()));
        statement.setInt(3, task.getPriority());
        statement.setBoolean(4, task.isDone());
        statement.setLong(5, task.getId());
        statement.executeUpdate();
    }

    private final String statement_delete = "DELETE FROM " + table + " WHERE ID_TASK=?";
    public void delete(Task task) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(statement_delete);
        statement.setLong(1, task.getId());
        statement.execute();
    }

    private final String statement_findAll = "SELECT * FROM " + table;
    public List<Task> findAll() throws SQLException {
        PreparedStatement statement = connection.prepareCall(statement_findAll);
        ResultSet resultSet = statement.executeQuery();

        List<Task> list = new ArrayList<>();
        while (resultSet.next()){
            list.add(loadTask(resultSet));
        }

        return list;
    }

    private Task loadTask(ResultSet resultSet) throws SQLException {
        Task task = create();
        task.setId(resultSet.getLong("ID_TASK"));
        task.setName(resultSet.getString("NAME"));
        task.setDeadline(resultSet.getDate("DEADLINE"));
        task.setPriority(resultSet.getInt("PRIORITY"));
        task.setDone(resultSet.getBoolean("DONE"));
        return task;
    }

    private void checkTask(Task task){
        if ( task == null) throw new IllegalArgumentException("Task must not be null.");

        if ( ! Util.notNull(task.getName(), task.getDeadline(), task.getPriority(), task.getPriority(), task.isDone()))
            throw new IllegalArgumentException("Some of task fields are null ID(" + task.getId() + ").");
    }


}
