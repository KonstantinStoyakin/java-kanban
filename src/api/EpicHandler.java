package api;

import interfaces.TaskManager;
import tasks.Epic;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class EpicHandler extends BaseHttpHandler {
    private final TaskManager taskManager;
    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();

    public EpicHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            String path = exchange.getRequestURI().getPath();
            String method = exchange.getRequestMethod();

            if (path.equals("/epics") && method.equals("GET")) {
                handleGetEpics(exchange);
            } else if (path.startsWith("/epics/") && method.equals("GET")) {
                handleGetEpicById(exchange);
            } else if (path.equals("/epics") && method.equals("POST")) {
                handlePostEpic(exchange);
            } else if (path.startsWith("/epics/") && method.equals("DELETE")) {
                handleDeleteEpic(exchange);
            } else {
                exchange.sendResponseHeaders(404, 0);
                exchange.close();
            }
        } catch (Exception e) {
            sendServerError(exchange);
        }
    }

    private void handleGetEpics(HttpExchange exchange) throws IOException {
        List<Epic> epics = taskManager.getEpic();
        String response = gson.toJson(epics);
        sendText(exchange, response, 200);
    }

    private void handleGetEpicById(HttpExchange exchange) throws IOException {
        int id = parseId(exchange);
        Epic epic = taskManager.getEpic(id);

        if (epic != null) {
            sendText(exchange, gson.toJson(epic), 200);
        } else {
            sendNotFound(exchange);
        }
    }

    private void handlePostEpic(HttpExchange exchange) throws IOException {
        String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        Epic epic = gson.fromJson(body, Epic.class);

        try {
            if (epic.getId() == 0) {
                taskManager.add(epic);
            } else {
                taskManager.updateTask(epic);
            }
            sendText(exchange, "Эпик успешно добавлен/обновлен", 201);
        } catch (IllegalArgumentException e) {
            sendConflict(exchange);
        }
    }

    private void handleDeleteEpic(HttpExchange exchange) throws IOException {
        int id = parseId(exchange);
        Epic epic = taskManager.getEpic(id);
        if (epic == null) {
            sendNotFound(exchange);
            return;
        }
        taskManager.removeEpic(id);
        sendText(exchange, "Эпик удален", 200);
    }

    private int parseId(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String[] parts = path.split("/");
        if (parts.length < 3) {
            throw new IOException("Некорректный формат URL");
        }
        return Integer.parseInt(parts[2]);
    }
}

