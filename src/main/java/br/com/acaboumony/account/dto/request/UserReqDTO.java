package br.com.acaboumony.account.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserReqDTO(@NotBlank String name,
                         @NotBlank @Size(min = 11, max = 11) String cpf,
                         @NotBlank String password,
                         @NotBlank String contact,
                         @NotBlank @Email String email) {
}
