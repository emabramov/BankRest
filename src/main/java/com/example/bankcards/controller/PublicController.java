package com.example.bankcards.controller;

import com.example.bankcards.dto.ErrorDto;
import com.example.bankcards.dto.UserDto;
import com.example.bankcards.entity.Role;
import com.example.bankcards.entity.RoleType;
import com.example.bankcards.entity.UserEntity;
import com.example.bankcards.exception.SearchException;
import com.example.bankcards.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/public")
@RequiredArgsConstructor
@Slf4j
public class PublicController {
    private final UserService userService;

    @PostMapping("/create")
    public ResponseEntity<UserDto> createUserAccount(@RequestBody UserDto userDto) throws SearchException{
        if(userService.isUsernameExist(userDto.getUsername())){
            throw new SearchException("Имя пользователя %s занято".formatted(userDto.getUsername()));
        }
        RoleType roleType = RoleType.ROLE_USER;
        return ResponseEntity.status(HttpStatus.CREATED)
                    .body(createAccount(userDto, roleType));
    }

    private UserDto createAccount(UserDto userDto, RoleType roleType) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(userDto.getUsername());
        userEntity.setPassword(userDto.getPassword());
        UserEntity createdUser = userService.createUserAccount(userEntity, Role.from(roleType));

        return UserDto.builder()
                .id(createdUser.getId())
                .username(createdUser.getUsername())
                .password(createdUser.getPassword())
                .build();
    }

    @ExceptionHandler(SearchException.class)
    public ResponseEntity<ErrorDto> handleRuntimeException(SearchException ex){
        ErrorDto errorDto = new ErrorDto(ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorDto);
    }
}
