import enums.TaskStatus;
import interfaces.TaskManager;
import managers.InMemoryTaskManager;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

public class Main {
    public static void main(String[] args) {
        InMemoryTaskManager manager = new InMemoryTaskManager();

        Task firstTask = new Task("Позавтракать", "Приготовить омлет и сварить кофе", TaskStatus.NEW,
                null, null);
        Task secondTask = new Task("Пообедать", "Сварить суп и налить компот", TaskStatus.NEW,
                null, null);

        manager.add(firstTask);
        manager.add(secondTask);

        Epic firstEpic = new Epic("Получить высшее образование",
                "Диплом бакалавра и диплом магистра",
                TaskStatus.IN_PROGRESS);

        manager.add(firstEpic);

        Subtask firstSubtask = new Subtask("Поступить в университет",
                "Экономический факультет",
                TaskStatus.DONE, firstEpic.getId(), null, null);
        Subtask secondSubtask = new Subtask("Получить диплом бакалавра",
                "Защитить диплом",
                TaskStatus.IN_PROGRESS, firstEpic.getId(), null, null);

        manager.add(firstSubtask);
        manager.add(secondSubtask);

        System.out.println("После добавления задач:");
        printAllTasks(manager);

        manager.getTask(firstTask.getId());
        manager.getTask(secondTask.getId());
        manager.getEpic(firstEpic.getId());
        manager.getSubtask(firstSubtask.getId());
        manager.getSubtask(secondSubtask.getId());

        System.out.println("После просмотров задач:");
        printAllTasks(manager);

    }

    private static void printAllTasks(TaskManager manager) {
        System.out.println("Задачи:");
        for (Task task : manager.getTask()) { // Вывод всех задач
            System.out.println(task);
        }

        System.out.println("Эпики:");
        for (Epic epic : manager.getEpic()) { // Вывод всех эпиков
            System.out.println(epic);
            for (Subtask subtask : manager.getEpicSubtasks(epic.getId())) { // Вывод подзадач эпика
                System.out.println("--> " + subtask);
            }
        }

        System.out.println("Подзадачи:");
        for (Subtask subtask : manager.getSubtask()) { // Вывод всех подзадач
            System.out.println(subtask);
        }

        System.out.println("История:");
        for (Task task : manager.getHistory()) { // Вывод истории просмотров
            System.out.println(task);
        }
    }

}
