package view;

import controller.TaskPlannerController;
import observer.Observer;

import javax.swing.*;
import java.awt.*;
import decorator.*;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class TaskPlannerView extends JFrame implements Observer {
    private JTextField dayField, dateField;
    private JLabel birthdayMessageValue;
    private DefaultListModel<String> taskListModel;
    private JList<String> taskList;
    private JButton addTaskButton, deleteTaskButton, editTaskButton;
    private JTextArea notificationsArea;
    private TaskPlannerController controller;

    private List<String> staticNotifications;

    private List<String> fetchNotificationsFromDB() {
        if (controller != null) {
            return controller.fetchNotifications();
        }
        return Arrays.asList();
    }

    public TaskPlannerView() {
        setTitle("Task Planner");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        staticNotifications = fetchNotificationsFromDB();

        Composite topPanel = createTopPanel();
        add(topPanel.render(), BorderLayout.NORTH);

        Composite centerPanel = createCenterPanel();
        add(centerPanel.render(), BorderLayout.CENTER);

        initializeDayAndDate();
        startDayDateTimer();
        updateStaticNotifications();

        setVisible(true);
    }

    private Composite createTopPanel() {
        Composite topPanel = new Composite(new GridLayout(2, 1, 10, 10));

        Composite dayDatePanel = new Composite(new GridLayout(1, 4, 5, 5));
        dayDatePanel.addComponent(new Leaf(new JLabel("Day:")));
        dayField = new JTextField();
        dayField.setEditable(false);
        dayDatePanel.addComponent(new Leaf(dayField));

        dayDatePanel.addComponent(new Leaf(new JLabel("Date:")));
        dateField = new JTextField();
        dateField.setEditable(false);
        dayDatePanel.addComponent(new Leaf(dateField));

        Composite birthdayPanel = new Composite(new BorderLayout());
        birthdayPanel.addComponent(new Leaf(new JLabel("Birthday Celebration Message:")), BorderLayout.WEST);
        birthdayMessageValue = new JLabel("");
        birthdayPanel.addComponent(new Leaf(birthdayMessageValue), BorderLayout.CENTER);

        topPanel.addComponent(dayDatePanel);
        topPanel.addComponent(birthdayPanel);

        return topPanel;
    }

    private Composite createCenterPanel() {
        Composite centerPanel = new Composite(new GridLayout(1, 2, 10, 10));
        Composite notificationsPanel = new Composite(new BorderLayout());

        notificationsPanel.addComponent(new Leaf(new JLabel("Notifications")), BorderLayout.NORTH);
        notificationsArea = new JTextArea(10, 20);
        notificationsArea.setEditable(false);
        notificationsPanel.addComponent(new Leaf(new JScrollPane(notificationsArea)), BorderLayout.CENTER);

        Composite taskListPanel = new Composite(new BorderLayout());
        Composite taskButtonsPanel = new Composite(new GridLayout(1, 3, 10, 5));
        addTaskButton = new JButton("Add Task");
        deleteTaskButton = new JButton("Delete Task");
        editTaskButton = new JButton("Edit Task");

        taskButtonsPanel.addComponent(new Leaf(addTaskButton));
        taskButtonsPanel.addComponent(new Leaf(deleteTaskButton));
        taskButtonsPanel.addComponent(new Leaf(editTaskButton));

        taskListPanel.addComponent(taskButtonsPanel, BorderLayout.NORTH);

        taskListModel = new DefaultListModel<>();
        taskList = new JList<>(taskListModel);
        taskList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);


        Leaf taskListScrollPane = new Leaf(new JScrollPane(taskList));
        taskListScrollPane.setSize(new Dimension(400, 150));
        taskListPanel.addComponent(taskListScrollPane, BorderLayout.CENTER);

        centerPanel.addComponent(notificationsPanel);
        centerPanel.addComponent(taskListPanel);

        return centerPanel;
    }

    private void initializeDayAndDate() {
        String day = LocalDate.now().getDayOfWeek().toString();
        String date = LocalDate.now().toString();

        Message message = new BasicMessage(day, date);
        message = new BirthdayMessageDecorator(message);

        String birthdayMessage = message.getMessage();

        if (birthdayMessage.contains(" Happy Birthday! ")) {
            setBirthdayMessage("Happy Birthday!");
        } else {
            setBirthdayMessage(null);
        }

        dayField.setText(day);
        dateField.setText(date);
    }


    public DefaultListModel<String> getTaskListModel() {
        return taskListModel;
    }

    public JList<String> getTaskList() {
        return taskList;
    }
    public void setController(TaskPlannerController controller) {
        this.controller = controller;
    }

    public JButton getAddTaskButton() {
        return addTaskButton;
    }

    public JButton getDeleteTaskButton() {
        return deleteTaskButton;
    }

    public JButton getEditTaskButton() {
        return editTaskButton;
    }

    public JTextArea getNotificationsArea() {
        return notificationsArea;
    }

    public void setBirthdayMessage(String message) {
        if (message != null) {
            birthdayMessageValue.setText(message);
        } else {
            birthdayMessageValue.setText("");
        }
    }



    private void startDayDateTimer() {
        Timer timer = new Timer(1000, e -> {
            String day = LocalDate.now().getDayOfWeek().toString();
            String date = LocalDate.now().toString();

            Message message = new BasicMessage(day, date);
            message = new BirthdayMessageDecorator(message);

            dayField.setText(day);
            dateField.setText(date);

            notificationsArea.setText(message.getMessage() + "\n\n" + getStaticNotificationsAsString());
        });
        timer.start();
    }

    private void updateStaticNotifications() {
        notificationsArea.setText(getStaticNotificationsAsString());
    }

    private String getStaticNotificationsAsString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Notifications:");
        for (String notification : staticNotifications) {
            builder.append("\n- ").append(notification);
        }
        return builder.toString();
    }

    @Override
    public void update() {
        SwingUtilities.invokeLater(() -> {
            taskListModel.clear();
            taskList.revalidate();
            taskList.repaint();
        });
    }
}
