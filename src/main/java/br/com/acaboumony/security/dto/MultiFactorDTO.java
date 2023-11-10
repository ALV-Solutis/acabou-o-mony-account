package br.com.acaboumony.security.dto;


import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.UUID;

public record MultiFactorDTO(@Size(max = 6, min = 6) String verificationCode,
                             @NotNull UUID userId) {
}
