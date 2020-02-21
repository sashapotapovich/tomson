package org.server.server;

import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.List;
import org.server.server.servlet.CustomServlet;
import org.server.server.servlet.wrapper.HttpHandlerWithServletSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.test.di.annotations.Autowired;
import org.test.di.annotations.Component;
import org.test.di.annotations.PostConstruct;

@Component
public class Initialize {
    
    private static final Logger log = LoggerFactory.getLogger(Initialize.class);
        
    @Autowired
    List<CustomServlet> servlets;
    
    @PostConstruct
    public void run() throws IOException {
        int port = 8090;
        log.info("Starting Server at port - {}", port);
        HttpServer server = HttpServer.create(new InetSocketAddress(8090), 0);
        servlets.forEach(servlet -> server.createContext(servlet.getPath(), new HttpHandlerWithServletSupport(servlet)));
        server.setExecutor(null);
        server.start();
        log.info("Server is running");
    }
}
