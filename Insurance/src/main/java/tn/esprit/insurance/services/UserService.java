package tn.esprit.insurance.services;


import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import tn.esprit.insurance.entities.Profil;
import tn.esprit.insurance.entities.User;
import tn.esprit.insurance.repositories.ProfilRepository;
import tn.esprit.insurance.repositories.UserRepository;
import tn.esprit.insurance.Models.ChangePasswordRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final ProfilRepository profilRepository;
    private final UserRepository repository;

    public List<User> getAllUsers() {
        return repository.findAll();
    }

    // Method to get a user by ID
    public User getUserById(int id) {
        return repository.findById(id).orElse(null);
    }

    // Method to update a user
    public User updateUser(User user) {
        return repository.save(user);
    }

    // Method to delete a user
    public void deleteUser(int id) {
        repository.deleteById(id);
    }
    public void changePassword(ChangePasswordRequest request, Principal connectedUser) {

        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        // check if the current password is correct
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new IllegalStateException("Wrong password");
        }
        // check if the two new passwords are the same
        if (!request.getNewPassword().equals(request.getConfirmationPassword())) {
            throw new IllegalStateException("Password are not the same");
        }

        // update the password
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));

        // save the new password
        repository.save(user);
    }

    public User saveUserWithProfil(User user, Profil profil) {
        Profil savedProfil = profilRepository.save(profil);

        return repository.save(user);
    }
    public User loadUserByEmail(String currentEmail){
       return this.repository.findByEmail(currentEmail);
    }

}
