package mobilemanagement;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

public final class MobileManagementServer {
    private static final int DEFAULT_PORT = 9090;
    private static final MobileRepository REPOSITORY = new MobileRepository();

    private MobileManagementServer() {
    }

    public static void main(String[] args) throws IOException {
        int port = args.length > 0 ? parseInt(args[0], DEFAULT_PORT) : DEFAULT_PORT;
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/", MobileManagementServer::handleHome);
        server.createContext("/add", MobileManagementServer::handleAdd);
        server.setExecutor(null);
        server.start();
        System.out.println("Mobile Management System running at http://localhost:" + port + "/");
        awaitShutdown();
    }

    private static void awaitShutdown() {
        try {
            new CountDownLatch(1).await();
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
        }
    }

    private static void handleHome(HttpExchange exchange) throws IOException {
        if (!"GET".equalsIgnoreCase(exchange.getRequestMethod())) {
            redirect(exchange, "/");
            return;
        }
        String notice = queryParams(exchange.getRequestURI().getRawQuery()).get("notice");
        render(exchange, HtmlRenderer.page(REPOSITORY.findAllNewestFirst(), REPOSITORY.activeCount(), notice));
    }

    private static void handleAdd(HttpExchange exchange) throws IOException {
        if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
            redirect(exchange, "/");
            return;
        }

        String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        Map<String, String> form = queryParams(body);
        Mobile mobile = new Mobile(
                form.get("brand"),
                form.get("model"),
                form.get("os"),
                parseInt(form.get("storage"), 128),
                parseDouble(form.get("price"), 0.0)
        );
        REPOSITORY.add(mobile);
        redirect(exchange, "/?notice=" + encode(mobile.getDisplayName() + " object created successfully"));
    }

    private static int parseInt(String value, int fallback) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException exception) {
            return fallback;
        }
    }

    private static double parseDouble(String value, double fallback) {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException exception) {
            return fallback;
        }
    }

    private static Map<String, String> queryParams(String raw) {
        Map<String, String> params = new HashMap<>();
        if (raw == null || raw.isBlank()) {
            return params;
        }
        for (String pair : raw.split("&")) {
            String[] keyValue = pair.split("=", 2);
            String key = decode(keyValue[0]);
            String value = keyValue.length > 1 ? decode(keyValue[1]) : "";
            params.put(key, value);
        }
        return params;
    }

    private static String decode(String value) {
        return URLDecoder.decode(value, StandardCharsets.UTF_8);
    }

    private static String encode(String value) {
        return java.net.URLEncoder.encode(value, StandardCharsets.UTF_8);
    }

    private static void redirect(HttpExchange exchange, String location) throws IOException {
        exchange.getResponseHeaders().set("Location", location);
        exchange.sendResponseHeaders(303, -1);
        exchange.close();
    }

    private static void render(HttpExchange exchange, String html) throws IOException {
        byte[] bytes = html.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
        exchange.sendResponseHeaders(200, bytes.length);
        try (OutputStream output = exchange.getResponseBody()) {
            output.write(bytes);
        }
    }
}
