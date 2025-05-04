/*
 * UserDTO.java
 * 
 * This DTO represents a user request object, typically used for user authentication or registration. It contains fields for the user's email and password, along with appropriate getters and setters.
 */

package edu.log.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "User request object")
public class UserDTO {

    @Schema(description = "User email", example = "johndoe@jh.edu")
    private String email;

    @Schema(description = "User password", example = "password123")
    private String password;
    
    // Constructors
    public UserDTO() {}
    public UserDTO(String email, String password) {
        this.email = email;
        this.password = password;
    }

    // Getters and Setters
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    @Override
    public String toString() {
        return "UserDTO{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
