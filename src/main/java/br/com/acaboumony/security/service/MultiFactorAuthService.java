package br.com.acaboumony.security.service;

import br.com.acaboumony.security.RabbitMQ.producer.SecurityProducer;
import br.com.acaboumony.security.dto.MultiFactorDTO;
import br.com.acaboumony.security.model.MultiFactorAuth;
import br.com.acaboumony.security.repository.MultiFactorAuthRepository;
import br.com.acaboumony.util.SecurityUtil;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

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

    public String verifyVerificationCode(MultiFactorDTO multiFactorDTO){
        try {
            MultiFactorAuth multiFactorAuth = mFARepository.findByCode(multiFactorDTO.verificationCode()).orElseThrow(NoSuchElementException::new);
            if (multiFactorDTO.userId().equals(multiFactorAuth.getUserId()) && !multiFactorAuth.getIsUsed()) {
                multiFactorAuth.setIsUsed(true);
                deleteVerificationCode(multiFactorAuth); //alterar para um processo automatizado fora da aplicação
                return "Código verificado";
            }
            throw new RuntimeException();
        }
        catch (RuntimeException e) {
            return "Código de verificação não encontrado ou já utilizado!";
        }
    }

    private void deleteVerificationCode(MultiFactorAuth multiFactorAuth) {
        mFARepository.delete(multiFactorAuth);
    }

}
