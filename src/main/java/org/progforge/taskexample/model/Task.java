package org.progforge.taskexample.model;

import java.util.Date;

public interface Task {

    Long getId();

    String getName();

    Date getDeadline();

    Integer getPriority();

    Boolean isDone();

    void setId(long id);

    void setName(String name);

    void setDeadline(Date expires);

    void setPriority(int priority);

    void setDone(boolean isDone);
}
