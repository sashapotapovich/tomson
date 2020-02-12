package com.server.server;


import com.common.command.AddCustomerCommand;
import com.common.command.Command;
import com.common.command.DeleteCustomerCommand;
import com.common.command.ServerCommandManager;
import com.common.model.Customer;
import com.server.command.AddCustomerCommandImpl;
import com.server.command.DeleteCustomerCommandImpl;
import java.io.IOException;
import java.rmi.AlreadyBoundException;
import java.rmi.Remote;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
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

	public static void main(String[] args) throws AlreadyBoundException, IOException, ClassNotFoundException, SQLException {
		log.info("Starting...");
        Class.forName("org.postgresql.Driver");
        DriverManager.registerDriver(new Driver());
		System.setProperty("java.rmi.server.codebase", "http://127.0.0.1:2005");
        log.info(System.getProperty("java.security.policy"));
        liquibaseUpdateWithChangelog();
        ApplicationContext applicationContext = new ApplicationContext("com.server");
        Registry registry = LocateRegistry.createRegistry(2005);
        ServerCommandManagerImpl scm = new ServerCommandManagerImpl();

        Map<Class, Command> commands = new HashMap<>();
        commands.put(AddCustomerCommand.class, new AddCustomerCommandImpl());
        commands.put(DeleteCustomerCommand.class, new DeleteCustomerCommandImpl());
        scm.setCommands(commands);
        System.setProperty("java.rmi.server.hostname","127.0.0.1");
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
        return DriverManager.getConnection("jdbc:postgresql://postgres:5432/postgres", "postgres", "postgres");
    }
}
