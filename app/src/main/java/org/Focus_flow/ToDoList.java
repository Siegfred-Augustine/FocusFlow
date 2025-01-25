package org.Focus_flow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import static org.Focus_flow.TaskBuilder.addTaskUI;

public class ToDoList {
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
    public DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyy HH:mm");
    Hierarchy taskImportance = Hierarchy.LOW;
    String taskName;
    String taskDescription;
    LocalDateTime deadline;
    static ArrayList<Task> taskList = new ArrayList<>();
    static ArrayList<Task> taskListImportance = new ArrayList<>();
    static ArrayList<RecurringTask> recurringtasklist = new ArrayList<>();
    static ArrayList<Events> eventsList = new ArrayList<>();
    static ArrayList<Task> doneList = new ArrayList<>();

    static JFrame frame = new JFrame("Test Window");
    JButton button = new JButton("Add Task");
    public void UIbuilder(){
        frame.setSize(400,500);
        frame.setLayout(new GridLayout(10,1));
        frame.add(button);
        button.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                addTaskUI();
            }
        });
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void setTaskDeadline(String input){
        try{
            this.deadline = LocalDateTime.parse(input, formatter);
        } catch(Exception e){
            JOptionPane.showMessageDialog(null, "Invalid Date Format");
            taskListImportance.remove(taskListImportance.getLast());
        }
    }
    public void deadLineChecker() {
        LocalDateTime current = LocalDateTime.now();
        for (Task t : taskListImportance) {
            if (!current.isBefore(t.deadline)) {
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(null, "Deadline reached for: " + t.taskName, "Deadline Notification", JOptionPane.WARNING_MESSAGE);
                });
                doneList.add(t);
            }
        }
        taskListImportance.removeAll(doneList);
    }
}

