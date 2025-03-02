package api;

import interfaces.TaskManager;
import tasks.Subtask;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class SubtaskHandler extends BaseHttpHandler {
    private final TaskManager taskManager;
    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();

    public SubtaskHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            String path = exchange.getRequestURI().getPath();
            String method = exchange.getRequestMethod();

            if (path.equals("/subtasks") && method.equals("GET")) {
                handleGetSubtasks(exchange);
            } else if (path.startsWith("/subtasks/") && method.equals("GET")) {
                handleGetSubtaskById(exchange);
            } else if (path.equals("/subtasks") && method.equals("POST")) {
                handlePostSubtask(exchange);
            } else if (path.startsWith("/subtasks/") && method.equals("DELETE")) {
                handleDeleteSubtask(exchange);
            } else {
                exchange.sendResponseHeaders(404, 0);
                exchange.close();
            }
        } catch (Exception e) {
            sendServerError(exchange);
        }
    }

    private void handleGetSubtasks(HttpExchange exchange) throws IOException {
        List<Subtask> subtasks = taskManager.getSubtask();
        String response = gson.toJson(subtasks);
        sendText(exchange, response, 200);
    }

    private void handleGetSubtaskById(HttpExchange exchange) throws IOException {
        int id = parseId(exchange);
        Subtask subtasks = taskManager.getSubtask(id);

        if (subtasks != null) {
            sendText(exchange, gson.toJson(subtasks), 200);
        } else {
            sendNotFound(exchange);
        }
    }

    private void handlePostSubtask(HttpExchange exchange) throws IOException {
        String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        Subtask subtasks = gson.fromJson(body, Subtask.class);

        try {
            if (subtasks.getId() == 0) {
                taskManager.add(subtasks);
            } else {
                taskManager.updateTask(subtasks);
            }
            sendText(exchange, "Подзадача успешно добавлена/обновлена", 201);
        } catch (IllegalArgumentException e) {
            sendConflict(exchange);
        }
    }

    private void handleDeleteSubtask(HttpExchange exchange) throws IOException {
        int id = parseId(exchange);
        taskManager.removeSubtask(id);
        sendText(exchange, "Подзадача удалена", 200);
    }

    private int parseId(HttpExchange exchange) {
        String path = exchange.getRequestURI().getPath();
        return Integer.parseInt(path.substring(path.lastIndexOf("/") + 1));
    }
}
