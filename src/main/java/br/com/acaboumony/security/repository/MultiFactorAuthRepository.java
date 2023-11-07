package br.com.acaboumony.security.repository;

import br.com.acaboumony.security.model.MultiFactorAuth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MultiFactorAuthRepository extends JpaRepository<MultiFactorAuth, Long> {

    Optional<MultiFactorAuth> findByCode(String code);
}
