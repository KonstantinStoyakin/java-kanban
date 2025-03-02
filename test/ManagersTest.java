import interfaces.HistoryManager;
import interfaces.TaskManager;
import managers.Managers;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ManagersTest {

    @Test
    void testManagersInitialization() {
        TaskManager taskManager = Managers.getDefault();
        HistoryManager historyManager = Managers.getDefaultHistory();

        assertNotNull(taskManager, "INTERFACE.TaskManager не должен быть null.");
        assertNotNull(historyManager, "INTERFACE.HistoryManager не должен быть null.");
    }
}
