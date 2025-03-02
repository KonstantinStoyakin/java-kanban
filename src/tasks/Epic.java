package tasks;

import enums.TaskStatus;
import enums.TaskType;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private final List<Integer> subtasksIds;

    public Epic(String name, String description, TaskStatus status) {
        super(name, description, status, Duration.ZERO, null);
        this.subtasksIds = new ArrayList<>();
    }

    public List<Integer> getSubtasksIds() {
        return subtasksIds;
    }

    public TaskType getType() {
        return TaskType.EPIC;
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

    public LocalDateTime calculateStartTime(List<Subtask> subtasks) {
        return  subtasks.stream()
                .filter(subtask -> subtasksIds.contains(subtask.getId()))
                .map(Subtask::getStartTime)
                .filter(time -> time != null)
                .min(LocalDateTime::compareTo)
                .orElse(null);
    }

    public Duration calculateDuration(List<Subtask> subtasks) {
        return subtasks.stream()
                .filter(subtask -> subtasksIds.contains(subtask.getId()))
                .map(Subtask::getDuration)
                .filter(duration -> duration != null)
                .reduce(Duration::plus)
                .orElse(Duration.ZERO);
    }

    private LocalDateTime endTime;

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public LocalDateTime calculateEndTime(List<Subtask> subtasks) {
        return subtasks.stream()
                .filter(subtask -> subtasksIds.contains(subtask.getId()))
                .map(Subtask::getEndTime)
                .filter(time -> time != null)
                .max(LocalDateTime::compareTo)
                .orElse(null);
    }

    @Override
    public String toString() {
        return "TASK.Epic{" +
                "Id=" + super.getId() +
                ", name='" + super.getName() + '\'' +
                ", description='" + super.getDescription() + '\'' +
                ", status=" + super.getStatus() +
                ", subtasksIds=" + subtasksIds +
                '}';
    }
}
