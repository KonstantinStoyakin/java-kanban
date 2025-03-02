package API;

import INTERFACE.TaskManager;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

public class HistoryHandler extends BaseHttpHandler {
    private final TaskManager taskManager;
    private final Gson gson = new Gson();

    public HistoryHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("GET".equals(exchange.getRequestMethod())) {
            String response = gson.toJson(taskManager.getHistory());
            sendText(exchange, response, 200);
        } else {
            exchange.sendResponseHeaders(405, 0);
            exchange.close();
        }
    }
}
