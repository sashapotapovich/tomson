package org.web.controller;

import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.test.di.annotations.Autowired;
import org.test.di.annotations.Component;
import org.test.di.annotations.Configuration;
import org.test.di.annotations.PostConstruct;
import org.web.servlet.CustomServlet;
import org.web.servlet.wrapper.HttpHandlerWithServletSupport;

@Slf4j
@Component
public class Initialize {

    @Autowired
    private CustomAuthenticator customAuthenticator;
    @Autowired
    private List<CustomServlet> servlets;
    @Configuration(prefix = "server")
    private int port;

    @PostConstruct
    public void run() throws IOException {
        log.info("Starting Server at port - {}", port);
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        servlets.forEach(customServlet -> log.info("{} servlet registered", customServlet.getPath()));
        servlets.forEach(servlet -> {
            HttpContext context = server.createContext(servlet.getPath(), new HttpHandlerWithServletSupport(servlet));
            context.setAuthenticator(customAuthenticator);
        });
        server.setExecutor(null);
        server.start();
        log.info("Server is running");
    }
    
}
