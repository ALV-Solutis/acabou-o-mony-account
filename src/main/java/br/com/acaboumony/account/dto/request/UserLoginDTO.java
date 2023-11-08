package br.com.acaboumony.account.dto.request;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserLoginDTO(@NotBlank @Email String email,
                           @NotBlank String password) {


}
