package com.example.projectvodtech.controller;

import com.example.projectvodtech.jms.Lengthener;
import com.example.projectvodtech.jms.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.jms.JMSException;
import javax.management.MalformedObjectNameException;
import java.io.IOException;
import java.util.HashMap;

@Controller
@RequestMapping(path="/q")
public class QueueController {

    @Autowired
    Producer producer;

    @GetMapping("/send")
    public @ResponseBody String sendMessage() {

        try {
            System.out.println(producer.sendMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Success";
    }

    @GetMapping("/smartSend/{message}")
    public @ResponseBody String sendSmartMessage(@PathVariable(value = "message") String msg) {
        producer.sendMessageSmarter("hypeQueue", msg);
        return "success";
    }
//
//    @GetMapping("/dest")
//    public @ResponseBody String destinations() {
//        try {
//            new Producer().listDestinations();
//        } catch (JMSException e) {
//            e.printStackTrace();
//        }
//        return "werks?";
//    }

    @GetMapping("/length")
    public @ResponseBody Long queueLength() {

        try {
            return new Lengthener().getQueueSize("customerQueue");
        } catch (MalformedObjectNameException | IOException e) {
            e.printStackTrace();
            return 0L;
        }

    }

    @GetMapping("/queues")
    public @ResponseBody HashMap<String, Integer> getQueues() {

        try {
            return new Producer().getQueuesAndCount();
        } catch (JMSException e) {
            e.printStackTrace();
        }

        return null;
    }

}
