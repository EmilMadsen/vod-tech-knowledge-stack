package com.example.projectvodtech.jms;

import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.apache.activemq.broker.jmx.DestinationViewMBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.BrowserCallback;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.QueueBrowser;
import javax.jms.Session;
import javax.management.*;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import java.io.IOException;
import java.util.Collections;

@Component
public class Lengthener {

    @Autowired
    private JmsTemplate jmsTemplate;

    private MBeanServerConnection mBeanServerConnection;

//    public int getMessageCount(String messageSelector)
//    {
//        return jmsTemplate.browseSelected(messageSelector, new BrowserCallback<Integer>() {
//            @Override
//            public Integer doInJms(Session s, QueueBrowser qb) throws JMSException
//            {
//                return Collections.list(qb.getEnumeration()).size();
//            }
//        });
//    }

    public int getMessageCount(String queueName)
    {
        return jmsTemplate.browseSelected(queueName, null, new BrowserCallback<Integer>() {
            @Override
            public Integer doInJms(Session s, QueueBrowser qb) throws JMSException
            {

                return Collections.list(qb.getEnumeration()).size();
            }
        });
    }

    public Long getQueueSize(String queueName) throws MalformedObjectNameException, IOException {

//        Long queueSize = null;
//        try {
//
//            ObjectName objectNameRequest = new ObjectName(
//                    "org.apache.activemq:BrokerName=localhost,Type=Queue,Destination=" + queueName);
//
//            queueSize = (Long) mBeanServerConnection.getAttribute(objectNameRequest, "QueueSize");
//
//            return queueSize;
//
//        } catch (IOException | MalformedObjectNameException | MBeanException | AttributeNotFoundException | InstanceNotFoundException | ReflectionException e) {
//            e.printStackTrace();
//        }
//
//        return queueSize;

        // connection
        String url = "service:jmx:rmi:///jndi/rmi://localhost:1099/jmxrmi";
        JMXConnector connector = JMXConnectorFactory.connect(new JMXServiceURL(url));
        MBeanServerConnection connection = connector.getMBeanServerConnection();

        // get queue size
        ObjectName nameConsumers = new ObjectName("org.apache.activemq:type=Broker,brokerName=localhost,destinationType=Queue,destinationName=" + queueName);
        DestinationViewMBean mbView = MBeanServerInvocationHandler.newProxyInstance(connection, nameConsumers, DestinationViewMBean.class, true);

        return mbView.getQueueSize();
    }

}
