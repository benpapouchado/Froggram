package backend.user;

import java.time.*;
import java.util.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import backend.user.auth.*;

import org.springframework.validation.annotation.Validated;

@RestController
@CrossOrigin(origins = { "http://localhost:8081" })
@RequestMapping("/api/user")
@Validated
public class UserController {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository repository;
    private final AuthRepository authRepository;
    private final JwtService jwtService;

    public record CreateAccountRequest(
            @NotBlank @Size(max = 50) String username,
            @NotBlank String password_hash,
            @Size(max = 50) String bio,
            LocalDate dateOfBirth,
            LocalDateTime createdAt) {
    }

    public record Login(
            @NotBlank String username,
            @NotBlank String password_hash) {
    }

    public record LoginResponse(
            @NotBlank UUID userId,
            @NotBlank String username,
            @NotBlank String token) {
    }

    public record Session(
            @NotBlank UUID userId,
            @NotBlank String tokenHash,
            @NotBlank LocalDateTime createdAt,
            @NotBlank LocalDateTime expiresAt) {
    }

    public UserController(PasswordEncoder passwordEncoder, UserRepository repository,
            AuthRepository authRepository, JwtService jwtService) {
        this.passwordEncoder = passwordEncoder;
        this.repository = repository;
        this.authRepository = authRepository;
        this.jwtService = jwtService;
    }

    @GetMapping("/hello")
    public String hello() {
        return "Hello, World!";
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<User> createAccount(@Valid @RequestBody CreateAccountRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED).body(repository.save(new User(
                request.username().trim(),
                passwordEncoder.encode(request.password_hash().trim()),
                request.bio().trim(),
                request.dateOfBirth(),
                LocalDateTime.now())));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody Login creds) {

        List<User> users = repository.findByUsername(creds.username());

        for (User user : users) {
            if (user.getUsername().equals(creds.username()) &&
                    passwordEncoder.matches(creds.password_hash(), user.getPasswordHash())) {
                String token = jwtService.generateToken(user.getUserId().toString());

                authRepository.save(
                        new Auth(user.getUserId(),
                                passwordEncoder.encode(token),
                                LocalDateTime.now(),
                                LocalDateTime.now().plusHours(1)));

                return ResponseEntity.status(HttpStatus.ACCEPTED).body(
                        new LoginResponse(user.getUserId(),
                                user.getUsername(),
                                token));
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}