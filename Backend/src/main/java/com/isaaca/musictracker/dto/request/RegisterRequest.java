package com.isaaca.musictracker.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank
        String username,

        @NotBlank
        @Size(min = 8)
        String password
) {
}
