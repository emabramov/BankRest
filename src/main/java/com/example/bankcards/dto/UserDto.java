package com.example.bankcards.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Long id;
    private String name;
    private String lastname;
    private Long phoneNumber;
    private String email;
    private String username;
    private String password;
    private String role;
    private List<CardDto> cardDtoList;
}
