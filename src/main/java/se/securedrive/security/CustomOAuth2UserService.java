package se.securedrive.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import se.securedrive.model.User;
import se.securedrive.repository.UserRepository;

import java.util.Optional;

/**
 * Service för att hantera användarinformation som hämtas via OAuth2 (t.ex. GitHub).
 * Denna tjänst registrerar automatiskt nya användare i databasen om de inte redan finns.
 */
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // Hämta användarinformation från GitHub (eller annan provider)
        OAuth2User oauth2User = super.loadUser(userRequest);

        // Hämta GitHub-specifik information
        String githubId = oauth2User.getAttribute("id").toString();
        String login = oauth2User.getAttribute("login");
        String email = oauth2User.getAttribute("email");

        // Kontrollera om användaren redan finns i vår databas via GitHub ID
        Optional<User> userOptional = userRepository.findByGithubId(githubId);

        if (userOptional.isEmpty()) {
            // Skapa en ny användare om den inte finns
            User newUser = User.builder()
                    .githubId(githubId)
                    .username(login)
                    .email(email)
                    .build();
            userRepository.save(newUser);
        } else {
            // Uppdatera eventuellt existerande användare (t.ex. om de bytt username på GitHub)
            User existingUser = userOptional.get();
            if (login != null && !login.equals(existingUser.getUsername())) {
                existingUser.setUsername(login);
                userRepository.save(existingUser);
            }
        }

        return oauth2User;
    }
}
