package ru.practicum.shareit.request.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class RequestDto {
    @NotNull
    @NotEmpty
    private String description;
}