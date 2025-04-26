package edu.log.controllers;

import edu.log.dto.UserDTO;
import edu.log.models.user.User;
import edu.log.services.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(AuthController.class)
@Import(edu.log.config.TestSecurityConfig.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    void login_successful_returnsToken() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("test@example.com");
        userDTO.setPassword("password123");

        when(userService.validateLogin(userDTO.getEmail(), userDTO.getPassword())).thenReturn(true);
        when(userService.getTokenFromEmail(userDTO.getEmail())).thenReturn("mocked-token");

        mockMvc.perform(post("/api/auth/token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isOk())
                .andExpect(content().string("Token:mocked-token"));
    }

    @Test
    void login_invalidCredentials_returnsUnauthorized() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("test@example.com");
        userDTO.setPassword("password123");
        when(userService.validateLogin(userDTO.getEmail(), userDTO.getPassword())).thenReturn(false);

        mockMvc.perform(post("/api/auth/token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isUnauthorized());
    }

    @Test
void register_successful_returnsCreatedUser() throws Exception {
    UserDTO userDTO = new UserDTO();
    userDTO.setEmail("test@example.com");
    userDTO.setPassword("password123");

    User savedUser = new User(userDTO.getEmail(), "hashedPassword");

    when(userService.registerUser(userDTO.getEmail(), userDTO.getPassword())).thenReturn(savedUser);
    when(userService.createToken(savedUser))
            .thenReturn(new edu.log.models.user.Token(savedUser, "generated-token"));

    mockMvc.perform(post("/api/auth/create")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(userDTO)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.email").value("test@example.com"));  // ✅ FIXED EXPECTATION
}

    @Test
    void register_existingEmail_returnsConflict() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("test@example.com");
        userDTO.setPassword("password123");
        when(userService.registerUser(userDTO.getEmail(), userDTO.getPassword()))
                .thenThrow(new org.springframework.web.server.ResponseStatusException(
                        org.springframework.http.HttpStatus.CONFLICT, "Email already exists"));

        mockMvc.perform(post("/api/auth/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isConflict());
    }
}
