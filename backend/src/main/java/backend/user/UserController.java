package backend.user;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.validation.annotation.Validated;

@RestController
@CrossOrigin(origins = { "http://localhost:8081" })
@RequestMapping("/api/user")
@Validated
public class UserController {

    private final UserRepository repository;

    public record CreateAccountRequest(
            @NotBlank @Size(max = 50) String username,
            @NotBlank String password_hash,
            @Size(max = 50) String bio,
            LocalDate dateOfBirth,
            LocalDateTime createdAt) {
    }

    public UserController(UserRepository repository) {
        this.repository = repository;
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


}
