package org.Focus_flow;

public class Events extends ToDoList{
    public Events(String name, String description, Hierarchy importance, String unParsedTime) {
        taskName = name;
        taskDescription = description;
        taskImportance = importance;
        setTaskDeadline(unParsedTime);

        eventsList.add(this);
    }
}
