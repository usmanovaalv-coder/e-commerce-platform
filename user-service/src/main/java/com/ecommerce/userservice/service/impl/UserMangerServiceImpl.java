package com.ecommerce.userservice.service.impl;

import com.ecommerce.userservice.dto.UserDto;
import com.ecommerce.userservice.mapper.UserMapper;
import com.ecommerce.userservice.params.UserParam;
import com.ecommerce.userservice.repository.UserRepository;
import com.ecommerce.userservice.service.UserManagerService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserMangerServiceImpl implements UserManagerService {

    UserMapper userMapper;
    UserRepository userRepository;

    @Override
    public UserDto getByUsername(String username) {
        return userRepository.findByUserName(username)
                .map(userMapper::toUserDto)
                .orElseThrow(() -> new UsernameNotFoundException("User with username '" + username + "' not found"));
    }

    @Override
    public List<UserDto> getUsers(int limit, int offset) {
        Pageable pageable = PageRequest.of(offset / limit, limit);
        return userRepository.findAll(pageable).stream()
                .map(userMapper::toUserDto)
                .toList();
    }

    @Override
    public UserDto updateUser(long id, UserParam userParam) {
        return userRepository.findById(id)
                .map(existing -> userMapper.toUserEntity(existing, userParam))
                .map(updatedUser -> {
                    UserDto dto = userMapper.toUserDto(userRepository.save(updatedUser));
                    log.info("User with id = {} has been updated successfully", id);
                    return dto;
                })
                .orElseThrow(() ->  new UsernameNotFoundException("User with id '" + id + "' not found"));
    }

    @Override
    public void deleteUser(long id) {
        userRepository.deleteById(id);
        log.info("User with id = {} has been deleted", id);
    }
}
