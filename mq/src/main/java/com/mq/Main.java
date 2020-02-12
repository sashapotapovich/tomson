package com.mq;

import com.ibm.msg.client.jms.JmsConnectionFactory;
import java.rmi.Naming;
import java.util.Properties;
import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.test.di.annotations.Autowired;
import org.test.di.app.ApplicationContext;

public class Main {

    public static void main(String[] args) throws NamingException, JMSException {
        ApplicationContext applicationContext = new ApplicationContext("com.mq");
        ConnectionFactory connectionFactory = (ConnectionFactory) applicationContext.getBeanFactory().getBean("connectionFactory");
        Properties env = new Properties();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.rmi.registry.RegistryContextFactory");
        env.put(Context.PROVIDER_URL, "rmi://localhost:2005/");
        Context remoteContext = new InitialContext(env);
        JmsConnectionFactory jmsConnectionFactory = connectionFactory.jmsConnectionFactory();
        /*Connection connection = jmsConnectionFactory.createConnection();
        connection.start();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Destination queue = session.createQueue("queue:///" + "DEV.QUEUE.1");
        MessageProducer producer = session.createProducer(queue);
        TextMessage message =  session.createTextMessage();
        message.setText("text");
        producer.send(message);*/
        //Naming.rebind("jmsConnectionFactory", jmsConnectionFactory);
        remoteContext.bind("rmi://localhost:2005/jms", jmsConnectionFactory);
    }
}
