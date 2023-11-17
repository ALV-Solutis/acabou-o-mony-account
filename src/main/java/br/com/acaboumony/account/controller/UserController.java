package br.com.acaboumony.account.controller;

import br.com.acaboumony.account.dto.request.UserReqDTO;
import br.com.acaboumony.account.dto.request.UserUpdateDTO;
import br.com.acaboumony.account.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> createUser(@RequestBody UserReqDTO userReqDTO) {
        userService.createUser(userReqDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body("Usuário " + userReqDTO.getName() + " criado!");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestHeader String email, @RequestHeader String password) {
        return ResponseEntity.ok().body(userService.login(email, password));
    }

    @PostMapping("/verify2FA")
    public ResponseEntity<?> verify2FA(@RequestHeader String email, @RequestHeader String password) {
        return ResponseEntity.ok().body(userService);
    }

    @GetMapping
    public ResponseEntity<?> listUsers() {
        return ResponseEntity.ok().body(userService.listUsers());
    }

    @GetMapping("/detail")
    public ResponseEntity<?> detailUser(@RequestHeader UUID userId) {
        return ResponseEntity.ok().body(userService.detailUser(userId));
    }

    @PutMapping
    public ResponseEntity<?> updateUser(@RequestBody UserUpdateDTO userUpdateDTO) {
        userService.updateUser(userUpdateDTO);
        return ResponseEntity.ok().body("Usuário atualizado!");
    }
}
