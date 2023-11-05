package ru.practicum.shareit.users.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class UserDto {
    @NotNull
    @NotEmpty
    private String name;
    @NotNull
    @NotEmpty
    @Email
    private String email;
}
