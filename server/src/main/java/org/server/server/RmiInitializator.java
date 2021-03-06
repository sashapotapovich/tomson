package org.server.server;

import com.common.command.Command;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.test.di.annotations.Autowired;
import org.test.di.annotations.Component;
import org.test.di.annotations.Configuration;
import org.test.di.annotations.PostConstruct;

@Slf4j
@Component
public class RmiInitializator {
    
    @Autowired
    private List<Command> commands;
    @Configuration(prefix = "rmi")
    private int port;
    
    @PostConstruct
    public void init() throws RemoteException {
        Registry registry = LocateRegistry.createRegistry(port);
        commands.forEach(command -> {
            String simpleName = "";
            for (Class<?> interfaces : command.getClass().getInterfaces()){
                simpleName = interfaces.getSimpleName();
            }
            try {
                Remote remoteCommand = UnicastRemoteObject.exportObject(command, port);
                registry.rebind(simpleName, remoteCommand);
            } catch (RemoteException e) {
                log.error("Command was not registered - {}", simpleName);
            }
        });
        String[] list = registry.list();
        for (String exposed : list){
            log.info("Remote Command registered - {}", exposed);
        }
    }
}
