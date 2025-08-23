package com.rainbow.demo.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rainbow.demo.kafka.Message;
import com.rainbow.demo.service.ProducerService;

@RestController
@RequestMapping("/kafka")
public class KafkaController {

    private final ProducerService producerService;

    public KafkaController(ProducerService producerService) {
        this.producerService = producerService;
    }

    @PostMapping("/send")
    public String send(@RequestBody Message message) {
        producerService.sendMessage(message);
        return "Message sent!";
    }
}

