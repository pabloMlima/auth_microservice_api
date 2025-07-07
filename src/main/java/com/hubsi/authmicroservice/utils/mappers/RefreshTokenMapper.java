package com.hubsi.authmicroservice.utils.mappers;

import com.hubsi.authmicroservice.adapters.out.persistence.entities.RefreshTokenEntity;
import com.hubsi.authmicroservice.domain.refresh_token.RefreshToken;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RefreshTokenMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "expiresAt", source = "expiresAt")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "user", source = "userEntity")
    RefreshToken toDomain(RefreshTokenEntity refreshTokenEntity);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "expiresAt", source = "expiresAt")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "userEntity", source = "user")
    RefreshTokenEntity toEntity(RefreshToken refreshToken);
}
