package org.Focus_flow;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;

public class Task {
    public enum Hierarchy{
        IMPORTANT(4),
        HIGH(3),
        MEDIUM(2),
        LOW(1);

        private final int order;

        Hierarchy(int order) {
            this.order = order;
        }
        public int getOrder() {
            return order;
        }
    }
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyy HH:mm");
    public static ArrayList<Task> taskList = new ArrayList<Task>();
    Hierarchy taskImportance = Hierarchy.LOW;
    private String taskName;
    private String taskDescription;
    private LocalDateTime deadline;

    public Task(String name, String description, LocalDateTime deadline){
        taskName = name;
        taskDescription = description;
        this.deadline = deadline;
    }
    public String getTaskName(){
        return this.taskName;
    }
    public String getTaskDescription(){
        return this.taskDescription;
    }
    public LocalDateTime getTaskDeadline(){
        return this.deadline;
    }
    public void setTaskName(String input){
        this.taskName = input;
    }
    public void setTaskDescription(String input){
        this.taskDescription = input;
    }
    public void setTaskDeadline(LocalDateTime input){
        this.deadline = input;
    }
    public boolean isDue(){
        LocalDateTime current = LocalDateTime.now();
        return !current.isBefore(this.deadline);
    }
    public static void addTask(Task task) {
        taskList.add(task);
        Collections.sort(taskList, (a, b) -> Integer.compare(b.taskImportance.getOrder(), a.taskImportance.getOrder()));
    }
}
