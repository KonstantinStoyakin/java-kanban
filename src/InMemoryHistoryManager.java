import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private static final int IN_ARRAY_SIZE = 10;
    private final List<Task> arrayHistory = new ArrayList<>();

    @Override
    public void add(Task task) {
        arrayHistory.remove(task);
        if (arrayHistory.size() == IN_ARRAY_SIZE) {
            arrayHistory.removeFirst();
        }
        arrayHistory.add(task);
    }

    @Override
    public List<Task> getHistory() {
        return new ArrayList<>(arrayHistory);
    }
}