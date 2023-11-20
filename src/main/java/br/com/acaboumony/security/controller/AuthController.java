package br.com.acaboumony.security.controller;

import br.com.acaboumony.security.service.MultiFactorAuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final MultiFactorAuthService mFAService;

    public AuthController(MultiFactorAuthService mFAService) {
        this.mFAService = mFAService;
    }

    @Operation(
            summary = "Gerar um código de autenticação de 2 fatores e enviar email",
            description = "Gera um código de 6 digitos que é enviado ao email fornecido no cabeçalho. Necessário " +
                    "ter o código OTP gerado pelo login e o email vinculado ao OTP."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", content = {@Content(schema = @Schema(defaultValue = "Código enviado!"))}),
            @ApiResponse(responseCode = "400", content = {@Content(schema = @Schema)}, description = "Bad Request"),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema)}, description = "Not Found"),
            @ApiResponse(responseCode = "406", content = {@Content(schema = @Schema)}, description = "Not Acceptable"),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema) }, description = "Internal Server Error")
    })
    @PostMapping("/generate")
    public ResponseEntity<?> generateConfirmationCode(@RequestHeader String email, @RequestHeader String otpCode){
        return ResponseEntity.status(HttpStatus.CREATED).body(mFAService.generateVerificationCode(email, otpCode));
    }

    @Operation(
            summary = "Realizar a verificação do código de autenticação de 2 fatores",
            description = "Realiza a verificação e confere se o código OTP e o de autenticação de 2 fatores são do mesmo usuário. " +
                    "A resposta é o token JWT usado para ter acesso às demais requisições."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(defaultValue = "Token JWT"))}),
            @ApiResponse(responseCode = "400", content = {@Content(schema = @Schema)}, description = "Bad Request"),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema)}, description = "Not Found"),
            @ApiResponse(responseCode = "406", content = {@Content(schema = @Schema)}, description = "Not Acceptable"),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema) }, description = "Internal Server Error")
    })
    @PostMapping("/confirmation")
    public ResponseEntity<?> verifyCode(@RequestHeader String code, @RequestHeader String otpCode){
        return ResponseEntity.ok().body(mFAService.verifyVerificationCode(code, otpCode));
    }

}
