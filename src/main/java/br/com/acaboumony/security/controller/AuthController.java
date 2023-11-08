package br.com.acaboumony.security.controller;

import br.com.acaboumony.security.dto.MultiFactorDTO;
import br.com.acaboumony.security.service.MultiFactorAuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final MultiFactorAuthService mFAService;

    public AuthController(MultiFactorAuthService mFAService) {
        this.mFAService = mFAService;
    }

//    public ResponseEntity<?> generateVerificationCode(@RequestHeader UUID userId){
//
//        return ResponseEntity.status(HttpStatus.CREATED).body(mFAService);
//    }

    @PostMapping("/generate")
    public ResponseEntity<?> generateConfirmationCode(@RequestHeader UUID userId,@RequestHeader String email){
        return ResponseEntity.ok().body(mFAService.generateVerificationCode(userId, email));
    }

    @PostMapping("/confirmation")
    public ResponseEntity<?> verifyCode(@RequestHeader String code, @RequestHeader UUID userId){
        return ResponseEntity.ok().body(mFAService.verifyVerificationCode(code, userId));
    }

//    @PostMapping("/confirmation")
//    public ResponseEntity<?> confirmAccount(@RequestParam String confirmationCode){
//
//        return ResponseEntity.ok().body(mFAService);
//    }
}
