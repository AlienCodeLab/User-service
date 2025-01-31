package com.akmal.user_service.dto;

import lombok.*;

@Builder
@Value
public class UserDto {
    Long id;
    String username;
    String email;
    String role; // Avoid exposing Role as ENUM directly
}
