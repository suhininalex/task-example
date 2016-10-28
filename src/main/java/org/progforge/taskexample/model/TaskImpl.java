package org.progforge.taskexample.model;

import java.util.Date;

public class TaskImpl implements Task {
    private Long id;
    private String name;
    private Date expires;
    private Integer priority;
    private Boolean isDone;

    @Override
    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Date getDeadline() {
        return expires;
    }

    public void setDeadline(Date expires) {
        this.expires = expires;
    }

    @Override
    public Integer getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    @Override
    public Boolean isDone() {
        return isDone;
    }

    @Override
    public void setDone(boolean done) {
        isDone = done;
    }
}
