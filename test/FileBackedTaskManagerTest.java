import enums.TaskStatus;
import managers.FileBackedTaskManager;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager> {
    private File testFile;

    @BeforeEach
    void setUp() throws IOException {
        testFile = File.createTempFile("test", ".csv");
        manager = new FileBackedTaskManager(testFile);
    }

    @Test
    void testSaveAndLoadEmptyFile() {
        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(testFile);
        assertTrue(loadedManager.getTask().isEmpty(), "Список задач должен быть пустым");
        assertTrue(loadedManager.getEpic().isEmpty(), "Список эпиков должен быть пустым");
        assertTrue(loadedManager.getSubtask().isEmpty(), "Список подзадач должен быть пустым");
    }

    @Test
    void testSaveAndLoadMultipleTasks() {
        Task task1 = new Task("Task1", "Description1",
                TaskStatus.NEW, null, null);
        Task task2 = new Task("Task2", "Description2",
                TaskStatus.IN_PROGRESS, null, null);
        manager.add(task1);
        manager.add(task2);

        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(testFile);

        assertEquals(2, loadedManager.getTask().size(), "Неверное количество задач");
        assertTrue(loadedManager.getTask().contains(task1), "Task1 отсутствует после загрузки");
        assertTrue(loadedManager.getTask().contains(task2), "Task2 отсутствует после загрузки");
    }

    @Test
    void testSaveAndLoadMultipleTasksAndEpics() {
        Task task = new Task("Task1", "Description1", TaskStatus.NEW, null, null);
        Epic epic = new Epic("Epic1", "Description Epic1", TaskStatus.DONE);

        manager.add(epic);

        Subtask subtask = new Subtask("Subtask1", "Description Subtask1",
                TaskStatus.DONE, epic.getId(), null, null);

        manager.add(task);
        manager.add(subtask);

        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(testFile);

        assertEquals(1, loadedManager.getTask().size(), "Неверное количество задач");
        assertEquals(1, loadedManager.getEpic().size(), "Неверное количество эпиков");
        assertEquals(1, loadedManager.getSubtask().size(), "Неверное количество подзадач");

        assertTrue(loadedManager.getTask().contains(task), "TASK.Task отсутствует после загрузки");
        assertTrue(loadedManager.getEpic().contains(epic), "TASK.Epic отсутствует после загрузки");
        assertTrue(loadedManager.getSubtask().contains(subtask), "TASK.Subtask отсутствует после загрузки");
    }
}
