/*
 * AuthController.java
 * 
 * This controller handles authentication-related requests, including user login and registration.
 * It uses the UserService to validate user credentials and create tokens for authenticated users.
 */

 package edu.log.controllers;

 import edu.log.models.user.User;
 import edu.log.services.user.UserService;
 import edu.log.dto.UserDTO;
 import org.springframework.web.bind.annotation.*;
 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.http.HttpStatus;
 import org.springframework.http.ResponseEntity;
 import org.springframework.web.server.ResponseStatusException;
 
 @RestController
 @RequestMapping("/api/auth")
 public class AuthController {
 
     @Autowired
     private UserService us;
 
     @PostMapping("/token")
     public ResponseEntity<String> login(@RequestBody UserDTO user) {
         if (user == null) {
             throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User data must not be null.");
         }
 
         String email = user.getEmail();
         String password = user.getPassword();
 
         if (email == null || email.isEmpty() || password == null || password.isEmpty()) {
             throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email and password must not be empty.");
         }
 
         boolean isAuthenticated = us.validateLogin(email, password);
         if (!isAuthenticated) {
             throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password. Please sign up.");
         }
 
         String token = us.getTokenFromEmail(email);
         return ResponseEntity.ok("Token:" + token);
     }
 
     @PostMapping("/create")
     public ResponseEntity<User> register(@RequestBody UserDTO user) {
         if (user == null) {
             throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User data must not be null.");
         }
         
 
         String email = user.getEmail();
         String password = user.getPassword();
         if (!email.matches("^[\\w-.]+@[\\w-]+\\.[a-zA-Z]{2,}$")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email format is invalid.");
        }
         if (email == null || email.isEmpty() || password == null || password.isEmpty()) {
             throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email and password must not be empty.");
         }
 
         // Register user or throw conflict if email already exists
         User user_ = us.registerUser(email, password);
 
         // Create token for user
         String tokenValue = us.createToken(user_).getTokenValue();
         if (tokenValue == null || tokenValue.isEmpty()) {
             throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to create token for the user.");
         }
 
         return ResponseEntity.status(HttpStatus.CREATED).body(user_);
     }
 }
 