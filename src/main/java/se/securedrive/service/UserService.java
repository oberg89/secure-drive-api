package se.securedrive.service;

import se.securedrive.model.User;
import se.securedrive.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    /**
     * Hämtar en användare via användarnamn.
     *
     * @param username användarnamnet
     * @return användaren
     * @throws ResponseStatusException om användaren inte hittas
     */
    public User getByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Användaren hittades inte"));
    }
}
