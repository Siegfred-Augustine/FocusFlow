package org.Focus_flow;

public class RecurringTask extends ToDoList {
    public RecurringTask(String name, String description, ToDoList.Hierarchy importance, String unParsedTime) {
        taskName = name;
        taskDescription = description;
        taskImportance = importance;
        setTaskDeadline(unParsedTime);

        recurringtasklist.add(this);
    }
}
