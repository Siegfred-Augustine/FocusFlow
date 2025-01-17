package org.Focus_flow;
import java.util.ArrayList;

public class Task {
    enum Hierarchy{
        IMPORTANT,
        HIGH,
        MEDIUM,
        LOW;
    }
    public static ArrayList<Task> taskList = new ArrayList<Task>();
    Hierarchy taskImportance = Hierarchy.LOW;
    private String taskName;
    private String taskDescription;
    private int daysTilDeadline;

    public Task(String name, String description, int deadline){
        taskName = name;
        taskDescription = description;
        daysTilDeadline = deadline;
    }
    public String getTaskName(){
        return this.taskName;
    }
    public String getTaskDescription(){
        return this.taskDescription;
    }
    public int getTaskDeadline(){
        return this.daysTilDeadline;
    }
    public void setTaskName(String input){
        this.taskName = input;
    }
    public void setTaskDescription(String input){
        this.taskDescription = input;
    }
    public void setTaskDeadline(int input){
        this.daysTilDeadline = input;
    }
}
