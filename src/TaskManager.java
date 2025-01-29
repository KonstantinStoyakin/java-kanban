import java.util.List;

public interface TaskManager {

    void add(Task task);

    void add(Epic epic);

    void add(Subtask subtask);

    void updateTask(Task task);

    void updateEpic(Epic epic);

    void updateSubtask(Subtask subtask);

    Task getTask(int id);

    Epic getEpic(int id);

    Subtask getSubtask(int id);

    void removeTask(int id);

    void removeEpic(int id);

    void removeSubtask(int id);

    void deleteAllTasks(Task task);

    void deleteAllEpics(Epic epic);

    void deleteAllSubtasks(Subtask subtask);

    List<Task> getTask();

    List<Epic> getEpic();

    List<Subtask> getSubtask();

    List<Task> getHistory();

    List<Subtask> getEpicSubtasks(int epicId);
}

