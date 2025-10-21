package com.example.bankcards.service;

import com.example.bankcards.dto.UserDto;
import com.example.bankcards.entity.Role;
import com.example.bankcards.entity.UserEntity;
import com.example.bankcards.exception.SearchException;
import com.example.bankcards.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService implements CRUDService<UserDto>{
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void create(UserDto userDto) {
        userRepository.save(mapToEntity(userDto));
    }

    @Override
    public UserDto getById(Long id) {
        UserEntity userEntity = userRepository.findById(id).orElseThrow(() ->
                new SearchException("Пользователь с id %d не найден".formatted(id)));
        return mapToDto(userEntity);
    }

    @Override
    public Collection<UserDto> getAll() {
        return userRepository.findAll()
                .stream()
                .map(UserService::mapToDto)
                .toList();
    }

    @Override
    public void update(Long id, UserDto userDto) {
        UserEntity userToUpdate = userRepository.findById(id).orElseThrow(() ->
                new SearchException("Пользователь с id %d не найден".formatted(id)));
        userToUpdate.setName(userDto.getName());
        userToUpdate.setLastname(userDto.getLastname());
        userToUpdate.setPhoneNumber(userDto.getPhoneNumber());
        userToUpdate.setEmail(userDto.getEmail());
        userToUpdate.setUsername(userDto.getUsername());
        if(userDto.getPassword() != null){userToUpdate.setPassword(passwordEncoder.encode(userDto.getPassword()));}
        userRepository.save(userToUpdate);
    }

    @Override
    public void delete(Long id) {
        userRepository.findById(id).orElseThrow(() ->
                new SearchException("Пользователь с id %d не найден".formatted(id)));
        userRepository.deleteById(id);
    }

    public UserEntity createUserAccount(UserEntity user, Role role){
        user.setRole(Collections.singletonList(role));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        role.setUser(user);
        return userRepository.saveAndFlush(user);
    }

    public UserEntity findByUsername(String username){
        return userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new SearchException("Пользователь с именем %s не найден".formatted(username)));
    }

    public boolean isUsernameExist(String username){
        return userRepository.findByUsername(username).isPresent();
    }

    // Mappers
    public static UserDto mapToDto(UserEntity userEntity) {
        return UserDto.builder()
                .id(userEntity.getId())
                .username(userEntity.getUsername())
                .password(userEntity.getPassword())
                .name(userEntity.getName())
                .lastname(userEntity.getLastname())
                .email(userEntity.getEmail())
                .phoneNumber(userEntity.getPhoneNumber())
                .cardDtoList(userEntity.getCardList().stream()
                        .map(CardService::mapToDto)
                        .toList())
                .role(userEntity.getRole().get(0).getAuthority().toString())
                .build();
    }

    public static UserEntity mapToEntity(UserDto userDto) {
        UserEntity userEntity = new UserEntity();
        userEntity.setName(userDto.getName());
        userEntity.setLastname(userDto.getLastname());
        userEntity.setEmail(userDto.getEmail());
        userEntity.setCardList(userDto.getCardDtoList().stream()
                .map(CardService::mapToEntity)
                .toList());
        userEntity.setPhoneNumber(userDto.getPhoneNumber());
        return userEntity;
    }
}
