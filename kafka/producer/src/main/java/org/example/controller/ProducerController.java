package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.model.UserDto;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProducerController {

    private final KafkaTemplate<String, UserDto> kafkaTemplate;
    private static final String TOPIC = "test_topic";

    @PostMapping("/send")
    public String sendMessage(@RequestBody UserDto dto) {
        kafkaTemplate.send(TOPIC, dto);
        return "Message sent: " + dto.toString();
    }
}
