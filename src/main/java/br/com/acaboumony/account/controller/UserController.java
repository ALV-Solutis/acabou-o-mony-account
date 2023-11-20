package br.com.acaboumony.account.controller;

import br.com.acaboumony.account.dto.request.UserReqDTO;
import br.com.acaboumony.account.dto.request.UserUpdateDTO;
import br.com.acaboumony.account.dto.response.UserResDTO;
import br.com.acaboumony.account.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "User", description = "API para gerenciamento e detalhamento de usuário")
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(
            summary = "Criar um novo usuário",
            description = "Cria um novo usuário verificando se o CPF e email não estão sendo utilizados.",
            method = "POST"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", content = {@Content(schema = @Schema(defaultValue = "Usuário teste criado com sucesso"))}),
            @ApiResponse(responseCode = "400", content = {@Content(schema = @Schema)}, description = "Bad Request"),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema)}, description = "Not Found"),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema) }, description = "Internal Server Error")})
    @PostMapping("/register")
    public ResponseEntity<?> createUser(@RequestBody UserReqDTO userReqDTO) {
        userService.createUser(userReqDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body("Usuário " + userReqDTO.getName() + " criado!");
    }

    @Operation(
            summary = "Realizar login",
            description = "Realiza o login caso as credenciais informadas estejam corretas. A resposta é um código OTP, vinculado ao email, gerado para realizar a autenticação de 2 fatores."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(defaultValue = "0000-0000-0000-0000-0000"))}),
            @ApiResponse(responseCode = "400", content = {@Content(schema = @Schema)}, description = "Bad Request"),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema)}, description = "Not Found"),
            @ApiResponse(responseCode = "406", content = {@Content(schema = @Schema)}, description = "Not Acceptable"),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema) }, description = "Internal Server Error") })
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestHeader String email, @RequestHeader String password) {
        return ResponseEntity.ok().body(userService.login(email, password));
    }

    @Operation(
            summary = "Listar todos os usuários",
            description = "Faz uma listagem completa dos usuários do banco."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = UserResDTO.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", content = {@Content(schema = @Schema)}, description = "Bad Request"),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema)}, description = "Not Found"),
            @ApiResponse(responseCode = "406", content = {@Content(schema = @Schema)}, description = "Not Acceptable"),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema) }, description = "Internal Server Error")
    })
    @GetMapping
    @SecurityRequirement(name = "bearer-key")
    public ResponseEntity<Page<?>> listUsers(Pageable pageable) {
        return ResponseEntity.ok().body(userService.listUsers(pageable));
    }

    @Operation(
            summary = "Detalhar os dados de um usuário pelo id",
            description = "Detalha os dados não sensiveis do usuário de acordo com o id informado."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = UserResDTO.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", content = {@Content(schema = @Schema)}, description = "Bad Request"),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema)}, description = "Not Found"),
            @ApiResponse(responseCode = "406", content = {@Content(schema = @Schema)}, description = "Not Acceptable"),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema) }, description = "Internal Server Error")
    })
    @GetMapping("/detail")
    @SecurityRequirement(name = "bearer-key")
    public ResponseEntity<?> detailUser(@RequestHeader UUID userId) {
        return ResponseEntity.ok().body(userService.detailUser(userId));
    }

    @Operation(
            summary = "Atualiza os dados de um usuário pelo id",
            description = "Atualiza o nome, contato ou email do usuário que foi fornecido o id."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(description = "string"))}),
            @ApiResponse(responseCode = "400", content = {@Content(schema = @Schema)}, description = "Bad Request"),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema)}, description = "Not Found"),
            @ApiResponse(responseCode = "406", content = {@Content(schema = @Schema)}, description = "Not Acceptable"),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema) }, description = "Internal Server Error")
    })
    @PutMapping
    @SecurityRequirement(name = "bearer-key")
    public ResponseEntity<?> updateUser(@RequestBody UserUpdateDTO userUpdateDTO) {
        userService.updateUser(userUpdateDTO);
        return ResponseEntity.ok().body("Usuário atualizado!");
    }
}
