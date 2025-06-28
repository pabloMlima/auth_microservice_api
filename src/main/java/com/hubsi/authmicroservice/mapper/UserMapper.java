package com.hubsi.authmicroservice.mapper;

import com.hubsi.authmicroservice.dto.request.RegisterRequest;
import com.hubsi.authmicroservice.dto.response.RegisterResponse;
import com.hubsi.authmicroservice.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "password", source = "encodedPassword")
    User toEntity(RegisterRequest request, String encodedPassword);

    RegisterResponse entityToDtoRegister(User user, String token, String message);
}
