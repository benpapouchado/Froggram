package backend.user;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import backend.validation.Validation;

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
    public ResponseEntity<Task> createAccount(@Valid @RequestBody CreateAccountRequest request) {

        // Validation.isValidTitle(request.title().trim());
        // Validation.isValidDescription(request.description().trim());
        // Validation.isValidStatus(request.status().trim());
        // Validation.isValidDueDate(request.dueDate());
        // Validation.isFutureDate(request.dueDate());

        return ResponseEntity.status(HttpStatus.CREATED).body(repository.save(new User(
                request.username().trim(),
                request.password_hash().trim(),
                request.bio().trim(),
                request.dateOfBirth(),
                LocalDateTime.now())));
    }


}
