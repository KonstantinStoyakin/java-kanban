import ENUM.TaskStatus;
import TASK.Task;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    @Test
    void testEqualityById() {
        Task task1 = new Task("Позавтракать", "Приготовить омлет и сварить кофе",
                TaskStatus.NEW, null, null);
        task1.setId(1);

        Task task2 = new Task("Пообедать", "Сварить суп и налить компот",
                TaskStatus.DONE, null, null);
        task2.setId(1);

        assertNotEquals(task1, task2, "Задачи с одинаковым ID должны быть равны друг другу.");
    }

}