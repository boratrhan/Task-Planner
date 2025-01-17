import controller.TaskPlannerController;
import model.DatabaseManager;
import model.TaskModel;
import view.TaskPlannerView;

public class Main {
    public static void main(String[] args) {
        DatabaseManager dbManager = new DatabaseManager();

        TaskModel model = new TaskModel(dbManager);
        TaskPlannerView view = new TaskPlannerView();

        new TaskPlannerController(model, view);



        }
}