package br.com.acaboumony.security.service;

import br.com.acaboumony.security.RabbitMQ.producer.SecurityProducer;
import br.com.acaboumony.security.dto.MultiFactorDTO;
import br.com.acaboumony.security.model.MultiFactorAuth;
import br.com.acaboumony.security.repository.MultiFactorAuthRepository;
import br.com.acaboumony.util.SecurityUtil;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class MultiFactorAuthService {

    private final MultiFactorAuthRepository mFARepository;
    private final SecurityProducer securityProducer;

    public MultiFactorAuthService(MultiFactorAuthRepository mFARepository, SecurityProducer securityProducer) {
        this.mFARepository = mFARepository;
        this.securityProducer = securityProducer;
    }

    @Transactional
    public String generateVerificationCode(UUID userId, String email){
        String code = SecurityUtil.generateVerificationCode();
        MultiFactorAuth multiFactorAuth = new MultiFactorAuth(userId, code, email);
        mFARepository.save(multiFactorAuth);
        securityProducer.publishMessageEmail(multiFactorAuth);
        return "Código enviado!";
    }

    public Boolean verifyVerificationCode(String code, UUID userId){
        MultiFactorDTO multiFactorDTO = new MultiFactorDTO(code, userId);
        try {
            MultiFactorAuth multiFactorAuth = mFARepository.findByCode(multiFactorDTO.verificationCode()).orElseThrow(NoSuchElementException::new);
            if (multiFactorDTO.userId().equals(multiFactorAuth.getUserId()) && !multiFactorAuth.getIsUsed()) {
                multiFactorAuth.setIsUsed(true);
                deleteVerificationCode(multiFactorAuth); //alterar para um processo automatizado fora da aplicação
                return true;
            }
            throw new RuntimeException();
        }
        catch (RuntimeException e) {
            return false;
        }
    }

    private void deleteVerificationCode(MultiFactorAuth multiFactorAuth) {
        mFARepository.delete(multiFactorAuth);
    }

}
