package br.com.acaboumony.account.repository;

import br.com.acaboumony.account.model.UserAuth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserAuthRepository extends JpaRepository<UserAuth, Long> {

    UserAuth findUserAuthByUserIdAndIsUsedIsFalse(UUID userId);

    UserAuth findUserAuthByOtpCodeAndIsUsedIsFalse(String otpCode);

}
