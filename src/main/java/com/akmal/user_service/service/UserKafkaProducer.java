package com.akmal.user_service.service;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.stereotype.Service;

@Service
@EnableKafka
@RequiredArgsConstructor
public class UserKafkaProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    private static final String TOPIC = "user-events";

    public void sendUserEvent(String message) {
        kafkaTemplate.send(TOPIC, message);
    }
}
