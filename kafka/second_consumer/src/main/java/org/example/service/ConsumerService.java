package org.example.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.model.UserDto;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ConsumerService {

    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "test_topic", groupId = "group_id_2")
    public void consume(String message) throws JsonProcessingException {
        UserDto dto = objectMapper.readValue(message, UserDto.class);
        if (dto.age() >= 18) {
            log.info(
                    "Совершеннолетний пользователь {} прошел регистрацию," + " контактный номер телефона {} ",
                    dto.name(),
                    dto.phoneNumber());

        } else {
            log.warn(
                    "Пользователь {} прошел регистрацию," + " контактный номер телефона {} ",
                    dto.name(),
                    dto.phoneNumber());
        }
    }
}
