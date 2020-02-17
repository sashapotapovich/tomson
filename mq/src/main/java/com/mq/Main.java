package com.mq;

import com.common.model.RemoteContextHolder;
import com.ibm.msg.client.jms.JmsConnectionFactory;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Properties;
import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.test.di.app.ApplicationContext;

public class Main {

    private static final String JNDI = "java:comp/env/jdbc/mq";

    private static void lookup(Context context) throws NamingException {
        try {
            final ConnectionFactory jmsConnectionFactory = (ConnectionFactory) context.lookup(JNDI);
            JmsConnectionFactory jmsConnectionFactory1 = jmsConnectionFactory.jmsConnectionFactory();
            Connection connection = jmsConnectionFactory1.createConnection();
            connection.start();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Destination queue = session.createQueue("queue:///" + "DEV.QUEUE.1");
            MessageProducer producer = session.createProducer(queue);
            TextMessage message = session.createTextMessage();
            message.setText("text");
            producer.send(message);
            producer.close();
            connection.close();
        } catch (JMSException e) {
            e.printStackTrace();
        } finally {
            context.close();
        }
    }


    public static void main(String[] args) throws NamingException, RemoteException, NotBoundException {
        ApplicationContext applicationContext = new ApplicationContext("com.mq");
        System.setProperty("sun.rmi.registry.registryFilter", "java.**;com.common.**");
        Properties env = new Properties();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.rmi.registry.RegistryContextFactory");
        env.put(Context.PROVIDER_URL, "rmi://localhost:2005");
        Context remoteContext = new InitialContext(env);
        try {
            RemoteContextHolder context = (RemoteContextHolder) remoteContext.lookup("context");
            Context connection = context.getConnection();
            connection.lookup(JNDI);
        } catch (NamingException | ClassCastException e) {
            e.printStackTrace();
        }
        Registry localhost = LocateRegistry.getRegistry("localhost", 2005);

        RemoteContextHolder context = (RemoteContextHolder) localhost.lookup("context");
        lookup(context.getConnection());
    }
}
