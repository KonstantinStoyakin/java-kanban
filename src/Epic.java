import java.util.ArrayList;

public class Epic extends Task {
    private final ArrayList<Integer> subtasksIds;

    public Epic(String name, String description, TaskStatus status) {
        super(name, description, status);
        this.subtasksIds = new ArrayList<>();
    }

    public ArrayList<Integer> getSubtasksIds() {
        return subtasksIds;
    }

    public void removeSubtask(int subtaskId) {
        subtasksIds.remove((Integer) subtaskId);
    }

    public void addSubtask(int subtaskId) {
        if (subtaskId == this.getId()) {
            throw new IllegalArgumentException("Эпик не может быть своей подзадачей.");
        }
        subtasksIds.add(subtaskId);
    }


    @Override
    public String toString() {
        return "Epic{" +
                "Id=" + super.getId() +
                ", name='" + super.getName() + '\'' +
                ", description='" + super.getDescription() + '\'' +
                ", status=" + super.getStatus() +
                ", subtasksIds=" + subtasksIds +
                '}';
    }
}
