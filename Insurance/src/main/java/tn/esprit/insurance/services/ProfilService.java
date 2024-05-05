package tn.esprit.insurance.services;

import tn.esprit.insurance.entities.Profil;
import tn.esprit.insurance.Models.ProfilUpdateRequest;
import tn.esprit.insurance.repositories.ProfilRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfilService {
    private final ProfilRepository profilRepository;

    public Profil getUserProfil(int Id) {
        return profilRepository.findByUserId(Id)
                .orElseThrow(() -> new EntityNotFoundException("Profil not found for user with ID " + Id));
    }
    public void updateUserProfil(int Id, ProfilUpdateRequest request) {
        Profil profil = profilRepository.findByUserId(Id)
                .orElseThrow(() -> new EntityNotFoundException("Profil not found for user with ID " + Id));

        profil.setImage(request.getImage());
        profilRepository.save(profil);
    }

    public void deleteProfil(int Id) {
        Profil profil = profilRepository.findByUserId(Id)
                .orElseThrow(() -> new EntityNotFoundException("Profil not found for user with ID " + Id));

        profilRepository.delete(profil);
    }

    @Transactional
    public void changeProfilePicture(int userId, byte[] newImage) {
        Profil profil = profilRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("Profil not found for user with ID " + userId));

        profil.setImage(newImage); // Assuming 'image' is a byte array in the Profil entity representing the picture

        profilRepository.save(profil);
    }

}
