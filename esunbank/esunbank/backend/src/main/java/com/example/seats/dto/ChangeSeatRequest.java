package com.example.seats.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record ChangeSeatRequest(
        @NotBlank @Pattern(regexp = "^[0-9]{5}$", message = "員編必須為5碼數字") String empId,
        @NotNull Integer newSeatSeq
) {}
