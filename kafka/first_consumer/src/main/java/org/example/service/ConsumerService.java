package org.example.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.example.model.UserDto;
import org.example.model.UserEntity;
import org.example.repository.UserRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ConsumerService {

    private final ObjectMapper objectMapper;
    private final UserRepository userRepository;

    @KafkaListener(topics = "test_topic", groupId = "group_id")
    @Transactional
    public void consume(String message) throws JsonProcessingException {
        UserDto dto = objectMapper.readValue(message, UserDto.class);
        saveUser(dto);
    }

    @Transactional
    public void saveUser(UserDto dto) {
        UserEntity userEntity = new UserEntity(null, dto.name(), dto.surname(), dto.age(), dto.phoneNumber());
        userRepository.save(userEntity);
    }
}
