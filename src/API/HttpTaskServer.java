package API;

import INTERFACE.TaskManager;
import MANAGERS.Managers;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    private static final int PORT = 8080;
    private final HttpServer server;

    public HttpTaskServer(TaskManager taskManager) throws IOException {
        server = HttpServer.create(new InetSocketAddress(PORT), 0);

        server.createContext("/tasks", new TaskHandler(taskManager));
        server.createContext("/subtasks", new SubtaskHandler(taskManager));
        server.createContext("/epics", new EpicHandler(taskManager));
        server.createContext("/history", new HistoryHandler(taskManager));
        server.createContext("/prioritized", new PrioritizedHandler(taskManager));

        server.setExecutor(null);
    }

    public void start() {
        server.start();
        System.out.println("Сервер запущен на порту " + PORT);
    }

    public void stop() {
        server.stop(0);
        System.out.println("Сервер остановлен.");
    }

    public static void main(String[] args) throws IOException {
        TaskManager taskManager = Managers.getDefault();
        HttpTaskServer server = new HttpTaskServer(taskManager);
        server.start();
    }
}
