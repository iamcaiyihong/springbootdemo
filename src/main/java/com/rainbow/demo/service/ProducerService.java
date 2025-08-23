package com.rainbow.demo.service;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rainbow.demo.kafka.Message;

@Service
public class ProducerService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public ProducerService(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public void sendMessage(Message message) {
        try {
            String json = objectMapper.writeValueAsString(message);
            kafkaTemplate.send("student-topic", json);
            System.out.println("Sent: " + json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error serializing message", e);
        }
    }
}
