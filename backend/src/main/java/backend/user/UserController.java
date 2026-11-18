
package backend.user;

import java.time.*;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import backend.user.auth.Auth;
import backend.user.auth.AuthRepository;
import backend.user.auth.JwtService;

@RestController
@CrossOrigin(origins = { "http://localhost:8081" })
@RequestMapping("/api/user")
@Validated
public class UserController {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository repository;
    private final AuthRepository authRepository;
    private final JwtService jwtService;

    public UserController(
            PasswordEncoder passwordEncoder,
            UserRepository repository,
            AuthRepository authRepository,
            JwtService jwtService) {

        this.passwordEncoder = passwordEncoder;
        this.repository = repository;
        this.authRepository = authRepository;
        this.jwtService = jwtService;
    }

    public record CreateAccountRequest(
            @NotBlank @Size(max = 20) String username,
            @NotBlank String password_hash,
            @Size(max = 50) String bio,
            @NotNull LocalDate dateOfBirth) {
    }

    public record Login(
            @NotBlank String username,
            @NotBlank String password_hash) {
    }

    public record UserResponse(
            @NotNull java.util.UUID userId,
            @NotBlank String username) {
    }

    public record LoginResponse(
            @NotBlank UserResponse user,
            @NotBlank String token) {
    }

    @GetMapping("/hello")
    public String hello() {
        return "Hello, World!";
    }

    @PostMapping("/create")
    public ResponseEntity<User> createAccount(@Valid @RequestBody CreateAccountRequest request) {

        String username = request.username().trim();

        List<User> existingUsers = repository.findByUsername(username);

        if (!existingUsers.isEmpty()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        User user = new User(
                username,
                passwordEncoder.encode(request.password_hash()),
                request.bio() == null ? null : request.bio().trim(),
                request.dateOfBirth(),
                LocalDateTime.now());

        User savedUser = repository.save(user);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody Login creds) {

        List<User> users = repository.findByUsername(
                creds.username().trim());

        for (User user : users) {

            if (passwordEncoder.matches(creds.password_hash(), user.getPasswordHash())) {

                String token = jwtService.generateToken(user.getUserId().toString());

                LocalDateTime now = LocalDateTime.now();

                Auth auth = new Auth(
                        user.getUserId(),
                        jwtService.hashToken(token),
                        now,
                        now.plusHours(1));

                authRepository.save(auth);

                return ResponseEntity
                        .status(HttpStatus.ACCEPTED)
                        .body(new LoginResponse(
                                new UserResponse(
                                        user.getUserId(),
                                        user.getUsername()),
                                token));
            }
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
