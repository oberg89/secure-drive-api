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
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**
     * Registers a new user and stores the password encrypted
     *
     * @param registerRequest registration data
     * @return authentication response containing JWT token
     */
    public AuthResponse register(RegisterRequest registerRequest) {
        if(userRepository.existsByUsername(registerRequest.getUsername())){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already exists");
        }
        User user = User.builder()
                .username(registerRequest.getUsername())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .build();

        userRepository.save(user);

        return new AuthResponse(jwtUtil.generateToken(user.getUsername()));
    }
    /**
     * Authenticates a user and returns a JWT token if credentials are valid.
     *
     * @param request login data
     * @return authentication response containing JWT token
     */
    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }

        return new AuthResponse(jwtUtil.generateToken(user.getUsername()));
    }
}
