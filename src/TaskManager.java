import java.util.HashMap;

public class TaskManager {
    HashMap<Integer, Task> tasks = new HashMap<>();
    HashMap<Integer, Epic> epics = new HashMap<>();
    HashMap<Integer, Subtask> subtasks = new HashMap<>();
    public int nextId = 1;

    private int generateId() {
        return nextId++;
    }

    public void add(Task task) {
        task.setId(generateId());
        tasks.put(task.getId(), task);
    }

    public void add(Epic epic) {
        epic.setId(generateId());
        epics.put(epic.getId(), epic);
    }

    public void add(Subtask subtask) {
        subtask.setId(generateId());
        subtasks.put(subtask.getId(), subtask);

        Epic epic = epics.get(subtask.getEpicId());

        if (epic != null) {
            epic.getSubtasksIds().add(subtask.getId());
            updateEpicStatus(epic);
        }
    }

    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
    }

    public void updateEpic(Epic epic) {
        epics.put(epic.getId(), epic);
    }

    public void updateSubtask(Subtask subtask) {
        subtasks.put(subtask.getId(), subtask);

        Epic epic = epics.get(subtask.getEpicId());
        if (epic!= null) {
            updateEpicStatus(epic);
        }
    }

    public Task getTask(int id) {
        return tasks.get(id);
    }

    public Epic getEpic (int id) {
        return epics.get(id);
    }

    public Subtask getSubtask (int id) {
        return subtasks.get(id);
    }

    public void removeTask(int id) {
        tasks.remove(id);
    }

    public void removeEpic(int id) {
        Epic epic = epics.remove(id);

        if (epic != null) {
            for (int subtaskId : epic.getSubtasksIds()) {
                subtasks.remove(subtaskId);
            }
        }
    }

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

    public void deleteAllTasks(Task task) {
        tasks.clear();
    }

    public void deleteAllEpics(Epic epic) {
        epics.clear();
        subtasks.clear();
    }

    public void deleteAllSubtasks(Subtask subtask) {
        subtasks.clear();

        for (Epic epic : epics.values()) {
            epic.getSubtasksIds().clear();
            updateEpicStatus(epic);
        }
    }

    private void updateEpicStatus (Epic epic) {
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
    }
}

