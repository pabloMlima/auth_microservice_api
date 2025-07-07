package com.hubsi.authmicroservice.utils.mappers;

import com.hubsi.authmicroservice.adapters.in.request.RegisterRequest;
import com.hubsi.authmicroservice.adapters.out.persistence.entities.UserEntity;
import com.hubsi.authmicroservice.adapters.out.response.RegisterResponse;
import com.hubsi.authmicroservice.domain.user.User;
import com.hubsi.authmicroservice.utils.enums.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "password", source = "encodedPassword")
    @Mapping(target = "role", source = "role")
    UserEntity toEntity(RegisterRequest request, String encodedPassword, Role role);

    RegisterResponse entityToDtoRegister(User user, String token, String message);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "nome", source = "nome")
    @Mapping(target = "sobrenome", source = "sobrenome")
    @Mapping(target = "email", source = "email")
    User entityToDomain(UserEntity jpa);

    UserEntity domainToEntity(User user);

    @Mapping(target = "password", source = "encodedPassword")
    @Mapping(target = "role", source = "role")
    User dtoToDomain(RegisterRequest request, String encodedPassword, Role role);

}
