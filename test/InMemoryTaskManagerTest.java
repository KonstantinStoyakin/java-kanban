import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {

    @Test
    void taskAdditionTest() {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();
        Task task = new Task("Test Task", "Test Description", TaskStatus.NEW);

        taskManager.add(task);

        Task retrievedTask = taskManager.getTask(task.getId());
        assertNotNull(retrievedTask, "Задача должна быть добавлена.");
        assertEquals(task, retrievedTask, "Добавленная и извлечённая задачи должны совпадать.");
    }

    @Test
    void epicStatusUpdateTest() {
        InMemoryTaskManager manager = new InMemoryTaskManager();
        Epic epic = new Epic("Получить высшее образование", "Диплом бакалавра и диплом магистра", TaskStatus.IN_PROGRESS);
        manager.add(epic);

        Subtask subtask1 = new Subtask("Поступить в университет", "Экономический факультет", TaskStatus.DONE, epic.getId());
        Subtask subtask2 = new Subtask("Получить диплом бакалавра", "Защитить диплом", TaskStatus.IN_PROGRESS, epic.getId());

        manager.add(subtask1);
        manager.add(subtask2);

        Epic updatedEpic = manager.getEpic(epic.getId());
        assertEquals(TaskStatus.IN_PROGRESS, updatedEpic.getStatus(), "Статус эпика должен обновляться.");
    }
}
