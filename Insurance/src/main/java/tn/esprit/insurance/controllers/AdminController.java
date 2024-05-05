package tn.esprit.insurance.controllers;
import tn.esprit.insurance.entities.StatusU;
import tn.esprit.insurance.entities.User;
import tn.esprit.insurance.repositories.TokenRepository;
import tn.esprit.insurance.repositories.UserRepository;
import tn.esprit.insurance.services.LogoutService;
import tn.esprit.insurance.services.UserService;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private LogoutService logoutService;


    public AdminController(UserRepository userRepository, TokenRepository tokenRepository) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
    }

    @GetMapping("/listUser")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userRepository.findAll();
        return ResponseEntity.ok(users);
    }
    @PostMapping
    @PreAuthorize("hasAuthority('admin:create')")
    @Hidden
    public String post() {
        return "POST:: admin controller";
    }
    @PutMapping
    @PreAuthorize("hasAuthority('admin:update')")
    @Hidden
    public String put() {
        return "PUT:: admin controller";
    }
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('admin:delete')")
    @Hidden
    public ResponseEntity<String> delete(@PathVariable("id") int id) {
        try {
            User user = userRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("User with ID " + id + " not found"));

            tokenRepository.deleteByUserId(user.getId());
            userRepository.delete(user);

            return ResponseEntity.ok("User with ID: " + id + " deleted successfully");
        } catch(EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error deleting user with ID: " + id + ". Please try again later.");
        }
    }

    @PostMapping("/ban/{id}")
    @PreAuthorize("hasAuthority('admin:ban')")
    public ResponseEntity<String> banUser(@PathVariable("id") int userId) {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new EntityNotFoundException("User with ID " + userId + " not found"));
            user.setStatus(StatusU.Bloque); // Update user status to "bloqué"
            userRepository.save(user); // Save the updated user entity
            return ResponseEntity.ok("User with ID: " + userId + " has been banned successfully");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error banning user with ID: " + userId + ". Please try again later.");
        }
    }

    @PostMapping("/unban/{id}")
    @PreAuthorize("hasAuthority('admin:unban')")
    public ResponseEntity<String> unbanUser(@PathVariable("id") int userId) {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new EntityNotFoundException("User with ID " + userId + " not found"));
            user.setStatus(StatusU.Actif); // Update user status to "Actif"
            userRepository.save(user); // Save the updated user entity
            return ResponseEntity.ok("User with ID: " + userId + " has been unbanned successfully");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error unbanning user with ID: " + userId + ". Please try again later.");
        }
    }

    @RequestMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        logoutService.logout(request, null, null);
        String message = "You have been logged out successfully!";
        return ResponseEntity.ok().body(message);
    }

}

