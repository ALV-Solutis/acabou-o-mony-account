package br.com.acaboumony.account.dto.request;

import javax.validation.constraints.NotNull;

import java.util.UUID;

public record UserUpdateDTO(@NotNull UUID userId,
                            String name,
                            String contact,
                            String email) {
}
