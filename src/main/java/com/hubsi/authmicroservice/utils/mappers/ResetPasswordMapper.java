package com.hubsi.authmicroservice.utils.mappers;

import com.hubsi.authmicroservice.adapters.out.persistence.entities.ResetPasswordEntity;
import com.hubsi.authmicroservice.domain.reset_password.ResetPassword;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ResetPasswordMapper {

    @Mapping(target = "userEntity", source = "user")
    ResetPasswordEntity toEntity(ResetPassword resetPassword);

    @Mapping(target = "user", source = "userEntity")
    ResetPassword toDomain(ResetPasswordEntity resetPasswordEntity);
}
