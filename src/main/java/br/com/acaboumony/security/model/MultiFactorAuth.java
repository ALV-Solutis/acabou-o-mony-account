package br.com.acaboumony.security.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MultiFactorAuth {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long authId;

    private UUID userId;

    private String code;

    private Boolean isUsed;

    private Timestamp creationDate;
}
