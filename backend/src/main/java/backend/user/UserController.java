package backend.user;

import java.time.*;
import java.util.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import backend.user.auth.JwtService;

import org.springframework.validation.annotation.Validated;

@RestController
@CrossOrigin(origins = { "http://localhost:8081" })
@RequestMapping("/api/user")
@Validated
public class UserController {

    private final UserRepository repository;
    private final JwtService jwtService;

    public record CreateAccountRequest(
            @NotBlank @Size(max = 50) String username,
            @NotBlank String password_hash,
            @Size(max = 50) String bio,
            LocalDate dateOfBirth,
            LocalDateTime createdAt) {
    }

        public record Login(
            String username,
            String password_hash) {
    }

    public record LoginResponse(String token) {}

    public UserController(UserRepository repository, JwtService jwtService) {
        this.repository = repository;
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
                request.password_hash().trim(),
                request.bio().trim(),
                request.dateOfBirth(),
                LocalDateTime.now())));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody Login creds) {

        List<User> users = repository.findByUsername(creds.username());

        for (User user : users) {
            if (user.getUsername().equals(creds.username()) && 
            user.getPasswordHash().equals(creds.password_hash())) {
            
                return ResponseEntity.status(HttpStatus.ACCEPTED).body(
                new LoginResponse(jwtService.generateToken(creds.username())));
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    //     @PostMapping("/login")
    // public String login(@RequestParam String username) {

    //     // Demo only: authenticate username/password
    //     // before generating a token in production.

    //     return jwtService.generateToken(username);
    // }
}

// curl -i -X POST http://localhost:8080/api/user/login -H "Content-Type: application/json" -d '{
//     "username": "unique",
//     "password_hash": "birb"
// }'

