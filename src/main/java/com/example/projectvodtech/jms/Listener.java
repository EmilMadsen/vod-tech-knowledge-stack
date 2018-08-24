package com.example.projectvodtech.jms;

import com.google.gson.Gson;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;
import java.util.Map;

@Component
public class Listener {

//    @JmsListener(destination = "inbound.queue")
//    @SendTo("outbound.queue")
//    public String anotherName(final Message jsonMessage) throws JMSException {
//        String messageData = null;
//        System.out.println("Received message " + jsonMessage);
//        String response = null;
//        if(jsonMessage instanceof TextMessage) {
//            TextMessage textMessage = (TextMessage)jsonMessage;
//            messageData = textMessage.getText();
//            Map map = new Gson().fromJson(messageData, Map.class);
//            response  = "Hello " + map.get("name");
//        }
//
//        try {
//            Thread.sleep(2000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        System.out.println(response);
//        return response;
//    }


    @JmsListener(destination = "hypeQueue")
    public void hypeConsumer(final Message jsonMessage) throws JMSException {

        System.out.println("Received message " + jsonMessage);

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


}
