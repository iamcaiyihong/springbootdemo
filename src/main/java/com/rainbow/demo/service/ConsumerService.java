package com.rainbow.demo.service;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.benmanes.caffeine.cache.Cache;
import com.rainbow.demo.kafka.Message;
import com.rainbow.demo.mapper.StudentMapper;

@Service
public class ConsumerService {

    private final ObjectMapper objectMapper;

    @Autowired
    StudentMapper studentMapper;

     @Autowired
    RedisTemplate<String, Object> redisTemplate;

    @Autowired
    Cache<String, Object> caffeineCache;


    

    public ConsumerService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "student-topic", groupId = "demo-group")
    public void listen(ConsumerRecord<String, String> record) {
        try {
            Message msg = objectMapper.readValue(record.value(), Message.class);
            System.out.println("Received: " + msg);

            // 修改 name
            msg.setName(msg.getName() + "_processed");
            System.out.println("Modified: " + msg);
            int id = msg.getId();
            String key = "stu_"+id;
            studentMapper.updateNameById(msg.getId(), msg.getName());
            redisTemplate.delete(key);
            caffeineCache.invalidate(key);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}

