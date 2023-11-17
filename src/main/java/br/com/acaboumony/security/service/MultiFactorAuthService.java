package br.com.acaboumony.security.service;

import br.com.acaboumony.account.service.UserService;
import br.com.acaboumony.security.RabbitMQ.producer.SecurityProducer;
import br.com.acaboumony.security.dto.MultiFactorDTO;
import br.com.acaboumony.security.dto.Token;
import br.com.acaboumony.security.model.MultiFactorAuth;
import br.com.acaboumony.security.repository.MultiFactorAuthRepository;
import br.com.acaboumony.util.SecurityUtil;
import jakarta.ws.rs.NotAuthorizedException;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class MultiFactorAuthService {

    private final MultiFactorAuthRepository mFARepository;
    private final SecurityProducer securityProducer;
    private final UserService userService;
    private final AuthService authService;

    public MultiFactorAuthService(MultiFactorAuthRepository mFARepository, SecurityProducer securityProducer, UserService userService, AuthService authService) {
        this.mFARepository = mFARepository;
        this.securityProducer = securityProducer;
        this.userService = userService;
        this.authService = authService;
    }

    @Transactional
    public String generateVerificationCode(UUID userId, String email, String otpCode){
        if (userService.validateOTP(otpCode)){
            String code = SecurityUtil.generateVerificationCode();
            MultiFactorAuth multiFactorAuth = new MultiFactorAuth(userId, code, email);
            mFARepository.save(multiFactorAuth);
            securityProducer.publishMessageEmail(multiFactorAuth);
            return "Código enviado!";
        }else {
            throw new NotAuthorizedException("otpCode invalido");
        }

    }

    public Token verifyVerificationCode(String code, UUID userId, String otpCode){
        if (userService.validateOTP(otpCode)) {
            MultiFactorDTO multiFactorDTO = new MultiFactorDTO(code, userId);
            try {
                MultiFactorAuth multiFactorAuth = mFARepository.findByCode(multiFactorDTO.verificationCode()).orElseThrow(NoSuchElementException::new);
                if (multiFactorDTO.userId().equals(multiFactorAuth.getUserId()) && !multiFactorAuth.getIsUsed()) {
                    multiFactorAuth.setIsUsed(true);
                    deleteVerificationCode(multiFactorAuth); //alterar para um processo automatizado fora da aplicação
                    return new Token(authService.doAuthentication(userId));
                }else {
                    throw new RuntimeException();
                }
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
        }
        throw new NotAuthorizedException("otpCode invalido");
    }

    private void deleteVerificationCode(MultiFactorAuth multiFactorAuth) {
        mFARepository.delete(multiFactorAuth);
    }

}
