package com.akmal.user_service.mapper;

import com.akmal.user_service.dto.UserDto;
import com.akmal.user_service.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(UserEntity entity);
    @Mapping(target = "password",ignore = true)
    @Mapping(target = "createdAt",ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "id",ignore = true)
    UserEntity toEntity(UserDto dto);
}
