package com.hubsi.authmicroservice.adapters.out.persistence.repository.impl;

import com.hubsi.authmicroservice.adapters.out.persistence.entities.UserEntity;
import com.hubsi.authmicroservice.adapters.out.persistence.repository.JpaUserRepository;
import com.hubsi.authmicroservice.domain.user.User;
import com.hubsi.authmicroservice.domain.user.UserRepository;
import com.hubsi.authmicroservice.utils.mappers.UserMapper;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final JpaUserRepository jpaUserRepository;

    private final UserMapper userMapper;

    /*
    public UserRepositoryImpl(
            JpaUserRepository jpaUserRepository,
            UserMapper userMapper
    ) {
        this.jpaUserRepository = jpaUserRepository;
        this.userMapper = userMapper;
    }

     */

    @Override
    public Optional<User> findByEmail(String email) {
        return jpaUserRepository.findByEmail(email)
                .map(userMapper::entityToDomain);
    }

    @Override
    public User save(User user) {
        UserEntity userEntity = userMapper.domainToEntity(user);
        UserEntity savedUserEntity = jpaUserRepository.save(userEntity);

        return userMapper.entityToDomain(savedUserEntity);
    }
}
