package tn.esprit.insurance.controllers;

import tn.esprit.insurance.entities.User;
import tn.esprit.insurance.services.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/jwt")
public class JwtController {
    private final JwtService jwtService;

    public JwtController(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @GetMapping("/username")
    public ResponseEntity<String> getUsernameFromToken(@RequestHeader("Authorization") String token) {
        String username = jwtService.extractUsername(token.substring(7)); // Remove "Bearer " prefix
        return ResponseEntity.ok(username);
    }

}
