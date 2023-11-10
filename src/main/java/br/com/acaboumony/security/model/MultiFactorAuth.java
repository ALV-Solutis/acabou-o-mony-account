package br.com.acaboumony.security.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;


@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Entity
public class MultiFactorAuth {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID authId;

    private UUID userId;

    private String email;

    private String code;

    private Boolean isUsed;

    private LocalDateTime creationDate;

    public MultiFactorAuth(UUID userId, String code, String email) {
        this.userId = userId;
        this.code = code;
        this.email = email;
        this.isUsed = false;
        this.creationDate = LocalDateTime.now();
    }
}
