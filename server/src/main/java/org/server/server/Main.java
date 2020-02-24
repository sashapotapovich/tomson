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
import org.mq.ConnectionFactory;
import org.server.command.DeleteCustomerCommandImpl;
import org.server.command.FindAllCustomersCommandImpl;
import org.server.command.FindCustomerByIdCommandImpl;
import org.server.command.UpdateCustomerCommandImpl;
import org.test.di.app.ApplicationContext;

@Slf4j
public class Main {

    private static ConnectionFactory connectionFactory;

    public static void main(String[] args) throws IOException {
        log.info("Starting...");
        //System.setProperty("com.sun.jndi.rmi.object.trustURLCodebase", "true");
        //System.setProperty("com.sun.jndi.cosnaming.object.trustURLCodebase", "true");
        /*System.setProperty("sun.rmi.registry.registryFilter", "java.**;com.common.**");*/
        //System.setProperty("java.rmi.server.codebase", "http://localhost:2005");
        //log.info(System.getProperty("java.security.policy"));
        ApplicationContext applicationContext = new ApplicationContext("org");
        //connectionFactory = (ConnectionFactory) applicationContext.getBeanFactory().getBean("connectionFactory");
        Registry registry = LocateRegistry.createRegistry(2005);
        //Remote adapter = new RemoteContextHolderImpl(connectionFactory.jmsConnectionFactory());
        //Remote remoteContext = UnicastRemoteObject.exportObject(adapter, 2005);
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
        Remote remoteAddCustomerCommandImpl = UnicastRemoteObject.exportObject(addCustomerCommandImpl, 2005);
        Remote remoteDeleteCustomerCommandImpl = UnicastRemoteObject.exportObject(deleteCustomerCommandImpl, 2005);
        Remote remoteFindAllCustomersCommandImpl = UnicastRemoteObject.exportObject(findAllCustomersCommandImpl, 2005);
        Remote remoteFindCustomerByIdCommandImpl = UnicastRemoteObject.exportObject(findCustomerByIdCommandImpl, 2005);
        Remote remoteUpdateCustomerCommandImpl = UnicastRemoteObject.exportObject(updateCustomerCommandImpl, 2005);
        registry.rebind(AddCustomerCommand.class.getSimpleName(), remoteAddCustomerCommandImpl);
        registry.rebind(DeleteCustomerCommand.class.getSimpleName(), remoteDeleteCustomerCommandImpl);
        registry.rebind(FindAllCustomersCommand.class.getSimpleName(), remoteFindAllCustomersCommandImpl);
        registry.rebind(FindCustomerByIdCommand.class.getSimpleName(), remoteFindCustomerByIdCommandImpl);
        registry.rebind(UpdateCustomerCommand.class.getSimpleName(), remoteUpdateCustomerCommandImpl);
        int ssn = ThreadLocalRandom.current().nextInt();
        addCustomerCommandImpl.execute(new Customer(String.valueOf(ssn), "ASD", "ZXC"));
        String[] list = registry.list();
        for (String exposed : list){
            log.error(exposed);
        }
        log.info("Running...");
    }
    
}
