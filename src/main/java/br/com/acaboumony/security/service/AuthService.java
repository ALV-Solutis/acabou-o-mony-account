package br.com.acaboumony.security.service;

import br.com.acaboumony.account.model.Users;
import br.com.acaboumony.account.repository.UserRepository;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import jakarta.servlet.ServletException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public String attemptAuthentication(String email, String senha) {
        Users user = userRepository.findByEmail(email).orElseThrow(NoSuchElementException::new);
        String token = "";
        try {
            token = successfulAuthentication(user);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return passwordEncoder.matches(senha, user.getPassword()) ? token : "Não autorizado";
    }

    protected String successfulAuthentication(Users users) throws IOException, ServletException {
        Algorithm algorithm = Algorithm.HMAC256("ALV-squad2-Solutis".getBytes());

        String accessToken = JWT.create()
                .withSubject(users.getEmail())
                .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60000))
                .sign(algorithm);

        Map<String, String> token = new HashMap<>();
        token.put("ALV-squad2-Solutis", accessToken);
        return accessToken;
    }
}
