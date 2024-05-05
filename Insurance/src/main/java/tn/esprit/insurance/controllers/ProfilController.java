package tn.esprit.insurance.controllers;

import tn.esprit.insurance.entities.Profil;
import tn.esprit.insurance.Models.ProfilUpdateRequest;
import tn.esprit.insurance.repositories.ProfilRepository;
import tn.esprit.insurance.services.ProfilService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfilController {

    private final ProfilService profilService;
    private final ProfilRepository profilRepository;

    // Endpoint to get user profile by userId
    @GetMapping("/{userId}")
    public ResponseEntity<Profil> getUserProfil(@PathVariable("userId") int userId) {
        Profil profil = profilService.getUserProfil(userId);
        return ResponseEntity.ok(profil);
    }

    // Endpoint to update user profile
    @PutMapping("/{userId}/update")
    @PreAuthorize("#userId == principal.id")
    public ResponseEntity<String> updateProfil(@PathVariable("userId") int userId,
                                               @RequestBody ProfilUpdateRequest request) {
        profilService.updateUserProfil(userId, request);
        return ResponseEntity.ok("Profile updated successfully");
    }

    @PostMapping("/{userId}/picture")
    @PreAuthorize("#userId == principal.id")
    public void changeProfilePicture(int userId, MultipartFile file) {
        // Check if the file is empty
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }
        if (!file.getContentType().startsWith("image")) {
            throw new IllegalArgumentException("Only image files are allowed");
        }

        Profil profil = profilRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("Profile not found for user with ID " + userId));

        try {
            byte[] imageBytes = file.getBytes();
            profil.setImage(imageBytes);
            profilRepository.save(profil);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read file", e);
        }
    }
}