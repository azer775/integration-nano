package tn.esprit.insurance.Models;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tn.esprit.insurance.entities.Role;
import tn.esprit.insurance.entities.StatusU;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    private String firstname;
    private String lastname;
    private LocalDate DateOfBirth;
    private String email;
    private String password;
    private String adress;
    private String region;
    private Role role;
    private StatusU status;
    private String work;
}
