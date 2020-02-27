package org.web.controller;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import org.test.di.annotations.Component;
import org.test.di.annotations.Configuration;
import org.test.di.annotations.PostConstruct;

@Component(priority = 5)
public class RegistryHolder {
    private Registry registry;
    
    @Configuration(prefix = "registry")
    private String host;
    @Configuration(prefix = "registry")
    private int port;
    
    @PostConstruct
    private void init() throws RemoteException {
         registry = LocateRegistry.getRegistry(host, port);
    }

    public Registry getRegistry() {
        return registry;
    }
}
