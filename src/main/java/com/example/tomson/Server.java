package com.example.tomson;

import com.example.tomson.jndi.JndiProperties;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.test.di.annotations.Autowired;
import org.test.di.annotations.Component;
import org.test.di.annotations.PostConstruct;
import org.test.di.utils.ClassUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@Component
public class Server {

    private final Logger log = LoggerFactory.getLogger(Application.class);
    @Autowired
    private JndiProperties jndiProperties;
    private HttpServer httpServer;

    @PostConstruct
    public void init() throws IOException {
        int port = resolvePort();
        log.info("Starting Server on the port - {}", port);
        httpServer = HttpServer.create(new InetSocketAddress(port), 0);
        HttpContext server = httpServer.createContext("/");
        HttpContext jndi = httpServer.createContext("/jndi");
        jndi.setHandler(this::handleTestRequest);
        httpServer.start();
    }

    private int resolvePort() {
        Optional<Integer> port = Optional.empty();
        try {
            URL url = ClassUtil.getClassLoader().getResource("application.properties");
            Path path = Paths.get(url.toURI());
            port = Files.readAllLines(path, StandardCharsets.UTF_8)
                    .stream().parallel().filter(s -> s.startsWith("server.port"))
                    .map(s -> s.replace(" ", "")
                            .substring("server.port=".length()))
                    .map(Integer::valueOf)
                    .findFirst();
        } catch (NullPointerException | URISyntaxException | IOException e) {
            log.error("Unable to locate application.properties file, error - {}, {}", e.getMessage(), e.getMessage());
            log.error("Default port 8080 will be used");
        }
        return port.orElse(8080);
    }

    private void handleTestRequest(HttpExchange exchange) throws IOException {
        log.info(exchange.getProtocol());
        exchange.getHttpContext().getAttributes()
                .forEach((key, value) -> log.info("Http Attributes: key - " + key +
                        ", value - " + value));
        log.info(exchange.getHttpContext().getPath());
        log.info(exchange.getRequestMethod());
        BufferedReader body = new BufferedReader(new InputStreamReader(exchange.getRequestBody()));
        while (body.ready()) {
            String line = body.readLine();
            log.info(line);
        }
        String query = exchange.getRequestURI().getQuery();
        log.error("Query - {}", query);
        log.error("URI - {}", exchange.getRequestURI().toASCIIString());
        exchange.getRequestHeaders()
                .forEach((key, value) -> log.info("Headers: key - " + key + "values - "
                        + value.stream()
                        .reduce(" - ", String::concat)));
        exchange.sendResponseHeaders(200, "value".getBytes().length);
        OutputStream responseBody = exchange.getResponseBody();
        exchange.setStreams(null, responseBody);
        responseBody.write("value".getBytes());
        responseBody.flush();
        responseBody.close();
        body.close();
    }

}
