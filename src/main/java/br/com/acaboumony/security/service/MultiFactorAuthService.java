package br.com.acaboumony.security.service;

import br.com.acaboumony.security.model.MultiFactorAuth;
import br.com.acaboumony.security.repository.MultiFactorAuthRepository;
import br.com.acaboumony.util.SecurityUtil;
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

    public String generateConfirmationCode(UUID userId){
        String code = SecurityUtil.generateConfirmationCode();
        MultiFactorAuth multiFactorAuth;
        return null;
    }

    public String verifyVerificationCode(String verificationCode){

        return null;
    }

    public String verifyConfirmationCode(String confirmationCode){

        return null;
    }

}
