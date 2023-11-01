package br.com.acaboumony.account.dto.response;

import java.util.UUID;

public record UserResDTO(UUID userId, String name, String cpf, String password, String contact, String email) {
}
