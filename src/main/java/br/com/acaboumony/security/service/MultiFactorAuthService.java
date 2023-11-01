package br.com.acaboumony.security.service;

import br.com.acaboumony.security.repository.MultiFactorAuthRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class MultiFactorAuthService {

    private final MultiFactorAuthRepository mFARepository;

    public MultiFactorAuthService(MultiFactorAuthRepository mFARepository) {
        this.mFARepository = mFARepository;
    }

    public String generateVerificationCode(UUID userId){

        return null;
    }

    public String generateConfirmationCode(String confirmationCode){

        return null;
    }

    public String verifyVerificationCode(String verificationCode){

        return null;
    }

    public String verifyConfirmationCode(String confirmationCode){

        return null;
    }

}
