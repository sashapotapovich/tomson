package example.tomson;

import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import io.micronaut.context.ApplicationContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;

class Application {

    private static final Logger log = LoggerFactory.getLogger(Application.class);
    private static HttpServer httpServer;

    public static void main(String[] args) throws IOException {
        httpServer = HttpServer.create(new InetSocketAddress(8080), 0);
        HttpContext context = httpServer.createContext("/hello");
        context.setHandler(Application::handleRequest);
        httpServer.start();
    }

    private static void handleRequest(HttpExchange exchange) throws IOException {
        String response = "Hello there standart!";
        exchange.sendResponseHeaders(200, response.getBytes().length);//response code and length
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}