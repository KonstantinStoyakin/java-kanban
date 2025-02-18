import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EpicTest {

    @Test
    void epicCannotBeItsOwnSubtask() {
        Epic epic = new Epic("Получить высшее образование", "Диплом бакалавра и диплом магистра",
                TaskStatus.IN_PROGRESS);
        epic.setId(1);

        assertThrows(IllegalArgumentException.class, () -> {
            epic.addSubtask(1);
        }, "Эпик не может быть своей подзадачей.");
    }
}