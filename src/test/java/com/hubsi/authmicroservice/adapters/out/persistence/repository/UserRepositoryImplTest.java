package com.hubsi.authmicroservice.adapters.out.persistence.repository;

import com.hubsi.authmicroservice.adapters.out.persistence.entities.UserEntity;
import com.hubsi.authmicroservice.adapters.out.persistence.repository.impl.UserRepositoryImpl;
import com.hubsi.authmicroservice.domain.user.User;
import com.hubsi.authmicroservice.utils.mappers.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserRepositoryImplTest {

    private JpaUserRepository jpaUserRepository;
    private UserMapper userMapper;
    private UserRepositoryImpl userRepositoryImpl;

    @BeforeEach
    void setUp() {
        jpaUserRepository = mock(JpaUserRepository.class);
        userMapper = mock(UserMapper.class);
        userRepositoryImpl = new UserRepositoryImpl(jpaUserRepository, userMapper);
    }

    @Test
    void testFindByEmail_UserExists() {
        String email = "test@email.com";
        UserEntity userEntity = new UserEntity();
        User user = new User();

        when(jpaUserRepository.findByEmail(email)).thenReturn(Optional.of(userEntity));
        when(userMapper.entityToDomain(userEntity)).thenReturn(user);

        Optional<User> result = userRepositoryImpl.findByEmail(email);

        assertTrue(result.isPresent());
        assertEquals(user, result.get());
        verify(jpaUserRepository).findByEmail(email);
        verify(userMapper).entityToDomain(userEntity);
    }

    @Test
    void testFindByEmail_UserNotFound() {
        String email = "notfound@email.com";
        when(jpaUserRepository.findByEmail(email)).thenReturn(Optional.empty());

        Optional<User> result = userRepositoryImpl.findByEmail(email);

        assertFalse(result.isPresent());
        verify(jpaUserRepository).findByEmail(email);
        verifyNoInteractions(userMapper);
    }

    @Test
    void testSave() {
        User user = new User();
        UserEntity userEntity = new UserEntity();
        UserEntity savedUserEntity = new UserEntity();
        User savedUser = new User();

        when(userMapper.domainToEntity(user)).thenReturn(userEntity);
        when(jpaUserRepository.save(userEntity)).thenReturn(savedUserEntity);
        when(userMapper.entityToDomain(savedUserEntity)).thenReturn(savedUser);

        User result = userRepositoryImpl.save(user);

        assertEquals(savedUser, result);
        verify(userMapper).domainToEntity(user);
        verify(jpaUserRepository).save(userEntity);
        verify(userMapper).entityToDomain(savedUserEntity);
    }
}
