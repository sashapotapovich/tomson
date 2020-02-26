package org.server.server;


import com.common.command.AddCustomerCommand;
import com.common.command.DeleteCustomerCommand;
import com.common.command.FindAllCustomersCommand;
import com.common.command.FindCustomerByIdCommand;
import com.common.command.UpdateCustomerCommand;
import com.common.model.Customer;
import java.io.IOException;
import java.rmi.Remote;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.ThreadLocalRandom;
import lombok.extern.slf4j.Slf4j;
import org.server.command.DeleteCustomerCommandImpl;
import org.server.command.FindAllCustomersCommandImpl;
import org.server.command.FindCustomerByIdCommandImpl;
import org.server.command.UpdateCustomerCommandImpl;
import org.test.di.app.ApplicationContext;

@Slf4j
public class Main {

    public static void main(String[] args) throws IOException {
        log.info("Starting...");
        int rmiPort = 2006;
        //System.setProperty("com.sun.jndi.rmi.object.trustURLCodebase", "true");
        //System.setProperty("com.sun.jndi.cosnaming.object.trustURLCodebase", "true");
        /*System.setProperty("sun.rmi.registry.registryFilter", "java.**;com.common.**");*/
        //System.setProperty("java.rmi.server.codebase", "http://localhost:rmiPort");
        //log.info(System.getProperty("java.security.policy"));
        ApplicationContext applicationContext = new ApplicationContext("org");
        //connectionFactory = (ConnectionFactory) applicationContext.getBeanFactory().getBean("connectionFactory");
        Registry registry = LocateRegistry.createRegistry(rmiPort);
        //Remote adapter = new RemoteContextHolderImpl(connectionFactory.jmsConnectionFactory());
        //Remote remoteContext = UnicastRemoteObject.exportObject(adapter, rmiPort);
        //registry.rebind("context", remoteContext);
        ServerCommandManagerImpl scm = new ServerCommandManagerImpl();

        AddCustomerCommand addCustomerCommandImpl = (AddCustomerCommand) applicationContext
                .getBean("addCustomerCommandImpl");
        DeleteCustomerCommandImpl deleteCustomerCommandImpl = (DeleteCustomerCommandImpl) applicationContext
                .getBean("deleteCustomerCommandImpl");
        FindAllCustomersCommandImpl findAllCustomersCommandImpl = (FindAllCustomersCommandImpl) applicationContext
                .getBean("findAllCustomersCommandImpl");
        FindCustomerByIdCommandImpl findCustomerByIdCommandImpl = (FindCustomerByIdCommandImpl) applicationContext
                .getBean("findCustomerByIdCommandImpl");
        UpdateCustomerCommandImpl updateCustomerCommandImpl = (UpdateCustomerCommandImpl) applicationContext
                .getBean("updateCustomerCommandImpl");
/*        Map<Class, Command> commands = new HashMap<>();
        commands.put(AddCustomerCommand.class, addCustomerCommandImpl);
        commands.put(DeleteCustomerCommand.class, deleteCustomerCommandImpl);
        commands.put(FindAllCustomersCommand.class, findAllCustomersCommandImpl);
        commands.put(FindCustomerByIdCommand.class, findCustomerByIdCommandImpl);
        commands.put(UpdateCustomerCommand.class, updateCustomerCommandImpl);
        scm.setCommands(commands);*/
        Remote remoteAddCustomerCommandImpl = UnicastRemoteObject.exportObject(addCustomerCommandImpl, rmiPort);
        Remote remoteDeleteCustomerCommandImpl = UnicastRemoteObject.exportObject(deleteCustomerCommandImpl, rmiPort);
        Remote remoteFindAllCustomersCommandImpl = UnicastRemoteObject.exportObject(findAllCustomersCommandImpl, rmiPort);
        Remote remoteFindCustomerByIdCommandImpl = UnicastRemoteObject.exportObject(findCustomerByIdCommandImpl, rmiPort);
        Remote remoteUpdateCustomerCommandImpl = UnicastRemoteObject.exportObject(updateCustomerCommandImpl, rmiPort);
        registry.rebind(AddCustomerCommand.class.getSimpleName(), remoteAddCustomerCommandImpl);
        registry.rebind(DeleteCustomerCommand.class.getSimpleName(), remoteDeleteCustomerCommandImpl);
        registry.rebind(FindAllCustomersCommand.class.getSimpleName(), remoteFindAllCustomersCommandImpl);
        registry.rebind(FindCustomerByIdCommand.class.getSimpleName(), remoteFindCustomerByIdCommandImpl);
        registry.rebind(UpdateCustomerCommand.class.getSimpleName(), remoteUpdateCustomerCommandImpl);
        int ssn = ThreadLocalRandom.current().nextInt();
        addCustomerCommandImpl.execute(new Customer(String.valueOf(ssn), "Test", "Address Test"));
        String[] list = registry.list();
        for (String exposed : list){
            log.info("Remote Command registered - {}", exposed);
        }
        log.info("Running...");
    }
    
}
