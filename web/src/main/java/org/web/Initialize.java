package org.web;

import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.test.di.annotations.Autowired;
import org.test.di.annotations.Component;
import org.test.di.annotations.PostConstruct;
import org.web.servlet.CustomServlet;
import org.web.servlet.wrapper.HttpHandlerWithServletSupport;

@Component
@Slf4j
public class Initialize {
            
    @Autowired
    List<CustomServlet> servlets;
    
    @PostConstruct
    public void run() throws IOException {
        int port = 8090;
        log.info("Starting Server at port - {}", port);
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        servlets.forEach(customServlet -> log.info("{} servlet registered", customServlet.getPath()));
        servlets.forEach(servlet -> server.createContext(servlet.getPath(), new HttpHandlerWithServletSupport(servlet)));
        server.setExecutor(null);
        server.start();
        log.info("Server is running");
    }
}
