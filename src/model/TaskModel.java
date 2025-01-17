package model;

import observer.Subject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class TaskModel extends Subject {
    private DatabaseManager dbManager;

    public TaskModel(DatabaseManager dbManager) {
        this.dbManager = dbManager;
    }

    public void addTask(String name, String description, String category, String deadline) {
        try (Connection conn = dbManager.getConnection()) {
            String query = "INSERT INTO tasks (task_name, description, category, deadline) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, name);
            stmt.setString(2, description);
            stmt.setString(3, category);
            stmt.setString(4, deadline);
            stmt.executeUpdate();
            notifyObservers();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void editTask(String oldTaskName, String newName, String description, String category, String deadline) {
        try (Connection conn = dbManager.getConnection()) {
            String query = "UPDATE tasks SET task_name = ?, description = ?, category = ?, deadline = ? WHERE task_name = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, newName);
            stmt.setString(2, description);
            stmt.setString(3, category);
            stmt.setString(4, deadline);
            stmt.setString(5, oldTaskName);
            stmt.executeUpdate();

            notifyObservers();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteTask(String name) {
        try (Connection conn = dbManager.getConnection()) {
            String query = "DELETE FROM tasks WHERE task_name = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, name);
            stmt.executeUpdate();
            notifyObservers();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<String> checkDeadlines() {
        List<String> notifications = new ArrayList<>();
        try (Connection conn = dbManager.getConnection()) {
            String query = "SELECT task_name, deadline FROM tasks";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate today = LocalDate.now();

            while (rs.next()) {
                String taskName = rs.getString("task_name");
                String deadline = rs.getString("deadline");

                LocalDate deadlineDate = LocalDate.parse(deadline, formatter);

                if (deadlineDate.minusDays(1).isEqual(today)) {
                    notifications.add("Task '" + taskName + "' is due tomorrow!");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return notifications;
    }

    public List<String> getTasks() {
        List<String> tasks = new ArrayList<>();
        try (Connection conn = dbManager.getConnection()) {
            String query = "SELECT * FROM tasks";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String task = rs.getString("task_name") + " - " + rs.getString("description");
                tasks.add(task);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tasks;
    }
}
