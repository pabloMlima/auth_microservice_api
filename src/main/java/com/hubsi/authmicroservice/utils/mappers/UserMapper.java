package com.hubsi.authmicroservice.utils.mappers;

import com.hubsi.authmicroservice.adapters.in.request.RegisterRequest;
import com.hubsi.authmicroservice.adapters.out.response.RegisterResponse;
import com.hubsi.authmicroservice.adapters.out.persistence.entities.User;
import com.hubsi.authmicroservice.utils.enums.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "password", source = "encodedPassword")
    @Mapping(target = "role", source = "role")
    User toEntity(RegisterRequest request, String encodedPassword, Role role);

    RegisterResponse entityToDtoRegister(User user, String token, String message);
}
