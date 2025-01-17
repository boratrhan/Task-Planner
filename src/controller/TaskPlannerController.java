package controller;

import model.TaskModel;
import strategy.TaskControllerStrategy;
import strategy.DefaultTaskStrategy;
import view.TaskPlannerView;

import javax.swing.*;

import java.util.List;

public class TaskPlannerController {
    private TaskModel model;
    private TaskPlannerView view;
    private TaskControllerStrategy strategy;

    public TaskPlannerController(TaskModel model, TaskPlannerView view) {
        this.model = model;
        this.view = view;
        this.strategy = new DefaultTaskStrategy(model, view);

        model.addObserver(() -> updateTaskList());

        view.getAddTaskButton().addActionListener(e -> {
            strategy.handleAddTask();
            updateTaskList();
        });
        view.getDeleteTaskButton().addActionListener(e -> {
            strategy.handleDeleteTask();
            updateTaskList();
        });
        view.getEditTaskButton().addActionListener(e -> {
            strategy.handleEditTask();
            updateTaskList();
        });

        updateTaskList();

        Timer timer = new Timer(6000, e -> checkDeadlines());
        timer.start();
    }

    public void setStrategy(TaskControllerStrategy strategy) {
        this.strategy = strategy;
    }

    public List<String> fetchNotifications() {
        return model.checkDeadlines();
    }

    private void updateTaskList() {
        view.getTaskListModel().clear();
        for (String task : model.getTasks()) {
            view.getTaskListModel().addElement(task);
        }
        view.getTaskList().revalidate();
        view.getTaskList().repaint();
        System.out.println("Task list updated: " + model.getTasks());
    }


    private void checkDeadlines() {
        List<String> notifications = model.checkDeadlines();
        StringBuilder notificationsText = new StringBuilder();
        for (String notification : notifications) {
            notificationsText.append(notification).append("\n");
        }
        view.getNotificationsArea().setText(notificationsText.toString());
    }
}
