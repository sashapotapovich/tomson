package com.mq;

import com.ibm.msg.client.jms.JmsConnectionFactory;
import com.ibm.msg.client.jms.JmsFactoryFactory;
import com.ibm.msg.client.wmq.WMQConstants;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import javax.jms.JMSException;
import org.test.di.annotations.Component;
import org.test.di.annotations.PostConstruct;

@Component
public class ConnectionFactory {

    private String host;
    private int port;
    private String userID;
    private String password;
    private String queueManager;
    private String channel;
    
    @PostConstruct
    public void init(){
        try (InputStream is = getClass().getResourceAsStream("/mq.properties")) {
            Properties props = new Properties();
            props.load(is);
            host = props.getProperty("jms.host");
            port = Integer.parseInt(props.getProperty("jms.port"));
            userID = props.getProperty("jms.userID");
            password = props.getProperty("jms.password");
            queueManager = props.getProperty("jms.queueManager");
            channel = props.getProperty("jms.channel");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public JmsConnectionFactory jmsConnectionFactory() throws JMSException {
        JmsConnectionFactory jmsConnectionFactory = JmsFactoryFactory.getInstance(WMQConstants.WMQ_PROVIDER)
                                                                     .createConnectionFactory();
        jmsConnectionFactory.setStringProperty(WMQConstants.WMQ_HOST_NAME, host);
        jmsConnectionFactory.setIntProperty(WMQConstants.WMQ_PORT, port);
        jmsConnectionFactory.setStringProperty(WMQConstants.USERID, userID);
        jmsConnectionFactory.setStringProperty(WMQConstants.PASSWORD, password);
        jmsConnectionFactory.setStringProperty(WMQConstants.WMQ_QUEUE_MANAGER, queueManager);
        jmsConnectionFactory.setStringProperty(WMQConstants.WMQ_CHANNEL, channel);
        jmsConnectionFactory.setIntProperty(WMQConstants.WMQ_CONNECTION_MODE, WMQConstants.WMQ_CM_CLIENT);
        return jmsConnectionFactory;
    }

}
