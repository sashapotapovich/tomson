package com.server.server;

import com.server.server.servlet.CustomersListServlet;
import com.server.server.servlet.EditCustomerServlet;
import com.server.server.servlet.wrapper.HttpHandlerWithServletSupport;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import org.test.di.annotations.Autowired;
import org.test.di.annotations.Component;
import org.test.di.annotations.PostConstruct;

@Component
public class Initialize {

    @Autowired
    private CustomersListServlet customersListServlet;
    @Autowired
    private EditCustomerServlet editCustomerServlet;
    
    @PostConstruct
    public void run() throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8090), 0);

        server.createContext(customersListServlet.getPath(), new HttpHandlerWithServletSupport(customersListServlet));
        server.createContext(editCustomerServlet.getPath(), new HttpHandlerWithServletSupport(editCustomerServlet));
        
        server.setExecutor(null);
        server.start();
        
        System.out.println("Server is running");
    }
}
