package br.com.acaboumony.security.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class MultiFactorAuth {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID authId;

    private UUID userId;

    private String code;

    private Boolean isUsed;

    private LocalDateTime creationDate;

    public MultiFactorAuth(UUID userId, String code) {
        this.userId = userId;
        this.code = code;
        this.isUsed = false;
        this.creationDate = LocalDateTime.now();
    }
}
