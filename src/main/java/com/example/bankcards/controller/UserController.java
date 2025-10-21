package com.example.bankcards.controller;

import com.example.bankcards.dto.ErrorDto;
import com.example.bankcards.dto.UserDto;
import com.example.bankcards.entity.UserEntity;
import com.example.bankcards.exception.SearchException;
import com.example.bankcards.security.AppUserPrincipal;
import com.example.bankcards.security.UserDetailsServiceImpl;
import com.example.bankcards.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authorization.method.AuthorizeReturnObject;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/users")
public class UserController {
    private final UserService userService;
    private final UserDetailsServiceImpl userDetailsService;

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Collection<UserDto> getAllUsers(){
        return userService.getAll().stream().toList();
    }

    @GetMapping("/{username}")
    @PreAuthorize("#username == principal.username")
    public UserDto getUser(@PathVariable String username) throws SearchException {
        return UserService.mapToDto(userService.findByUsername(username));
    }

    @GetMapping("/delete/{username}")
    @PreAuthorize("#username == principal.username")
    public void deleteUser(@PathVariable String username) throws SearchException {
        UserEntity userEntity = userService.findByUsername(username);
        userService.delete(userEntity.getId());
    }

    @PutMapping("/update/{username}")
    @ResponseStatus(value = HttpStatus.OK)
    @PreAuthorize("#username == principal.username")
    public UserDto updateUser(@PathVariable String username, @RequestBody UserDto userDto) throws SearchException {
        log.info("UserDto change to:\n" + userDto.toString());
        UserEntity userEntity = userService.findByUsername(username);
        userService.update(userEntity.getId(), userDto);
        return userService.getById(userEntity.getId());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorDto> handleUserException(SearchException ex){
        ErrorDto errorDto = new ErrorDto();
        errorDto.setError(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDto);
    }
}
