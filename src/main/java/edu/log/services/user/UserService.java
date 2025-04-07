/*
 * UserService.java
 * 
 * This service class handles user-related operations, such as registering a new user and creating a token for the user. It interacts with the UserRepository and TokenRepository to perform these operations.
 */

package edu.log.services.user;

import edu.log.models.user.*;
import edu.log.repositories.user.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository ur;

    @Autowired
    private TokenRepository tr;

    public User registerUser(String email, String password) {
        if (ur.existsByEmail(email)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists: " + email);
        }

        String hash = new BCryptPasswordEncoder().encode(password);
        User user = new User(email, hash);
        return ur.save(user);
    }

    public boolean validateLogin(String email, String password) {
        User user = ur.findByEmail(email).orElse(null);
        if (user == null)
            return false;
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.matches(password, user.getPassword());
    }

    public Token createToken(User user) {
        if (user == null)
            throw new RuntimeException("User cannot be null");

        // check if a token already exists for the user
        Token existingToken = tr.findByUser(user).orElse(null);
        if (existingToken != null)
            return existingToken;

        // Generate a new token value
        String tokenValue = UUID.randomUUID().toString();

        // Create a new token for the user
        Token token = new Token(user, tokenValue);

        // Save the token to the repository
        tr.save(token);

        return token;
    }

    public boolean isTokenValid(String tokenValue) {
        if (tokenValue == null || tokenValue.isEmpty())
            return false; // Invalid token value

        // Find the token by its value
        Optional<Token> optionalToken = tr.findByTokenValue(tokenValue);
        if (optionalToken.isEmpty())
            return false; // Token not found

        // If found, return true indicating the token is valid
        return true;
    }

    public String getEmailFromToken(String tokenValue) {
        if (tokenValue == null || tokenValue.isEmpty())
            return null; // Invalid token value

        // Find the token by its value
        Optional<Token> optionalToken = tr.findByTokenValue(tokenValue);
        if (optionalToken.isEmpty())
            return null; // Token not found

        // Return the email associated with the user of the found token
        Token token = optionalToken.get();
        User user = token.getUser();
        if (user != null)
            return user.getEmail();

        return null;
    }

    public String getTokenFromEmail(String email) {

        if (email == null || email.isEmpty())
            return null; // Invalid email

        // Find the user by email
        Optional<User> optionalUser = ur.findByEmail(email);
        if (optionalUser.isEmpty())
            return null; // User not found

        User user = optionalUser.get();

        // Create a token for the user (or retrieve existing one)
        Token token = createToken(user);

        // Return the token value
        return token.getTokenValue();
    }
}
