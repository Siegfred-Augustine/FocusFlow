package org.Focus_flow;

import java.time.LocalDateTime;
import java.util.Collections;

public class Task extends ToDoList{

    public Task(String name, String description, Hierarchy importance, String unParsedTime) {
        taskName = name;
        taskDescription = description;
        taskImportance = importance;
        setTaskDeadline(unParsedTime);
    }

    public static void addTask(Task task) {
        taskListImportance.add(task);
        taskList.add(task);
        Collections.sort(taskListImportance, (a, b) -> Integer.compare(b.taskImportance.getOrder(), a.taskImportance.getOrder()));

        for(int i = 0; i<taskList.size()-1; i++){
            for(int j = 0; j<taskList.size()-1 - i; j++) {
                if (!taskList.get(j).deadline.isBefore(taskList.get(j + 1).deadline)) {
                    LocalDateTime temp = taskList.get(j).deadline;
                    taskList.get(j).deadline = taskList.get(j + 1).deadline;
                    taskList.get(j + 1).deadline = temp;
                }
            }
        }
    }
}
