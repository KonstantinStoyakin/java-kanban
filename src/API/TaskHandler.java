package API;

import INTERFACE.TaskManager;
import TASK.Task;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class TaskHandler extends BaseHttpHandler {
    private final TaskManager taskManager;
    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();

    public TaskHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            String path = exchange.getRequestURI().getPath();
            String method = exchange.getRequestMethod();

            if (path.equals("/tasks") && method.equals("GET")) {
                handleGetTasks(exchange);
            } else if (path.startsWith("/tasks/") && method.equals("GET")) {
                handleGetTaskById(exchange);
            } else if (path.equals("/tasks") && method.equals("POST")) {
                handlePostTask(exchange);
            } else if (path.startsWith("/tasks/") && method.equals("DELETE")) {
                handleDeleteTask(exchange);
            } else {
                exchange.sendResponseHeaders(404, 0);
                exchange.close();
            }
        } catch (Exception e) {
            sendServerError(exchange);
        }
    }

    private void handleGetTasks(HttpExchange exchange) throws IOException {
        List<Task> tasks = taskManager.getTask();
        String response = gson.toJson(tasks);
        sendText(exchange, response, 200);
    }

    private void handleGetTaskById(HttpExchange exchange) throws IOException {
        int id = parseId(exchange);
        Task task = taskManager.getTask(id);

        if (task != null) {
            sendText(exchange, gson.toJson(task), 200);
        } else {
            sendNotFound(exchange);
        }
    }

    private void handlePostTask(HttpExchange exchange) throws IOException {
        String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        Task task = gson.fromJson(body, Task.class);

        try {
            if (task.getId() == 0) {
                taskManager.add(task);
            } else {
                taskManager.updateTask(task);
            }
            sendText(exchange, "Задача успешно добавлена/обновлена", 201);
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка при добавлении: " + e.getMessage());
            sendConflict(exchange);
        }
    }

    private void handleDeleteTask(HttpExchange exchange) throws IOException {
        int id = parseId(exchange);
        taskManager.removeTask(id);
        sendText(exchange, "Задача удалена", 200);
    }

    private int parseId(HttpExchange exchange) {
        String path = exchange.getRequestURI().getPath();
        return Integer.parseInt(path.substring(path.lastIndexOf("/") + 1));
    }
}
