package com.server.server;


import com.common.command.AddCustomerCommand;
import com.common.command.Command;
import com.common.command.DeleteCustomerCommand;
import com.common.command.ServerCommandManager;
import com.common.model.Customer;
import com.common.model.RemoteContextHolderImpl;
import com.mq.ConnectionFactory;
import com.server.command.AddCustomerCommandImpl;
import com.server.command.DeleteCustomerCommandImpl;
import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import javax.jms.JMSException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import lombok.extern.slf4j.Slf4j;
import org.postgresql.Driver;
import org.test.di.app.ApplicationContext;

import static ch.qos.logback.core.db.DBHelper.closeConnection;

@Slf4j
public class Main {

    private static final String JNDI = "java:comp/env/jdbc/mq";
    private static ConnectionFactory connectionFactory;

    private static void bind(Registry registry) throws NamingException, RemoteException {
        System.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.apache.naming.java.javaURLContextFactory");
        System.setProperty(Context.URL_PKG_PREFIXES, "org.apache.naming");

        final Context context = new InitialContext();
        try {
            context.createSubcontext("java:");
            context.createSubcontext("java:comp");
            context.createSubcontext("java:comp/env");
            context.createSubcontext("java:comp/env/jdbc");
            context.bind(JNDI, connectionFactory);
            
        } finally {
            context.close();
        }
    }
    
    public static void main(String[] args) throws IOException, ClassNotFoundException, SQLException, NamingException, JMSException {
        log.info("Starting...");
        Class.forName("org.postgresql.Driver");
        System.setProperty("com.sun.jndi.rmi.object.trustURLCodebase", "true");
        System.setProperty("com.sun.jndi.cosnaming.object.trustURLCodebase", "true");
        /*System.setProperty("sun.rmi.registry.registryFilter", "java.**;com.common.**");*/
        DriverManager.registerDriver(new Driver());
        System.setProperty("java.rmi.server.codebase", "http://localhost:2005");
        log.info(System.getProperty("java.security.policy"));
        liquibaseUpdateWithChangelog();
        ApplicationContext applicationContext = new ApplicationContext("com.server");
        connectionFactory = (ConnectionFactory) applicationContext.getBeanFactory().getBean("connectionFactory");
        Registry registry = LocateRegistry.createRegistry(2005);
       // bind(registry);
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
        log.info("Running...");
    }

    public static void liquibaseUpdateWithChangelog() {
        Connection connection = null;
        try {
            connection = getConnection();
            log.info("Schema - {}", connection.getSchema());
            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
            log.info("Starting liquibase");
            Liquibase liquibase = new Liquibase("db/changelog.xml", new ClassLoaderResourceAccessor(), database);
            liquibase.update("");
        } catch (LiquibaseException | SQLException | ClassNotFoundException e) {
            log.error("", e);
        } finally {
            if (connection != null) {
                closeConnection(connection);
            }
        }
    }

    public static Connection getConnection() throws SQLException, ClassNotFoundException {
        Class.forName("org.postgresql.Driver");
        log.info("Trying to get connection");
        return DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "postgres");
    }
}
