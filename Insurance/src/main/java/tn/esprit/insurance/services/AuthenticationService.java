package tn.esprit.insurance.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import tn.esprit.insurance.entities.*;
import tn.esprit.insurance.Models.AuthenticationRequest;
import tn.esprit.insurance.Models.AuthenticationResponse;
import tn.esprit.insurance.Models.EmailVerificationRequest;
import tn.esprit.insurance.Models.RegisterRequest;
import tn.esprit.insurance.repositories.ProfilRepository;
import tn.esprit.insurance.repositories.TokenRepository;
import tn.esprit.insurance.repositories.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.stereotype.Service;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository repository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final JavaMailSender javaMailSender;
    private final ProfilRepository profilRepository;
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);

    public AuthenticationResponse register(RegisterRequest request) {
        Profil profil = Profil.builder()
                .build();

        var user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .DateOfBirth(request.getDateOfBirth())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .adress(request.getAdress())
                .region(request.getRegion())
                .role(request.getRole())
                .status(StatusU.Bloque)
                .work(request.getWork())
                .verificationCode(VerificationCodeGenerator.generateVerificationCode(6))
                .profil(profil)
                .build();


        profil.setUser(user);
        User savedUser = repository.save(user);
        profil = profilRepository.save(profil);


        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        saveUserToken(savedUser, jwtToken);
        sendVerificationCodeEmail(user.getEmail(), user.getVerificationCode());
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = repository.findByEmail(request.getEmail())
                ;
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    private void saveUserToken(User user, String jwtToken) {

        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;
        if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
            return;
        }
        refreshToken = authHeader.substring(7);
        userEmail = jwtService.extractUsername(refreshToken);
        if (userEmail != null) {
            var user = this.repository.findByEmail(userEmail);
            if (jwtService.isTokenValid(refreshToken, user)) {
                var accessToken = jwtService.generateToken(user);
                revokeAllUserTokens(user);
                saveUserToken(user, accessToken);
                var authResponse = AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }



    private void sendVerificationCodeEmail(String userEmail, String verificationCode) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(userEmail);
            mailMessage.setSubject("Verification Code");
            mailMessage.setText("Your verification code is: " + verificationCode);
            javaMailSender.send(mailMessage);
        } catch (MailException e) {
            logger.error("Failed to send verification code email to " + userEmail + ": " + e.getMessage(), e);
            // Handle email sending failure
        }
    }

    public class VerificationCodeGenerator {

        private static final SecureRandom secureRandom = new SecureRandom();

        public static String generateVerificationCode(int length) {
            return new BigInteger(length * 5, secureRandom).toString(32).substring(0, length);
        }
    }

    public ResponseEntity<String> verifyEmail(EmailVerificationRequest verificationRequest) {
        String email = verificationRequest.getEmail();
        String verificationCode = verificationRequest.getVerificationCode();


        // Retrieve the user from the database based on the email
        User user = repository.findByEmail(email);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found for email: " + email);
        }

        // Check if the verification code matches
        if (!user.getVerificationCode().equals(verificationCode)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid verification code");
        }

        user.setStatus(StatusU.valueOf("Actif"));
        repository.save(user);

        // Return a ResponseEntity indicating success
        return ResponseEntity.ok("Email verified successfully. Welcome to NanoCash!");

    }



}

