package br.com.acaboumony.account.dto.request;

import java.util.UUID;

public record UserUpdateDTO(UUID userId,
                            String name,
                            String contact,
                            String email) {
}
