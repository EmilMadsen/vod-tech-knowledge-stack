package com.example.projectvodtech.jms;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.advisory.DestinationSource;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.jms.*;
import java.net.URISyntaxException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

@Component
public class Producer {

    @Autowired
    private JmsTemplate jmsTemplate;

    public void sendMessageSmarter(String queueName, String message) {
        System.out.println("sending: " + message);
        jmsTemplate.convertAndSend(queueName, message);
    }

//    @Value("${activemq.brokerUrl}")
//    private String brokerUrl;

//    public String sendMessage() throws JMSException {
//
//
//        JmsTemplate jmsTemplate = getBean(JmsTemplate.class);
//
//
//        return "test";
//    }

    public String sendMessage() throws URISyntaxException, Exception{

        Connection connection = null;
        try {

            // Producer
            ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");
            connection = connectionFactory.createConnection();

            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Queue queue = session.createQueue("hypeQueue");
            MessageProducer producer = session.createProducer(queue);

            String payload = "HYPE";
            Message msg = session.createTextMessage(payload);

            System.out.println("Sending text '" + payload + "'");

            producer.send(msg);
            session.close();

        } finally {

            if (connection != null) {

                connection.close();

            }

        }

        return "hyped";

    }

//    public void listDestinations() throws JMSException {
//
//        // Create a ConnectionFactory
//        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");
//
//        // Create a Connection
//        ActiveMQConnection connection = (ActiveMQConnection) connectionFactory.createConnection();
//
//
//        //Important point that was missed in the above answer
//        connection.start();
//
//        DestinationSource ds = connection.getDestinationSource();
//        Set<ActiveMQQueue> queues = ds.getQueues();
//
//        for(ActiveMQQueue queue : queues){
//            try {
//                System.out.println(queue.getQueueName());
//            } catch (JMSException e) {
//                e.printStackTrace();
//            }
//        }
//    }


    public HashMap<String, Integer> getQueuesAndCount() throws JMSException {

        // Create a ConnectionFactory
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");

        // Create a Connection
        ActiveMQConnection connection = (ActiveMQConnection) connectionFactory.createConnection();

        connection.start();

        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        Set<ActiveMQQueue> amqs = connection.getDestinationSource().getQueues();
        Iterator<ActiveMQQueue> queues = amqs.iterator();

        HashMap<String, Integer> payload = new HashMap<String, Integer>();

        while ( queues.hasNext() )
        {
            ActiveMQQueue queue_t = queues.next();
            String q_name = queue_t.getPhysicalName();
//            System.out.println( "Queue = " + q_name);

            QueueBrowser queueBrowser = session.createBrowser(queue_t);
            Enumeration<Message> e = (Enumeration<Message>) queueBrowser.getEnumeration();

            int numMsgs = 0;
            while(e.hasMoreElements())
            {
                Message message = (Message) e.nextElement();
//                System.out.println("Current message: " + message.toString());
                numMsgs++;
            }
//            System.out.println(q_name + ": No of messages = " + numMsgs);
            queueBrowser.close();

            payload.put(q_name, numMsgs);
        }


        session.close();
        connection.close();

        return payload;


    }

}
