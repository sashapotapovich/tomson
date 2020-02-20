package org.server.server;


import com.common.command.AddCustomerCommand;
import com.common.command.Command;
import com.common.command.DeleteCustomerCommand;
import com.common.command.ServerCommandManager;
import com.common.model.Customer;
import com.common.model.RemoteContextHolderImpl;
import java.io.IOException;
import java.rmi.Remote;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import javax.jms.JMSException;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.mq.ConnectionFactory;
import org.postgresql.Driver;
import org.server.command.AddCustomerCommandImpl;
import org.server.command.DeleteCustomerCommandImpl;
import org.test.di.app.ApplicationContext;

@Slf4j
public class Main {

    private static ConnectionFactory connectionFactory;

    public static void main(String[] args) throws IOException, SQLException, JMSException {
        log.info("Starting...");
        final StandardServiceRegistry dbRegistry =
                new StandardServiceRegistryBuilder()
                        .configure("hibernate.cfg.xml")
                        .build();

        SessionFactory sessionFactory = null;

        try {
            sessionFactory = new MetadataSources(dbRegistry).buildMetadata()
                                                          .buildSessionFactory();
        } catch (Exception e) {
            StandardServiceRegistryBuilder.destroy(dbRegistry);
            log.error("cannot create sessionFactory", e);
            System.exit(1);
        }
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        
        //System.setProperty("com.sun.jndi.rmi.object.trustURLCodebase", "true");
        //System.setProperty("com.sun.jndi.cosnaming.object.trustURLCodebase", "true");
        /*System.setProperty("sun.rmi.registry.registryFilter", "java.**;com.common.**");*/
        DriverManager.registerDriver(new Driver());
        //System.setProperty("java.rmi.server.codebase", "http://localhost:2005");
        //log.info(System.getProperty("java.security.policy"));
        ApplicationContext applicationContext = new ApplicationContext("org");
        connectionFactory = (ConnectionFactory) applicationContext.getBeanFactory().getBean("connectionFactory");
        Registry registry = LocateRegistry.createRegistry(2005);
        Remote adapter = new RemoteContextHolderImpl(connectionFactory.jmsConnectionFactory());
        Remote remoteContext = UnicastRemoteObject.exportObject(adapter, 2005);
        registry.rebind("context", remoteContext);
        ServerCommandManagerImpl scm = new ServerCommandManagerImpl();

        Map<Class, Command> commands = new HashMap<>();
        commands.put(AddCustomerCommand.class, new AddCustomerCommandImpl());
        commands.put(DeleteCustomerCommand.class, new DeleteCustomerCommandImpl());
        scm.setCommands(commands);
        Remote remoteServerCommandManager = UnicastRemoteObject.exportObject(scm, 2005);
        registry.rebind(ServerCommandManager.class.getSimpleName(), remoteServerCommandManager);
        int ssn = ThreadLocalRandom.current().nextInt();
        AddCustomerCommandImpl addCustomerCommand = new AddCustomerCommandImpl();
        addCustomerCommand.execute(new Customer(String.valueOf(ssn), "ASD", "ZXC"));
        int ssn2 = ThreadLocalRandom.current().nextInt();
        Customer customer = new Customer(String.valueOf(ssn2), "Test", "Test");
        session.persist(customer);
        transaction.commit();

        log.info("Running...");
    }
    
}
