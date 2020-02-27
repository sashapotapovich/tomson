package org.mq;

import com.ibm.msg.client.jms.JmsConnectionFactory;
import com.ibm.msg.client.jms.JmsFactoryFactory;
import com.ibm.msg.client.wmq.WMQConstants;
import javax.jms.JMSException;
import org.test.di.annotations.Component;
import org.test.di.annotations.Configuration;

@Component
public class ConnectionFactory {

    @Configuration(prefix = "jms")
    private String host;
    @Configuration(prefix = "jms")
    private int port;
    @Configuration(prefix = "jms")
    private String userID;
    @Configuration(prefix = "jms")
    private String password;
    @Configuration(prefix = "jms")
    private String queueManager;
    @Configuration(prefix = "jms")
    private String channel;
    
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
