package br.com.acaboumony.account.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
public class UserAuth {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private UUID userId;

    private String otpCode;

    private Boolean isUsed;

    public UserAuth(UUID userId, String otpCode, Boolean isUsed) {
        this.userId = userId;
        this.otpCode = otpCode;
        this.isUsed = isUsed;
    }
}
