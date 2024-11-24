public class Main {

    public static void main(String[] args) {
        TaskManager manager = new TaskManager();

        Task firstTask = new Task("Позавтракать", "Приготовить омлет и сварить кофе", TaskStatus.NEW);
        Task secondTask = new Task("Пообедать", "Сварить суп и налить компот", TaskStatus.NEW);

        manager.add(firstTask);
        manager.add(secondTask);

        Epic firstEpic = new Epic("Получить высшее образование",
                "Диплом бакалавра и диплом магистра",
                TaskStatus.IN_PROGRESS);

        manager.add(firstEpic);

        Subtask firstSubtask = new Subtask("Поступить в университет",
                "Экономический факультет",
                TaskStatus.DONE, firstEpic.getId());
        Subtask secondSubtask = new Subtask("Получить диплом бакалавра",
                "Защитить диплом",
                TaskStatus.IN_PROGRESS, firstEpic.getId());

        manager.add(firstSubtask);
        manager.add(secondSubtask);

        Epic secondEpic = new Epic("Устроиться на работу", "Экономист или бухгалтер", TaskStatus.NEW);

        manager.add(secondEpic);

        Subtask thirdSubtask = new Subtask("Пройти собеседование",
                "Знание бухгалтерского учета и умение составлять бизнес план",
                TaskStatus.NEW, secondEpic.getId());

        manager.add(thirdSubtask);

        for (Task task : manager.tasks.values()) {
            System.out.println(task);
        }

        for (Epic epic : manager.epics.values()) {
            System.out.println(epic);
        }

        for (Subtask subtask : manager.subtasks.values()) {
            System.out.println(subtask);
        }

        firstTask.setStatus(TaskStatus.IN_PROGRESS);
        manager.updateTask(firstTask);

        secondTask.setStatus(TaskStatus.DONE);
        manager.updateTask(secondTask);

        secondSubtask.setStatus(TaskStatus.DONE);
        manager.updateSubtask(secondSubtask);

        thirdSubtask.setStatus(TaskStatus.DONE);
        manager.updateSubtask(thirdSubtask);

        System.out.println("После изменения статусов: ");

        for (Task task : manager.tasks.values()) {
            System.out.println(task);
        }

        for (Epic epic : manager.epics.values()) {
            System.out.println(epic);
        }

        for (Subtask subtask : manager.subtasks.values()) {
            System.out.println(subtask);
        }

        manager.removeTask(2);
        manager.removeEpic(3);

        System.out.println("После удаления задачи и эпика: ");

        for (Task task : manager.tasks.values()) {
            System.out.println(task);
        }

        for (Epic epic : manager.epics.values()) {
            System.out.println(epic);
        }

        for (Subtask subtask : manager.subtasks.values()) {
            System.out.println(subtask);
        }
    }
}
