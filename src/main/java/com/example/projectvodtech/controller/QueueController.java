package com.example.projectvodtech.controller;

import com.example.projectvodtech.jms.Lengthener;
import com.example.projectvodtech.jms.Producer;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Metrics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.jms.JMSException;
import javax.management.MalformedObjectNameException;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Controller
@RequestMapping(path="/q")
public class QueueController {

    private AtomicInteger n;

    @Autowired
    Producer producer;

    @Autowired
    Lengthener lengthener;

    public QueueController (MeterRegistry registry) {
        n = registry.gauge("numberGauge", new AtomicInteger(0));
    }


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

    @Scheduled(cron="*/5 * * * * *")
    @GetMapping("/smartLength")
    public @ResponseBody int queueSmartLength() {

        int count = lengthener.getMessageCount("hypeQueue");
        System.out.println("hypeQueue count is: " + count);
        n.set(count);
        return count;
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
            return lengthener.getQueueSize("customerQueue");
        } catch (MalformedObjectNameException | IOException e) {
            e.printStackTrace();
            return 0L;
        }

    }

    @GetMapping("/queues")
    public @ResponseBody HashMap<String, Integer> getQueues() {

        try {
            return producer.getQueuesAndCount();
        } catch (JMSException e) {
            e.printStackTrace();
        }

        return null;
    }

}
