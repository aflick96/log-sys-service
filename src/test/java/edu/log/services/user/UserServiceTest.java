package edu.log.services.user;

import edu.log.models.user.Token;
import edu.log.models.user.User;
import edu.log.repositories.user.TokenRepository;
import edu.log.repositories.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import org.mockito.MockitoAnnotations;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private TokenRepository tokenRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void registerUser_successful() {
        String email = "newuser@example.com";
        String password = "password123";

        when(userRepository.existsByEmail(email)).thenReturn(false);
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));

        User user = userService.registerUser(email, password);

        assertEquals(email, user.getEmail());
        assertNotNull(user.getPassword());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void registerUser_emailAlreadyExists_throwsConflict() {
        String email = "existing@example.com";
        String password = "password123";

        when(userRepository.existsByEmail(email)).thenReturn(true);

        assertThrows(org.springframework.web.server.ResponseStatusException.class, () -> {
            userService.registerUser(email, password);
        });
    }

    @Test
    void validateLogin_successful() {
        String email = "user@example.com";
        String rawPassword = "password123";
        String encodedPassword = new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder()
                .encode(rawPassword);

        User user = new User(email, encodedPassword);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        assertTrue(userService.validateLogin(email, rawPassword));
    }

    @Test
    void validateLogin_invalidPassword() {
        String email = "user@example.com";
        String encodedPassword = new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder()
                .encode("otherpassword");

        User user = new User(email, encodedPassword);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        assertFalse(userService.validateLogin(email, "wrongpassword"));
    }

    @Test
    void createToken_newTokenCreated() {
        User user = new User("test@example.com", "password");

        when(tokenRepository.findByUser(user)).thenReturn(Optional.empty());
        when(tokenRepository.save(any(Token.class))).thenAnswer(i -> i.getArgument(0));

        Token token = userService.createToken(user);

        assertNotNull(token);
        assertEquals(user, token.getUser());
    }

    @Test
    void isTokenValid_tokenExists() {
        String tokenValue = UUID.randomUUID().toString();

        when(tokenRepository.findByTokenValue(tokenValue)).thenReturn(Optional.of(new Token()));

        assertTrue(userService.isTokenValid(tokenValue));
    }

    @Test
    void isTokenValid_tokenDoesNotExist() {
        String tokenValue = UUID.randomUUID().toString();

        when(tokenRepository.findByTokenValue(tokenValue)).thenReturn(Optional.empty());

        assertFalse(userService.isTokenValid(tokenValue));
    }

    @Test
    void getEmailFromToken_success() {
        User user = new User("user@example.com", "password");
        Token token = new Token(user, "tokenValue");

        when(tokenRepository.findByTokenValue("tokenValue")).thenReturn(Optional.of(token));

        String email = userService.getEmailFromToken("tokenValue");

        assertEquals("user@example.com", email);
    }

    @Test
    void getEmailFromToken_invalidToken_returnsNull() {
        when(tokenRepository.findByTokenValue("invalidToken")).thenReturn(Optional.empty());

        assertNull(userService.getEmailFromToken("invalidToken"));
    }
}
