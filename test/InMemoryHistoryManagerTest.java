import enums.TaskStatus;
import interfaces.HistoryManager;
import managers.InMemoryHistoryManager;
import managers.Managers;
import tasks.Task;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    @Test
    void testHistoryManager() {
        HistoryManager historyManager = Managers.getDefaultHistory();
        Task task = new Task("История задач", "Описание",
                TaskStatus.NEW, null, null);
        task.setId(1);

        historyManager.add(task);
        List<Task> history = historyManager.getHistory();

        assertEquals(1, history.size(), "История должна содержать одну задачу.");
        assertEquals(task, history.get(0), "Задача в истории должна совпадать.");
    }

    @Test
    void testLinkedListAddAndRetrieve() {
        InMemoryHistoryManager historyManager = new InMemoryHistoryManager();
        Task task1 = new Task("История задач", "Описание",
                TaskStatus.NEW, null, null);
        Task task2 = new Task("Test Task2", "Test Description2",
                TaskStatus.NEW, null, null);

        task1.setId(1);
        task2.setId(2);

        historyManager.add(task1);
        historyManager.add(task2);

        List<Task> history = historyManager.getHistory();
        assertEquals(2, history.size());
        assertEquals(task1, history.get(0));
        assertEquals(task2, history.get(1));
    }

    @Test
    void testLinkedListRemoveFromMiddle() {
        InMemoryHistoryManager historyManager = new InMemoryHistoryManager();
        Task task1 = new Task("Test Task1", "Test Description1",
                TaskStatus.NEW, null, null);
        Task task2 = new Task("Test Task2", "Test Description2",
                TaskStatus.NEW, null, null);
        Task task3 = new Task("Test Task3", "Test Description3",
                TaskStatus.NEW, null, null);

        task1.setId(1);
        task2.setId(2);
        task3.setId(3);

        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);

        historyManager.remove(2); // Удаляем task2

        List<Task> history = historyManager.getHistory();
        assertEquals(2, history.size());
        assertEquals(task1, history.get(0));
        assertEquals(task3, history.get(1));
    }

    @Test
    void testLinkedListRemoveFirst() {
        InMemoryHistoryManager historyManager = new InMemoryHistoryManager();
        Task task1 = new Task("Test Task1", "Test Description1",
                TaskStatus.NEW, null, null);
        Task task2 = new Task("Test Task2", "Test Description2",
                TaskStatus.NEW, null, null);

        task1.setId(1);
        task2.setId(2);

        historyManager.add(task1);
        historyManager.add(task2);

        historyManager.remove(1); // Удаляем task1

        List<Task> history = historyManager.getHistory();
        assertEquals(1, history.size());
        assertEquals(task2, history.get(0));
    }
}
