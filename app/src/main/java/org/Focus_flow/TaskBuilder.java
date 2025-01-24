package org.Focus_flow;

import java.time.LocalDateTime;

public class TaskBuilder extends Task{
    public TaskBuilder(String name, String description, LocalDateTime deadline) {
        super(name, description, deadline);
        addTask(this);
    }
    public TaskBuilder(String name, String description, LocalDateTime deadline, Hierarchy importance) {
        super(name, description, deadline);
        this.taskImportance = importance;
        addTask(this);
    }
}
