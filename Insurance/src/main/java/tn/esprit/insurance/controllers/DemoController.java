package tn.esprit.insurance.controllers;


import tn.esprit.insurance.entities.StatusU;
import tn.esprit.insurance.entities.User;
import tn.esprit.insurance.repositories.UserRepository;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/demo-controller")
@Hidden
public class DemoController {

    private final UserRepository userRepository;


    public DemoController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    public ResponseEntity<String> sayHello() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            User user = userRepository.findByEmail(authentication.getName());
            if (user != null && user.getStatus() == StatusU.Bloque) {
                return ResponseEntity.status(403).body("You cannot access this platform currently. Please contact contact@nano.com for further information.");
            }
        }
        return ResponseEntity.ok("Hello from secured endpoint");
    }

}