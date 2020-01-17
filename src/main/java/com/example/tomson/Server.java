package com.example.tomson;

import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.test.di.annotations.Component;
import org.test.di.annotations.PostConstruct;
import org.test.di.utils.ClassUtil;

@Component
public class Server {

    private final Logger log = LoggerFactory.getLogger(Application.class);
    private HttpServer httpServer;

    @SneakyThrows
    @PostConstruct
    public void init() {
        int port = resolvePort();
        log.info("Starting Server on the port - {}", port);
        httpServer = HttpServer.create(new InetSocketAddress(port), 0);
        HttpContext context = httpServer.createContext("/hello");
        HttpContext test = httpServer.createContext("/");
        context.setHandler(this::handleRequest);
        test.setHandler(this::handleTestRequest);
        httpServer.start();
    }

    @SneakyThrows
    private int resolvePort() {
        URL url = ClassUtil.getClassLoader().getResource("application.properties");
        Path path = Paths.get(url.toURI());
        Optional<Integer> port = Files.readAllLines(path, StandardCharsets.UTF_8)
                                       .stream().filter(s -> s.startsWith("server.port"))
                                       .map(s -> s.replace(" ", "")
                                                  .substring("server.port=".length()))
                                       .map(Integer::valueOf)
                                       .findFirst();
        return port.orElse(8080);
    }

    private void handleRequest(HttpExchange exchange) throws IOException {
        String response = "Hello there standart!";
        exchange.sendResponseHeaders(200, response.getBytes().length);//response code and length
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    private void handleTestRequest(HttpExchange exchange) throws IOException {
        log.info(exchange.getProtocol());
        exchange.getHttpContext().getAttributes()
                .forEach((key, value) -> log.info("Http Attributes: key - " + key +
                                                                    ", value - " + value));
        log.info(exchange.getHttpContext().getPath());
        log.info(exchange.getRequestMethod());
        BufferedReader body = new BufferedReader(new InputStreamReader(exchange.getRequestBody()));
        while (body.ready()){
            String line = body.readLine();
            log.info(line);
        }
        exchange.getRequestHeaders()
                .forEach((key, value)
                                 -> log.info("Headers: key - " + key + "values - " 
                                                     + value.stream()
                                                            .reduce(" - ", String::concat)));
        exchange.sendResponseHeaders(200, 0);
        OutputStream responseBody = exchange.getResponseBody();
        exchange.setStreams(null, responseBody);
        responseBody.write("value".getBytes());
        responseBody.flush();
        responseBody.close();
        body.close();
    }
    
}
