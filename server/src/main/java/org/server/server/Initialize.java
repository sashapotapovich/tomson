package org.server.server;

import org.server.server.servlet.CustomersListServlet;
import org.server.server.servlet.EditCustomerServlet;
import org.server.server.servlet.wrapper.HttpHandlerWithServletSupport;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.test.di.annotations.Autowired;
import org.test.di.annotations.Component;
import org.test.di.annotations.PostConstruct;

@Component
public class Initialize {
    
    private static final Logger log = LoggerFactory.getLogger(Initialize.class);

    @Autowired
    private CustomersListServlet customersListServlet;
    @Autowired
    private EditCustomerServlet editCustomerServlet;
    
    @PostConstruct
    public void run() throws IOException {
        int port = 0;
        log.info("Starting Server at port - {}", port);
        HttpServer server = HttpServer.create(new InetSocketAddress(8090), 0);

        server.createContext(customersListServlet.getPath(), new HttpHandlerWithServletSupport(customersListServlet));
        server.createContext(editCustomerServlet.getPath(), new HttpHandlerWithServletSupport(editCustomerServlet));
        
        server.setExecutor(null);
        server.start();
        log.info("Server is running");
    }
}
