package strategy;

import model.TaskModel;
import view.TaskPlannerView;

import javax.swing.*;

public class DefaultTaskStrategy implements TaskControllerStrategy {
    private TaskModel model;
    private TaskPlannerView view;

    public DefaultTaskStrategy(TaskModel model, TaskPlannerView view) {
        this.model = model;
        this.view = view;
    }

    @Override
    public void handleAddTask() {
        String name = JOptionPane.showInputDialog("Task Name:");
        String description = JOptionPane.showInputDialog("Description:");
        String category = JOptionPane.showInputDialog("Category:");
        String deadline = JOptionPane.showInputDialog("Deadline (YYYY-MM-DD):");

        if (name != null && !name.isEmpty()) {
            model.addTask(name, description, category, deadline);
        }
    }

    @Override
    public void handleDeleteTask() {
        int selectedIndex = view.getTaskList().getSelectedIndex();
        if (selectedIndex != -1) {
            String selectedTask = view.getTaskListModel().get(selectedIndex);
            String taskName = selectedTask.split(" - ")[0].trim();
            model.deleteTask(taskName);
        } else {
            JOptionPane.showMessageDialog(view, "Please select a task to delete.");
        }
    }

    @Override
    public void handleEditTask() {
        int selectedIndex = view.getTaskList().getSelectedIndex();
        if (selectedIndex == -1) {
            JOptionPane.showMessageDialog(view, "Please select a task to edit.");
            return;
        }

        String selectedTask = view.getTaskListModel().get(selectedIndex);
        String taskName = selectedTask.split(" - ")[0].trim();

        JTextField nameField = new JTextField(taskName);
        JTextField descriptionField = new JTextField();
        JTextField categoryField = new JTextField();
        JTextField deadlineField = new JTextField();

        Object[] message = {
                "Task Name:", nameField,
                "Description:", descriptionField,
                "Category:", categoryField,
                "Deadline (YYYY-MM-DD):", deadlineField
        };

        int option = JOptionPane.showConfirmDialog(view, message, "Edit Task", JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            String newName = nameField.getText();
            String newDescription = descriptionField.getText();
            String newCategory = categoryField.getText();
            String newDeadline = deadlineField.getText();

            model.editTask(taskName, newName, newDescription, newCategory, newDeadline);
        }
    }
}
