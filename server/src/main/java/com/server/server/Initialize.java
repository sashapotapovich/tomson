package com.server.server;

import com.common.command.AddCustomerCommand;
import com.common.command.Command;
import com.common.command.ServerCommandManager;
import com.server.command.AddCustomerCommandImpl;
import com.server.server.servlet.HelloServlet;
import com.server.server.servlet.wrapper.HttpHandlerWithServletSupport;
import com.sun.net.httpserver.HttpServer;
import org.test.di.annotations.Autowired;
import org.test.di.annotations.Component;
import org.test.di.annotations.PostConstruct;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.rmi.AlreadyBoundException;
import java.rmi.Remote;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

@Component
public class Initialize {

    @Autowired
    private HelloServlet helloServlet;

    @PostConstruct
    public void run() throws AlreadyBoundException, IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        server.createContext("/test", new HttpHandlerWithServletSupport(helloServlet));


        server.setExecutor(null);
        server.start();

        Registry registry = LocateRegistry.createRegistry(2005);
        ServerCommandManagerImpl scm = new ServerCommandManagerImpl();

        Map<Class, Command> commands = new HashMap<>();
        commands.put(AddCustomerCommand.class, new AddCustomerCommandImpl());
        scm.setCommands(commands);

        Remote remoteServerCommandManager = UnicastRemoteObject.exportObject(scm, 2005);
        registry.bind(ServerCommandManager.class.getSimpleName(), remoteServerCommandManager);
    }
}
