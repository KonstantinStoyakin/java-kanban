import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;

public abstract class TaskManagerTest<T extends TaskManager> {

    protected T manager;

    @Test
    void taskAdditionTest() {
        Task task = new Task("Test Task", "Test Description",
                TaskStatus.NEW, null, null);
        manager.add(task);
        Task retrievedTask = manager.getTask(task.getId());
        assertNotNull(retrievedTask, "Задача должна быть добавлена.");
        assertEquals(task, retrievedTask, "Добавленная и извлечённая задачи должны совпадать.");
    }

    @Test
    void taskDeletionTest() {
        Task task = new Task("Test Task", "Test Description",
                TaskStatus.NEW, null, null);
        manager.add(task);
        manager.removeTask(task.getId());
        Task retrievedTask = manager.getTask(task.getId());
        assertNull(retrievedTask, "Задача должна быть удалена.");
    }

    @Test
    void testEpicStatus() {
        Epic epic = new Epic("Epic Task", "Epic Description", TaskStatus.NEW);
        manager.add(epic);

        Subtask subtask1 = new Subtask("Subtask 1", "Subtask Description", TaskStatus.NEW,
                epic.getId(), null, null);
        Subtask subtask2 = new Subtask("Subtask 2", "Subtask Description", TaskStatus.NEW,
                epic.getId(), null, null);
        manager.add(subtask1);
        manager.add(subtask2);

        Epic loadedEpic = manager.getEpic(epic.getId());
        assertEquals(TaskStatus.NEW, loadedEpic.getStatus(), "Статус эпика должен быть NEW.");
    }

    @Test
    void testEpicStatusDONE() {
        Epic epic = new Epic("Epic Task", "Epic Description", TaskStatus.NEW);
        manager.add(epic);

        Subtask subtask1 = new Subtask("Subtask 1", "Subtask Description", TaskStatus.DONE,
                epic.getId(), null, null);
        Subtask subtask2 = new Subtask("Subtask 2", "Subtask Description", TaskStatus.DONE,
                epic.getId(), null, null);
        manager.add(subtask1);
        manager.add(subtask2);

        Epic loadedEpic = manager.getEpic(epic.getId());
        assertEquals(TaskStatus.DONE, loadedEpic.getStatus(), "Статус эпика должен быть DONE.");
    }

    @Test
    void testEpicStatusMixed() {
        Epic epic = new Epic("Epic Task", "Epic Description", TaskStatus.NEW);
        manager.add(epic);

        Subtask subtask1 = new Subtask("Subtask 1", "Subtask Description",
                TaskStatus.NEW, epic.getId(), null, null);
        Subtask subtask2 = new Subtask("Subtask 2", "Subtask Description",
                TaskStatus.DONE, epic.getId(), null, null);
        manager.add(subtask1);
        manager.add(subtask2);

        Epic loadedEpic = manager.getEpic(epic.getId());
        assertEquals(TaskStatus.IN_PROGRESS, loadedEpic.getStatus(), "Статус эпика должен быть IN_PROGRESS.");
    }

    @Test
    void testTaskTimeIntervalsOverlap() {
        Task task1 = new Task("Task 1", "Task Description", TaskStatus.NEW,
                Duration.ofMinutes(10),
                LocalDateTime.of(2025, Month.FEBRUARY, 17, 10, 0));

        Task task2 = new Task("Task 2", "Task Description", TaskStatus.NEW,
                Duration.ofMinutes(15),
                LocalDateTime.of(2025, Month.FEBRUARY, 17, 10, 5));

        assertTrue(task1.getStartTime().isBefore(task2.getEndTime()) &&
                        task1.getEndTime().isAfter(task2.getStartTime()),
                "Временные интервалы задач должны пересекаться.");
    }
}


