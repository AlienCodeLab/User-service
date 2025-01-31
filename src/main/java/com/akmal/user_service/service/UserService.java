package com.akmal.user_service.service;

import com.akmal.user_service.dto.UserDto;
import com.akmal.user_service.entity.Role;
import com.akmal.user_service.entity.UserEntity;
import com.akmal.user_service.exception.UserNotFoundException;
import com.akmal.user_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserKafkaProducer kafkaProducer;
    public List<UserDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public UserDto getUserById(Long id) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        return convertToDto(user);
    }

    public UserDto createUser(UserDto userDto) {
        if (userRepository.findByEmail(userDto.getEmail()).isPresent()) {
            throw new IllegalStateException("Email already in use");
        }
        UserEntity user = UserEntity.builder()
                .username(userDto.getUsername())
                .email(userDto.getEmail())
                .password(passwordEncoder.encode("defaultPassword")) // Set default password
                .role(Role.USER)
                .build();

        UserEntity savedUser = userRepository.save(user);
        kafkaProducer.sendUserEvent("User created: " + userDto.getUsername());
        return convertToDto(savedUser);
    }

    public UserDto updateUser(Long id, UserDto userDto) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        UserEntity updatedUser = userRepository.save(user);

        return convertToDto(updatedUser);
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException("User not found");
        }
        userRepository.deleteById(id);
    }

    private UserDto convertToDto(UserEntity user) {
        return UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole().name())
                .build();
    }
}
