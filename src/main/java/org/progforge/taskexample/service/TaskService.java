package org.progforge.taskexample.service;

import org.progforge.taskexample.database.DBUtil;
import org.progforge.taskexample.model.Task;
import org.progforge.taskexample.persistance.TaskGateway;
import org.progforge.taskexample.util.Util;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class TaskService {

    private static TaskService instance;

    public static synchronized TaskService getInstance() {
        if (instance == null) {
            instance = new TaskService();
        } else {
            instance.checkConnection();
        }
        return instance;
    }

    private Connection connection = DBUtil.connectionProvider.getConnection();

    private TaskGateway taskGateway = new TaskGateway(connection);

    private void checkConnection(){
        try {
            if (connection.isClosed())
                connection = DBUtil.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException("Can not reopen connection!");
        }
    }

    private TaskService(){
        taskGateway.createTable();
    }

    public Task createTask(String name, Date deadline, int priority){
         return Util.withTransaction(connection, () -> {
            Task task = taskGateway.create();
            task.setName(name);
            task.setDeadline(deadline);
            task.setPriority(priority);
            task.setDone(false);
            taskGateway.add(task);
            return task;
        });
    }

    public List<Task> findAll(){
        return Util.withTransaction(connection, taskGateway::findAll);
    }

    public void setCompleted(long taskId) {
        Util.withTransaction(taskGateway.getConnection(), () -> {
            taskGateway.getConnection().setAutoCommit(false);
            Task task = taskGateway.fetch(taskId);
            if (task != null) {
                task.setDone(true);
                update(task);
            }
        });
    }

    public void delete(Task task) {
        Util.withTransaction(connection, () ->
            taskGateway.delete(task)
        );
    }

    public void update(Task task) {
        Util.withTransaction(connection, () ->
            taskGateway.update(task)
        );
    }

}
