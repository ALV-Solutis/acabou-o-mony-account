package br.com.acaboumony.security.dto;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;

public record MultiFactorDTO(@Size(max = 6, min = 6) String verificationCode,
                             @NotNull UUID userId) {
}
