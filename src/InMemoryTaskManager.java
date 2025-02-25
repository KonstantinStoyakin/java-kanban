import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.Comparator;

public class InMemoryTaskManager implements TaskManager {

    protected final Map<Integer, Task> tasks = new HashMap<>();
    protected final Map<Integer, Epic> epics = new HashMap<>();
    protected final Map<Integer, Subtask> subtasks = new HashMap<>();
    private final HistoryManager historyManager = Managers.getDefaultHistory();
    private final TreeSet<Task> prioritizedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime,
            Comparator.nullsLast(Comparator.naturalOrder())));
    private int nextId = 1;

    private int generateId() {
        return nextId++;
    }

    @Override
    public void add(Task task) {
        if (isOverlapping(task)) {
            throw new IllegalArgumentException("Задача пересекается с уже существующей!");
        }
        task.setId(generateId());
        tasks.put(task.getId(), task);
        prioritizedTasks.add(task);
    }

    @Override
    public void add(Epic epic) {
        epic.setId(generateId());
        epics.put(epic.getId(), epic);
    }

    @Override
    public void add(Subtask subtask) {
        if (subtask.getStartTime() != null && isOverlapping(subtask)) {
            throw new IllegalArgumentException("Ошибка: пересечение задач по времени!");
        }
        subtask.setId(generateId());
        subtasks.put(subtask.getId(), subtask);

        Epic epic = epics.get(subtask.getEpicId());
        if (epic != null) {
            epic.getSubtasksIds().add(subtask.getId());
            updateEpicStatus(epic);
        }

        prioritizedTasks.add(subtask);
    }

    @Override
    public void updateTask(Task task) {
        if (task.getStartTime() != null && isOverlapping(task)) {
            throw new IllegalArgumentException("Ошибка: пересечение задач по времени!");
        }
        tasks.put(task.getId(), task);
        prioritizedTasks.add(task);
    }

    @Override
    public void updateEpic(Epic epic) {
        epics.put(epic.getId(), epic);
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        if (subtask.getStartTime() != null && isOverlapping(subtask)) {
            throw new IllegalArgumentException("Ошибка: пересечение задач по времени!");
        }
        subtasks.put(subtask.getId(), subtask);

        Epic epic = epics.get(subtask.getEpicId());
        if (epic != null) {
            updateEpicStatus(epic);
        }

        prioritizedTasks.add(subtask);
    }

    @Override
    public Task getTask(int id) {
        Task task = tasks.get(id);
        if (task != null) {
            historyManager.add(task);
        }
        return task;
    }

    @Override
    public Epic getEpic(int id) {
        Epic epic = epics.get(id);
        if (epic != null) {
            historyManager.add(epic);
        }
        return epic;
    }

    @Override
    public Subtask getSubtask(int id) {
        Subtask subtask = subtasks.get(id);
        if (subtask != null) {
            historyManager.add(subtask);
        }
        return subtask;
    }

    @Override
    public void removeTask(int id) {
        Task task = tasks.remove(id);
        if (task != null) {
            prioritizedTasks.remove(task);
        }
    }

    @Override
    public void removeEpic(int id) {
        Epic epic = epics.remove(id);

        if (epic != null) {
            for (int subtaskId : epic.getSubtasksIds()) {
                subtasks.remove(subtaskId);
            }
        }
    }

    @Override
    public void removeSubtask(int id) {
        Subtask subtask = subtasks.remove(id);

        if (subtask != null) {
            Epic epic = epics.get(subtask.getEpicId());
            if (epic != null) {
                epic.removeSubtask(id);
                updateEpicStatus(epic);
            }
        }
    }

    @Override
    public void deleteAllTasks(Task task) {
        tasks.clear();
    }

    public void deleteAllEpics(Epic epic) {
        epics.clear();
        subtasks.clear();
    }

    @Override
    public void deleteAllSubtasks(Subtask subtask) {
        subtasks.clear();

        for (Epic epic : epics.values()) {
            epic.getSubtasksIds().clear();
            updateEpicStatus(epic);
        }
    }

    private void updateEpicStatus(Epic epic) {
        boolean allTaskNew = true;
        boolean allTaskDone = true;

        for (int subtaskId : epic.getSubtasksIds()) {
            Subtask subtask = subtasks.get(subtaskId);
            if (subtask != null) {
                if (subtask.getStatus() != TaskStatus.NEW) {
                    allTaskNew = false;
                }
                if (subtask.getStatus() != TaskStatus.DONE) {
                    allTaskDone = false;
                }
            }
        }

        if (allTaskNew) {
            epic.setStatus(TaskStatus.NEW);
        } else if (allTaskDone) {
            epic.setStatus(TaskStatus.DONE);
        } else {
            epic.setStatus(TaskStatus.IN_PROGRESS);
        }

        updateEpicTime(epic);
    }

    @Override
    public List<Task> getTask() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<Epic> getEpic() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public List<Subtask> getSubtask() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public List<Subtask> getEpicSubtasks(int epicId) {
        Epic epic = epics.get(epicId);
        if (epic == null) {
            return new ArrayList<>();
        }

        List<Subtask> subtasksList = new ArrayList<>();
        for (int subtaskId : epic.getSubtasksIds()) {
            Subtask subtask = subtasks.get(subtaskId);
            if (subtask != null) {
                subtasksList.add(subtask);
            }
        }
        return subtasksList;
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTasks);
    }

    private void updateEpicTime(Epic epic) {
        List<Subtask> subtasksList = getEpicSubtasks(epic.getId());
        epic.setStartTime(epic.calculateStartTime(subtasksList));
        epic.setDuration(epic.calculateDuration(subtasksList));
        epic.setEndTime(epic.calculateEndTime(subtasksList));
    }

    @Override
    public boolean isOverlapping(Task newTask) {
        return prioritizedTasks.stream().anyMatch(existingTask ->
                existingTask.getStartTime() != null && newTask.getStartTime() != null &&
                        !newTask.getEndTime().isBefore(existingTask.getStartTime()) &&
                        !newTask.getStartTime().isAfter(existingTask.getEndTime())
        );
    }
}
