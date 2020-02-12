package com.mq;

import com.ibm.msg.client.jms.JmsConnectionFactory;
import java.util.Properties;
import javax.jms.JMSException;
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
        env.put(Context.PROVIDER_URL, "remote+http://localhost:2005");
        Context localContext = new InitialContext(env);
        JmsConnectionFactory jmsConnectionFactory = connectionFactory.jmsConnectionFactory();
        localContext.bind("java:/mq", jmsConnectionFactory);
    }
}
