package kz.handshop.service;

import kz.handshop.dto.request.LoginRequest;
import kz.handshop.dto.request.RegisterRequest;
import kz.handshop.entity.User;
import kz.handshop.entity.UserRole;
import kz.handshop.exception.EmailAlreadyExistsException;
import kz.handshop.repository.FreelancerProfileRepository;
import kz.handshop.repository.SubscriptionRepository;
import kz.handshop.repository.UserRepository;
import kz.handshop.security.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AuthService Unit Tests")
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private FreelancerProfileRepository freelancerProfileRepository;

    @Mock
    private SubscriptionRepository subscriptionRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    private AuthService authService;

    private RegisterRequest registerRequest;
    private User savedUser;
    private LoginRequest loginRequest;

    @BeforeEach
    void setUp() {
        registerRequest = new RegisterRequest();
        registerRequest.setEmail("user@example.com");
        registerRequest.setUsername("testuser");
        registerRequest.setPassword("password123");

        savedUser = new User("user@example.com", "testuser", "encodedPassword");
        savedUser.setId(1L);
        savedUser.setRole(UserRole.USER);

        loginRequest = new LoginRequest("user@example.com", "password123");
    }

    @Test
    @DisplayName("registerUser - success")
    void registerUser_success_returnsAuthResponse() {
        when(userRepository.existsByEmail(registerRequest.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenAnswer(inv -> {
            User u = inv.getArgument(0);
            u.setId(1L);
            return u;
        });
        when(jwtTokenProvider.generateTokenFromEmail(anyString())).thenReturn("jwt-token");

        var response = authService.registerUser(registerRequest);

        assertThat(response).isNotNull();
        assertThat(response.getEmail()).isEqualTo("user@example.com");
        assertThat(response.getUsername()).isEqualTo("testuser");
        assertThat(response.getRole()).isEqualTo("USER");
        assertThat(response.getToken()).isEqualTo("jwt-token");
        assertThat(response.getId()).isEqualTo(1L);

        verify(userRepository).existsByEmail("user@example.com");
        verify(userRepository).save(any(User.class));
        verify(jwtTokenProvider).generateTokenFromEmail("user@example.com");
    }

    @Test
    @DisplayName("registerUser - email already exists throws")
    void registerUser_emailExists_throwsEmailAlreadyExistsException() {
        when(userRepository.existsByEmail(registerRequest.getEmail())).thenReturn(true);

        assertThatThrownBy(() -> authService.registerUser(registerRequest))
                .isInstanceOf(EmailAlreadyExistsException.class)
                .hasMessageContaining("Email уже используется");

        verify(userRepository).existsByEmail("user@example.com");
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("login - success")
    void login_success_returnsAuthResponse() {
        Authentication auth = new UsernamePasswordAuthenticationToken("user@example.com", null);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(auth);
        when(jwtTokenProvider.generateToken(any(Authentication.class))).thenReturn("jwt-token");
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(savedUser));

        var response = authService.login(loginRequest);

        assertThat(response).isNotNull();
        assertThat(response.getEmail()).isEqualTo("user@example.com");
        assertThat(response.getUsername()).isEqualTo("testuser");
        assertThat(response.getRole()).isEqualTo("USER");
        assertThat(response.getToken()).isEqualTo("jwt-token");
        assertThat(response.getId()).isEqualTo(1L);

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userRepository).findByEmail("user@example.com");
    }

    @Test
    @DisplayName("login - user not found throws RuntimeException")
    void login_userNotFound_throwsRuntimeException() {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(new UsernamePasswordAuthenticationToken("user@example.com", null));
        when(jwtTokenProvider.generateToken(any(Authentication.class))).thenReturn("jwt-token");
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> authService.login(loginRequest))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("User not found");

        verify(userRepository).findByEmail("user@example.com");
    }

    @Test
    @DisplayName("login - bad credentials throws BadCredentialsException")
    void login_badCredentials_throwsBadCredentialsException() {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        assertThatThrownBy(() -> authService.login(loginRequest))
                .isInstanceOf(BadCredentialsException.class)
                .hasMessageContaining("Bad credentials");

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userRepository, never()).findByEmail(anyString());
    }
}
