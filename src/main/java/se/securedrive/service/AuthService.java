package se.securedrive.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import se.securedrive.dto.AuthResponse;
import se.securedrive.dto.LoginRequest;
import se.securedrive.dto.RegisterRequest;
import se.securedrive.model.User;
import se.securedrive.repository.UserRepository;
import se.securedrive.security.JwtUtil;
import org.springframework.http.HttpStatus;

@Service
@RequiredArgsConstructor

public class AuthService  {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder passwordEncoder;

    /**
     * Registrerar en ny användare och lagrar lösenordet krypterat.
     *
     * @param registerRequest registreringsdata
     * @return autentiseringssvar med JWT-token
     */
    public AuthResponse register(RegisterRequest registerRequest) {
        if(userRepository.existsByUsername(registerRequest.getUsername())){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Användarnamnet upptaget");
        }
        User user = User.builder()
                .username(registerRequest.getUsername())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .build();

        userRepository.save(user);

        return new AuthResponse(jwtUtil.generateToken(user.getUsername()));
    }
    /**
     * Autentiserar en användare och returnerar en JWT-token om uppgifterna är giltiga.
     *
     * @param request inloggningsdata
     * @return autentiseringssvar med JWT-token
     */
    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Ogiltiga uppgifter"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Ogiltiga uppgifter");
        }

        return new AuthResponse(jwtUtil.generateToken(user.getUsername()));
    }
}
